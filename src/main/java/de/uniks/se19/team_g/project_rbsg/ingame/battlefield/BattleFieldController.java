package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.PlayerListController;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

/**
 * @author Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable, Rincled
{

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH / 2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60) / 2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);

    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MiniMapDrawer miniMapDrawer;
    private final ChangeListener<Cell> onSelectedUnitMoved = this::onSelectedUnitMoved;

    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public Button mapButton;
    public Canvas miniMapCanvas;
    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;
    public VBox root;
    public Button musicButton;
    private Canvas canvas;
    private ZoomableScrollPane zoomableScrollPane;
    public StackPane battlefieldStackPane;
    public AnchorPane overlayAnchorPane;
    public StackPane miniMapStackPane;

    private Game game;

    private ObservableList<Unit> units;

    private int zoomFactor = 1;

    private final ChangeListener<Hoverable> onHoveredChanged = this::onHoveredChanged;
    private final ChangeListener<Selectable> onSelectedChanged = this::onSelectedChanged;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    private final PropertyChangeListener highlightingListener = this::highlightingChanged;
    private final ChangeListener<Number> cameraViewChangedListener = this::cameraViewChanged;

    private TileDrawer tileDrawer;
    private Tile[][] tileMap;
    private int mapSize;
    private Camera camera;

    @Nonnull
    private final MovementManager movementManager;
    @Nonnull
    private final MusicManager musicManager;

    private IngameContext context;
    private final ChatBuilder chatBuilder;
    private ChatController chatController;

    @FXML
    public Button chatButton;
    @FXML
    public StackPane chatPane;

    @FXML
    public Button ingameInformationsButton;
    @FXML
    public HBox playerBar;
    @FXML
    public Pane player1;
    @FXML
    public Pane player2;
    @FXML
    public Pane player3;
    @FXML
    public Pane player4;
    @FXML
    public Label roundTextLabel;
    @FXML
    public Label roundCountLabel;
    @FXML
    public Label phaseLabel;
    @FXML
    public ImageView phaseImage;
    @FXML
    public HBox ingameInformationHBox;
    @FXML
    public HBox menueBar;

    private PlayerListController playerListController;

    private SimpleIntegerProperty roundCount;
    private int roundCounter;

    @Autowired
    public BattleFieldController(
            @Nonnull final SceneManager sceneManager,
            @Nonnull final AlertBuilder alertBuilder,
            @Nonnull final MovementManager movementManager,
            @Nonnull final ChatBuilder chatBuilder,
            @Nonnull final ChatController chatController,
            @Nonnull final MusicManager musicManager
    ) {
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.movementManager = movementManager;
        this.musicManager = musicManager;
        this.tileDrawer = new TileDrawer();
        this.miniMapDrawer = new MiniMapDrawer();
        this.chatBuilder=chatBuilder;
        this.chatController=chatController;
        this.roundCount = new SimpleIntegerProperty();
        this.roundCounter = 1;
    }

    public void initialize()
    {

        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                40
        );

        JavaFXUtils.setButtonIcons(
                zoomInButton,
                getClass().getResource("/assets/icons/navigation/zoomInWhite.png"),
                getClass().getResource("/assets/icons/navigation/zoomInBlack.png"),
                40
        );

        JavaFXUtils.setButtonIcons(
                zoomOutButton,
                getClass().getResource("/assets/icons/navigation/zoomOutWhite.png"),
                getClass().getResource("/assets/icons/navigation/zoomOutBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                ingameInformationsButton,
                getClass().getResource("/assets/icons/operation/accountWhite.png"),
                getClass().getResource("/assets/icons/operation/accountBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                endPhaseButton,
                getClass().getResource("/assets/icons/operation/endPhaseWhite.png"),
                getClass().getResource("/assets/icons/operation/endPhaseBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                chatButton,
                getClass().getResource("/assets/icons/operation/chatBubbleWhite.png"),
                getClass().getResource("/assets/icons/operation/chatBubbleBlack.png"),
                40
        );

        miniMapCanvas.visibleProperty().bindBidirectional(miniMapStackPane.visibleProperty());
        miniMapCanvas.setVisible(true);
        JavaFXUtils.setButtonIcons(
                mapButton,
                getClass().getResource("/assets/icons/operation/mapClosedWhite.png"),
                getClass().getResource("/assets/icons/operation/mapClosedBlack.png"),
                40);

        musicManager.initButtonIcons(musicButton);
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    @SuppressWarnings("unused")
    private void onHoveredChanged(
        ObservableValue<? extends Hoverable> observableValue,
        Hoverable last,
        Hoverable next
    ) {
        // update the mini map on next draw cycle so that it doesn't cause issues with listener order
        Platform.runLater(() ->miniMapDrawer.drawMinimap(tileMap));
    }

    @SuppressWarnings("unused")
    private void onSelectedChanged(
        ObservableValue<? extends Selectable> observableValue,
        Selectable last,
        Selectable next
    ) {
        Unit selectedUnit = context.getGameState().getSelectedUnit();

        if (last instanceof Unit) {
            ((Unit) last).positionProperty().removeListener(onSelectedUnitMoved);
        }
        if (next instanceof Unit) {
            ((Unit) next).positionProperty().addListener(onSelectedUnitMoved);
        }

        Platform.runLater(() -> {
            miniMapDrawer.drawMinimap(tileMap);

            // in this case, last and current selected units are null, so no update needed
            if (selectedUnit == null && !(last instanceof Unit)) {
                return;
            }

            setCellProperty(selectedUnit);
        });



//        logger.debug(String.valueOf(zoomableScrollPane.getScaleValue()));
//        logger.debug(String.valueOf(zoomableScrollPane.getHeight()));
//        logger.debug(String.valueOf(zoomableScrollPane.getWidth()));
    }

    @SuppressWarnings("unused")
    private void onSelectedUnitMoved(Observable observable, Cell last, Cell next) {
        Platform.runLater(() -> {
            miniMapDrawer.drawMinimap(tileMap);
            setCellProperty(context.getGameState().getSelectedUnit());
        });
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        while (c.next())
        {
            logger.debug(c.toString());
            if (c.wasAdded())
            {
                for (Unit unit : c.getAddedSubList())
                {
                    unit.positionProperty().addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                }
            }

            if (c.wasRemoved())
            {
                for (Unit unit : c.getRemoved())
                {
                    unit.positionProperty().removeListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition, Unit unit)
    {
        if (lastPosition != null)
        {
            tileDrawer.drawTile(getTileOf(lastPosition));
        }
        if (newPosition != null)
        {
            tileDrawer.drawTile(getTileOf(newPosition));
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    @SuppressWarnings("unused")
    private void cameraViewChanged(ObservableValue<? extends Number> observableValue, Number number, Number number1)
    {
        miniMapDrawer.drawMinimap(tileMap);
    }


    private void initCanvas()
    {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        canvas.setHeight(CELL_SIZE*mapSize);
        canvas.setWidth(CELL_SIZE*mapSize);
        battlefieldStackPane.getChildren().add(0,zoomableScrollPane);
        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
    }
    private void initPlayerBar(){
        HashMap<String, Player> playerMap = new HashMap<>();
        HashMap<String, Node> playerNodeMap=new HashMap<>();
        HashMap<String, Pane> playerPaneMap=new HashMap<>();
        ArrayList<Pane> playerCardList = new ArrayList<>();

        playerCardList.add(player1);
        playerCardList.add(player2);
        playerCardList.add(player3);
        playerCardList.add(player4);

        playerListController=new PlayerListController(this.game);
        playerBar.setAlignment(Pos.TOP_CENTER);

        int counter=0;
        if(this.game.getPlayers().size()==2){
            playerBar.getChildren().remove(player1);
            playerCardList.remove(player1);
            playerBar.getChildren().remove(player4);
            playerCardList.remove(player4);
        }

        for(Player player : this.game.getPlayers()){

            playerCardList.get(counter).getChildren().add(playerListController.getPlayerCards().get(counter));
            playerPaneMap.put(player.getId(), playerCardList.get(counter));
            playerMap.put(player.getId(), player);
            playerNodeMap.put(player.getId(), playerListController.getPlayerCards().get(counter));

            counter++;
        }

        playerListController=new PlayerListController(game);
        playerBar.setVisible(false);
        playerBar.setPickOnBounds(false);
        battlefieldStackPane.setPickOnBounds(false);
        miniMapStackPane.setPickOnBounds(false);

        if(!playerNodeMap.isEmpty() && this.game.getCurrentPlayer()!=null){
            playerNodeMap.get(this.game.getCurrentPlayer().getId()).setStyle("-fx-background-color: -selected-background-color");
        }
        this.game.currentPlayerProperty().addListener((observable, oldPlayer, newPlayer) -> {
            if(oldPlayer !=null){
                playerNodeMap.get(oldPlayer.getId()).setStyle("-fx-background-color: -root-background-color");
            }
            if(newPlayer!=null){
                playerNodeMap.get(newPlayer.getId()).setStyle("-fx-background-color: -selected-background-color");
            }
        });
        this.game.getPlayers().addListener((ListChangeListener<Player>) l -> {
            if(!l.next()){
                return;
            }
            List<? extends Player> removedPlayer = l.getRemoved();
            Platform.runLater(()->{
                for(Player player : removedPlayer){
                    playerPaneMap.get(player.getId()).getChildren().remove(0);
                    playerPaneMap.get(player.getId()).getChildren().add(playerListController.createLoserCard(player));
                }
            });
        });

        this.game.phaseProperty().addListener((observable, oldVal, newVal) -> {

                if(oldVal==null){
                    return;
                }
                if(oldVal.equals("lastMovePhase") && (roundCounter % this.game.getPlayers().size())==0){
                    Platform.runLater(() -> roundCount.set(roundCount.get()+1));
                    roundCounter=0;
                }
                roundCounter++;
        });

        this.game.phaseProperty().addListener(((observable, oldValue, newValue) -> {
            switch(newValue){
                case "movePhase": {
                    Image image = new Image(this.getClass().getResourceAsStream("/assets/icons/operation/footstepsWhite.png"));
                    phaseImage.imageProperty().setValue(image);
                }break;
                case "attackPhase": {
                    Image image = new Image(this.getClass().getResourceAsStream("/assets/icons/operation/swordClashWhite.png"));
                    phaseImage.imageProperty().setValue(image);
                }break;
                case "lastMovePhase": {
                    Image image = new Image(this.getClass().getResourceAsStream("/assets/icons/operation/footprintWhite.png"));
                    phaseImage.imageProperty().setValue(image);
                }break;
            }
        }));

        roundCount.set(0);
        roundCountLabel.textProperty().bind(roundCount.asString());
        phaseLabel.setText("Phase");
        ingameInformationHBox.setStyle("-fx-background-color: -surface-elevation-8-color");
        //ingameInformationHBox.setSpacing(10);
        HBox.setMargin(ingameInformationHBox, new Insets(10,10 ,10,10));
        //phaseLabel.textProperty().bind(this.game.phaseProperty());
        roundTextLabel.textProperty().setValue("Round");
    }

    protected Tile resolveTargetTile(MouseEvent event) {
        int xPos = (int) (event.getX() / CELL_SIZE);
        int yPos = (int) (event.getY() / CELL_SIZE);

        return tileMap[yPos][xPos];
    }

    protected Cell resolveTargetCell(MouseEvent event) {
        return resolveTargetTile(event).getCell();
    }

    public void canvasHandleMouseMove(MouseEvent event) {
        Cell cell = resolveTargetCell(event);
        Unit unit = cell.getUnit();

        if (context.getGameState() == null) {
            return;
        }

        context.getGameState().setHovered(Objects.requireNonNullElse(unit, cell));
    }

    public void canvasHandleMouseClicked(MouseEvent event) {
        Tile tile = resolveTargetTile(event);

        if (tile == null) {
            return;
        }

        if (handleAttack(tile)) {
            return;
        }

        if (handleMovement(tile)) {
            return;
        }

        handleSelection(tile);
    }

    protected void handleSelection(Tile tile) {
        Cell cell = tile.getCell();
        Unit unit = cell.getUnit();
        Selectable selectable = Objects.requireNonNullElse(unit, cell);

        if (selectable == context.getGameState().getSelected()) {
            selectable.clearSelection();
        } else {
            context.getGameState().setSelected(selectable);
        }
    }

    private boolean handleAttack(Tile tile) {

        Cell targetCell = tile.getCell();
        Unit selectedUnit = context.getGameState().getSelectedUnit();
        Unit targetUnit = targetCell.getUnit();

        if (
               !context.isMyTurn()
            || !context.getGameState().isPhase(Game.Phase.attackPhase)
            || Objects.isNull(selectedUnit)
            || selectedUnit.getLeader() != context.getUserPlayer()
            || !selectedUnit.canAttack(targetUnit)
        ) {
            return false;
        }

        context.getGameEventManager().api().attack(selectedUnit, targetUnit);
        selectedUnit.setAttackReady(false);
        game.setSelectedUnit(null);

        return true;
    }

    private boolean handleMovement(Tile tile) {
        if (!context.isMyTurn()) {
            return false;
        }
        Game game = context.getGameState();

        if (
                !game.isPhase(Game.Phase.movePhase)
            &&  !game.isPhase(Game.Phase.lastMovePhase)
        ) {
            return false;
        }

        Cell cell = tile.getCell();

        if (cell.unitProperty().get() != null) {
            return false;
        }

        Unit selectedUnit = game.getSelectedUnit();
        if (selectedUnit == null) {
            return false;
        }

        if (selectedUnit.getLeader() != context.getUserPlayer()) {
            return false;
        }

        Tour tour = movementManager.getTour(selectedUnit, cell);
        if (tour == null) {
            return false;
        }

        Map<String, Object> command = CommandBuilder.moveUnit(selectedUnit, tour.getPath());
        game.setInitiallyMoved(true);
        selectedUnit.setRemainingMovePoints(
                selectedUnit.getRemainingMovePoints() - tour.getCost()
        );
        if (selectedUnit.getRemainingMovePoints() == 0) {
            selectedUnit.clearSelection();
        }
        context.getGameEventManager().sendMessage(command);

        return true;
    }

    public void leaveGame(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::doLeaveGame,
                        null);
    }

    private void doLeaveGame()
    {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

    public void zoomIn(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (zoomFactor == 1)
        {
            zoomableScrollPane.onScroll(20.0, ZOOMPANE_CENTER);
            zoomFactor++;
        }
        else if (zoomFactor == 0)
        {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
        else if (zoomFactor == -1 && context.getGameData().getNeededPlayer() == 4)
        {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
    }

    public void zoomOut(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (zoomFactor == 2)
        {
            zoomableScrollPane.onScroll(-20.0, ZOOMPANE_CENTER);
            zoomFactor--;
        }
        else if (zoomFactor == 1)
        {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
        else if (zoomFactor == 0 && context.getGameData().getNeededPlayer() == 4)
        {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
    }

    public void endPhase()
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_PHASE,
                        this::doEndPhase,
                        null);
    }

    public void toggleMap(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (miniMapCanvas.visibleProperty().get())
        {
            miniMapCanvas.visibleProperty().set(false);
            JavaFXUtils.setButtonIcons(
                    mapButton,
                    getClass().getResource("/assets/icons/operation/mapClosedWhite.png"),
                    getClass().getResource("/assets/icons/operation/mapClosedBlack.png"),
                    40);
        }
        else
        {
            miniMapCanvas.visibleProperty().set(true);
            JavaFXUtils.setButtonIcons(
                    mapButton,
                    getClass().getResource("/assets/icons/operation/mapWhite.png"),
                    getClass().getResource("/assets/icons/operation/mapBlack.png"),
                    40);
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void doEndPhase(){
        this.context.getGameEventManager().sendEndPhaseCommand();
        this.context.getGameState().clearSelection();
    }

    @Override
    public void configure(@Nonnull IngameContext context) {
        this.context = context;

        context.getGameState().currentPlayerProperty().addListener(this::onNextPlayer);
        if (context.getGameState().getCurrentPlayer() != null) {
            onNextPlayer(null, null, context.getGameState().getCurrentPlayer());
        }

        game = context.getGameState();
        if (game != null) {
            ObservableList<Cell> cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells) {
                Tile tile = new Tile(cell);
                tileMap[cell.getY()][cell.getX()] = tile;
                tile.addListener(highlightingListener);
            }

            for (Unit unit : units) {
                //Adds listener for units which are already in the list
                unit.positionProperty().addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
            }

            initCanvas();
            camera = new Camera(zoomableScrollPane.scaleValueProperty(), zoomableScrollPane.hvalueProperty(),
                                zoomableScrollPane.vvalueProperty(), mapSize, zoomableScrollPane.heightProperty(),
                                zoomableScrollPane.widthProperty());
            initMiniMap();
            initPlayerBar();
            miniMapDrawer.setCamera(camera);
        }  // exception

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(unitListListener);
        game.selectedProperty().addListener(onSelectedChanged);
        game.hoveredProperty().addListener(onHoveredChanged);


        //Add Listeners to the Zoomable ScrollPane
        zoomableScrollPane.scaleValueProperty().addListener(cameraViewChangedListener);
        zoomableScrollPane.hvalueProperty().addListener(cameraViewChangedListener);
        zoomableScrollPane.vvalueProperty().addListener(cameraViewChangedListener);

        miniMapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::miniMapHandleMouseClick);

        this.context.getGameState().selectedUnitProperty()
                .addListener((observable, oldUnit, newUnit) -> setCellProperty(newUnit));

        this.context.getGameState().phaseProperty()
                .addListener(this::onNextPhase);

        configureEndPhase();

        addAlertListeners();

        try {
            initChat();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onNextPhase(Observable observable, String lastPhase, String nextPhase) {
        setCellProperty(null);
        game.getCurrentPlayer().getUnits().forEach(unit -> unit.setAttackReady(true));
    }

    private void addAlertListeners() {
        game.winnerProperty().addListener(((observable, oldValue, newValue) -> {
            if (newValue != null) {
                //TODO: Check if we are the winner. If yes, show other (priorityInformation) alert (Mockup)
                showWinnerLoserAlert(newValue);
            }
        }));
        //TODO: Add listeners to the users own units property
        //TODO: If the user lost ALL units AND their loss doesn't cause another player's win show the game lost (priorityConfirmation) alert (Mockup)
        this.context.getUserPlayer().getUnits().addListener((ListChangeListener<Unit>) unitList -> {
            if (unitList.getList().isEmpty() && this.context.getGameState().getPlayers().size() > 2){
                alertBuilder.priorityConfirmation(
                        AlertBuilder.Text.GAME_LOST,
                        () -> this.context.getGameData().setSpectatorModus(true),
                        () -> doLeaveGame()
                );
            }
        });
    }

    private void showWinnerLoserAlert(Player winner) {
        if (winner.equals(this.context.getUserPlayer())){
            alertBuilder.priorityInformation(
                    AlertBuilder.Text.GAME_WON,
                    () -> doLeaveGame());
        } else {
            alertBuilder.priorityInformation(
                    AlertBuilder.Text.GAME_SOMEBODY_ELSE_WON,
                    () -> doLeaveGame(),
                    winner.getName());
        }
    }



    private void initChat() throws Exception {
        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(context.getGameEventManager());
        chatPane.getChildren().add(chatComponents.getRoot());
        chatController = chatComponents.getController();
        chatPane.setVisible(false);
    }

    public void openChat(){

        if(chatPane.isVisible()){
            chatPane.setVisible(false);
            JavaFXUtils.setButtonIcons(
                    chatButton,
                    getClass().getResource("/assets/icons/operation/chatBubbleWhite.png"),
                    getClass().getResource("/assets/icons/operation/chatBubbleBlack.png"),
                    40
            );
        }
        else {
            chatPane.setVisible(true);
            JavaFXUtils.setButtonIcons(
                    chatButton,
                    getClass().getResource("/assets/icons/operation/chatWhite.png"),
                    getClass().getResource("/assets/icons/operation/chatBlack.png"),
                    40
            );
        }
    }

    public Tile getTileOf(Object positioned) {

        Cell position;

        if (positioned instanceof Unit) {
            position = ((Unit) positioned).getPosition();
        } else {
            position = (Cell) positioned;
        }

        return tileMap[position.getY()][position.getX()];
    }

    private void setCellProperty(@Nullable Unit selectedUnit) {
        if (selectedUnit == null) {
            for (Cell cell : this.context.getGameState().getCells()) {
                cell.setIsReachable(false);
            }
            return;
        }

        if (
            selectedUnit.getLeader() == null
            || !selectedUnit.getLeader().isPlayer()
            || (!this.context.isMyTurn())
        ) {
            return;
        }

        if (!this.context.getGameState().isPhase(Game.Phase.attackPhase)) {
            setMoveRadius(selectedUnit);
        }
    }

    private void setMoveRadius(@Nonnull Unit selectedUnit) {
        for(Cell cell: this.context.getGameState().getCells()) {
            if (this.movementManager.getTour(selectedUnit, cell) != null){
                cell.setIsReachable(true);
            } else{
                cell.setIsReachable(false);
            }
        }
    }

    private void miniMapHandleMouseClick(MouseEvent mouseEvent)
    {
        int xPos = miniMapDrawer.getXPostionOnMap(mouseEvent.getX());
        int yPos = miniMapDrawer.getYPostionOnMap(mouseEvent.getY());
        logger.debug("Pos: " + xPos + " " + yPos);
        camera.TryToCenterToPostition(xPos, yPos);
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void configureEndPhase() {
        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty(false);
        ObjectProperty<Player> currentPlayerProperty = this.context.getGameState().currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> (context.isMyTurn() && this.context.getGameState().getInitiallyMoved()),
                currentPlayerProperty, this.context.getGameState().initiallyMovedProperty()
        ));

        playerCanEndPhase.addListener(((observable, oldValue, newValue) -> {}) );

        currentPlayerProperty.addListener((observable, oldValue, newValue) -> this.context.getGameState().setInitiallyMoved(false));

        endPhaseButton.disableProperty().bind(playerCanEndPhase.not());

        endPhaseButton.disableProperty().addListener(((observable, oldValue, newValue) -> {}));
    }

    @SuppressWarnings("unused")
    private void onNextPlayer(Observable observable, Player lastPlayer, Player nextPlayer) {
        if (context.isMyTurn()) {
            onBeforeUserTurn();
        }
    }

    private void onBeforeUserTurn() {
        for (Unit unit : context.getUserPlayer().getUnits()) {
            unit.setRemainingMovePoints(unit.getMp());
        }
    }

    private void initMiniMap()
    {
        miniMapDrawer.setCanvas(miniMapCanvas, mapSize);
        miniMapDrawer.drawMinimap(tileMap);
    }

    /**
     * i don't think, we need to remove any listeners at all in the battlefield controller,
     * since we throw away the whole game anyway.
     */
    @Override
    public void terminate()
    {
        @Nullable Game gameState = context.getGameState();

        if (gameState == null) {
            return;
        }

        gameState.selectedProperty().removeListener(onSelectedChanged);
        gameState.hoveredProperty().removeListener(onHoveredChanged);

        for (Tile[] tileArray : tileMap)
        {
            for (Tile tile : tileArray)
            {
                tile.removeListener(highlightingListener);
            }
        }

        for (Unit unit : units)
        {
            unit.positionProperty()
                    .removeListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
        }

        units.removeListener(unitListListener);

        zoomableScrollPane.scaleValueProperty().removeListener(cameraViewChangedListener);
        zoomableScrollPane.hvalueProperty().removeListener(cameraViewChangedListener);
        zoomableScrollPane.vvalueProperty().removeListener(cameraViewChangedListener);
    }

    public void openPlayerBar(@SuppressWarnings("unused") ActionEvent event){
        if(!playerBar.visibleProperty().get()){
            playerBar.visibleProperty().setValue(true);
            playerBar.toFront();
        }else
        {
            playerBar.visibleProperty().setValue(false);
            playerBar.toBack();
        }
    }

    public void toggleMusic() {
        musicManager.toggleMusicAndUpdateButtonIconSet(musicButton);
    }
}
