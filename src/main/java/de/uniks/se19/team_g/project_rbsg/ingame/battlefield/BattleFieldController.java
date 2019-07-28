package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.PlayerListController;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingOne;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingTwo;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
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
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
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

    private static final String MOVE_PHASE = "movePhase";
    private static final String ATTACK_PHASE = "attackPhase";
    private static final String LAST_MOVE_PHASE = "lastMovePhase";

    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MiniMapDrawer miniMapDrawer;

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

    private ObservableList<Cell> cells;
    private ObservableList<Unit> units;

    private int zoomFactor = 1;

    private final ChangeListener<Tile> hoveredTileListener = this::hoveredTileChanged;
    private final ChangeListener<Tile> selectedTileListener = this::selectedTileChanged;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    private final PropertyChangeListener highlightingListener = this::highlightingChanged;
    private final ChangeListener<Number> cameraViewChangedListener = this::cameraViewChanged;

    private TileDrawer tileDrawer;
    private Tile[][] tileMap;
    private int mapSize;
    private Camera camera;

    public SimpleObjectProperty<Tile> selectedTileProperty()
    {
        return selectedTile;
    }

    public Tile getSelectedTile()
    {
        return selectedTile.get();
    }

    public void setSelectedTile(@Nullable Tile selectedTile) {
        this.selectedTile.set(selectedTile);
    }

    @Nonnull
    final private SimpleObjectProperty<Tile> selectedTile;
    @Nonnull
    final private SimpleObjectProperty<Tile> hoveredTile;

    @Nonnull
    private final MovementManager movementManager;
    @Nonnull
    private final MusicManager musicManager;

    private IngameContext context;

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
            @Nonnull final MusicManager musicManager
            ) {
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.movementManager = movementManager;
        this.musicManager = musicManager;
        this.tileDrawer = new TileDrawer();
        this.miniMapDrawer = new MiniMapDrawer();
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);
        this.roundCount = new SimpleIntegerProperty();
        this.roundCounter = 1;
    }

    public Tile getHoveredTile()
    {
        return hoveredTile.get();
    }

    public SimpleObjectProperty<Tile> hoveredTileProperty()
    {
        return hoveredTile;
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

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile != null && oldTile.getHighlightingTwo() == HighlightingTwo.HOVERED) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if(newTile != null && newTile.getHighlightingTwo() == HighlightingTwo.NONE) {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile) {
        if (oldTile != null) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if ((newTile != null) && (newTile.getCell().getUnit() != null) && (isMyUnit(newTile.getCell().getUnit()))){
            newTile.setHighlightingTwo(HighlightingTwo.SELECETD_WITH_UNITS);
        } else if(newTile != null) {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
        miniMapDrawer.drawMinimap(tileMap);
//        logger.debug(String.valueOf(zoomableScrollPane.getScaleValue()));
//        logger.debug(String.valueOf(zoomableScrollPane.getHeight()));
//        logger.debug(String.valueOf(zoomableScrollPane.getWidth()));
    }

    private boolean isMyUnit(@NonNull Unit unit){
        if (unit.getLeader() == null){
            return false;
        }
        if (unit.getLeader().getName().equals(context.getUser().getName())){
            return true;
        } else {
            return false;
        }
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


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition, Unit unit)
    {
        if (lastPosition != null)
        {
            tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        }
        if (newPosition != null)
        {
            tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

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
        HashMap<String, Player> playerMap=new HashMap<>();
        HashMap<String, Node> playerNodeMap=new HashMap<>();
        HashMap<String, Pane> playerPaneMap=new HashMap<>();
        ArrayList<Pane> playerCardList = new ArrayList<Pane>();

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
        this.game.currentPlayerProperty().addListener((observable, oldVal, newVal) -> {
            Player oldPlayer = oldVal;
            if(oldPlayer!=null){
                playerNodeMap.get(oldPlayer.getId()).setStyle("-fx-background-color: -root-background-color");
            }
            if(newVal!=null){
                Player newPlayer = newVal;
                playerNodeMap.get(newPlayer.getId()).setStyle("-fx-background-color: -selected-background-color");
            }
        });
        this.game.getPlayers().addListener((ListChangeListener) l -> {
            if(!l.next()){
                return;
            }
            @SuppressWarnings("unchecked")
            List<Player> removedPlayer = (List<Player>)l.getRemoved();
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
                    Platform.runLater(() -> {
                    roundCount.set(roundCount.get()+1);
                    });
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

    public void canvasHandleMouseMove(MouseEvent event) {
        Tile tile = resolveTargetTile(event);
        hoveredTile.set(tile);
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
            setSelectedTile(null);
            this.context.getGameState().setSelectedUnit(null);
            return;
        }

        if (tile.getCell().getUnit() != null){
            Unit unit = tile.getCell().getUnit();
            onUnitSelection(unit, tile);
        } else {
            onTileSelection(tile);
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
            || Objects.isNull(targetUnit)
            || !targetCell.isIsAttackable()
            || targetUnit.getLeader() == context.getUserPlayer()
        ) {
            return false;
        }

        context.getGameEventManager().api().attack(selectedUnit, targetUnit);
        game.setSelectedUnit(null);
        setSelectedTile(null);

        return true;
    }

    private void onUnitSelection(Unit unitClicked, Tile tileClicked){
        if(unitClicked.equals(game.getSelectedUnit())){
            game.setSelectedUnit(null);
            selectedTile.set(null);
            hoveredTile.set(null);
            setCellProperty(null);
        } else {
            game.setSelectedUnit(unitClicked);
            selectedTile.set(tileClicked);
        }
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
        game.setSelectedUnit(null);
        setSelectedTile(null);
        context.getGameEventManager().sendMessage(command);

        return true;
    }

    protected void onTileSelection(Tile tileClicked) {
        if(tileClicked.equals(selectedTile.get())) {
            selectedTile.set(null);
            hoveredTile.set(null);
        }
        else{
            selectedTile.set(tileClicked);
            setCellProperty(null);
            this.context.getGameState().setSelectedUnit(null);
            setSelectedTile(null);

        }
    }

    public void leaveGame(ActionEvent actionEvent)
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

    public void zoomIn(ActionEvent actionEvent)
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

    public void zoomOut(ActionEvent actionEvent)
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

    public void toggleMap(ActionEvent actionEvent)
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
        this.context.getGameState().setSelectedUnit(null);
        setSelectedTile(null);
    }

    @Override
    public void configure(@Nonnull IngameContext context) {
        this.context = context;

        context.getGameState().currentPlayerProperty().addListener(this::onNextPlayer);
        if (context.getGameState().getCurrentPlayer() != null) {
            onNextPlayer(null, null, context.getGameState().getCurrentPlayer());
        }

        Game gameState = context.getGameState();
        game = gameState;
        if (game != null) {
            cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells) {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell);
                tileMap[cell.getY()][cell.getX()].addListener(highlightingListener);
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
        } else {
            // exception
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(unitListListener);
        selectedTile.addListener(selectedTileListener);
        hoveredTile.addListener(hoveredTileListener);


        //Add Listeners to the Zoomable ScrollPane
        zoomableScrollPane.scaleValueProperty().addListener(cameraViewChangedListener);
        zoomableScrollPane.hvalueProperty().addListener(cameraViewChangedListener);
        zoomableScrollPane.vvalueProperty().addListener(cameraViewChangedListener);

        miniMapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::miniMapHandleMouseClick);

        this.context.getGameState().selectedUnitProperty()
                .addListener((observable, oldUnit, newUnit) -> setCellProperty(newUnit));

        this.context.getGameState().phaseProperty()
                .addListener(((observable, oldValue, newValue) -> setCellProperty(null)));

        configureEndPhase();

        configureCells();
    }

    private void configureCells() {
        for (Cell cell: this.context.getGameState().getCells()){
            cell.isReachableProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    cell.getTile().setHighlightingOne(HighlightingOne.MOVE);
                } else {
                    cell.getTile().setHighlightingOne(HighlightingOne.NONE);
                }
            }));

            cell.isAttackableProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue) {
                    cell.getTile().setHighlightingOne(HighlightingOne.ATTACK);
                    setUnitAttackProperty(cell);
                } else {
                    cell.getTile().setHighlightingOne(HighlightingOne.NONE);
                    removeUnitAttackProperty(cell);
                }
            }));
        }
    }

    private void removeUnitAttackProperty(@NonNull Cell cell){
        if (cell.getUnit() == null){
            return;
        }
        Unit unit = cell.getUnit();
        if (isMyUnit(unit)){
            return;
        }
        unit.setAttackable(false);
    }

    private void setUnitAttackProperty(@NonNull Cell cell){
        if (cell.getUnit() == null){
            return;
        }
        Unit unit = cell.getUnit();
        if (isMyUnit(unit)){
            return;
        }
        unit.setAttackable(true);
    }

    private void setCellProperty(@Nullable Unit selectedUnit) {
        if (selectedUnit == null) {
            for (Cell cell : this.context.getGameState().getCells()) {
                cell.setIsReachable(false);
                cell.setIsAttackable(false);
                removeUnitAttackProperty(cell);
            }
            return;
        }

        if ((!isMyUnit(selectedUnit)) || (!this.context.isMyTurn())) {
            return;
        }
        if (this.context.getGameState().isPhase(Game.Phase.attackPhase)) {
            setAttackRadius(selectedUnit);
        } else {
            setMoveRadius(selectedUnit);
        }
        selectedUnit.getPosition().getTile().setHighlightingOne(HighlightingOne.NONE);
    }

    private void setMoveRadius(@NonNull Unit selectedUnit) {
        for(Cell cell: this.context.getGameState().getCells()) {
            if (this.movementManager.getTour(selectedUnit, cell) != null){
                cell.setIsReachable(true);
            } else{
                cell.setIsReachable(false);
            }
        }
    }


    private void setAttackRadius(@NonNull Unit selectedUnit){
        Cell position = selectedUnit.getPosition();
        for (Cell cell: this.context.getGameState().getCells()){
            cell.setIsAttackable(false);
        }
        if (position.getLeft() != null){
            position.getLeft().setIsAttackable(true);
        }
        if (position.getTop() != null){
            position.getTop().setIsAttackable(true);
        }
        if (position.getRight() != null){
            position.getRight().setIsAttackable(true);
        }
        if (position.getBottom() != null) {
            position.getBottom().setIsAttackable(true);
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
    @Override
    public void terminate()
    {
        selectedTile.removeListener(selectedTileListener);
        hoveredTile.removeListener(hoveredTileListener);
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
    public void openPlayerBar(@Nonnull final ActionEvent event){
        if(playerBar.visibleProperty().get()==false){
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
