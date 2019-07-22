package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.HighlightingTwo;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public Button mapButton;
    public Canvas miniMapCanvas;
    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;
    public VBox root;
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
    private final ChangeListener<Cell> unitPositionListener = this::unitChangedPosition;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    private final PropertyChangeListener highlightingListener = this::highlightingChanged;

    private TileDrawer tileDrawer;
    private Tile[][] tileMap;
    private int mapSize;

    @Nonnull
    final private SimpleObjectProperty<Tile> selectedTile;
    @Nonnull
    final private SimpleObjectProperty<Tile> hoveredTile;

    @Nonnull
    private final MovementManager movementManager;

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

    private PlayerListController playerListController;

    private int roundCount;
    private int phaseCount;

    @Autowired
    public BattleFieldController(
            @NonNull final SceneManager sceneManager,
            @NonNull final AlertBuilder alertBuilder,
            @Nonnull final MovementManager movementManager
    ) {
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.movementManager = movementManager;
        this.tileDrawer = new TileDrawer();
        this.miniMapDrawer = new MiniMapDrawer();
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);
    }

    public Tile getSelectedTile()
    {
        return selectedTile.get();
    }

    public SimpleObjectProperty<Tile> selectedTileProperty()
    {
        return selectedTile;
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
        miniMapCanvas.setVisible(false);
        JavaFXUtils.setButtonIcons(
                mapButton,
                getClass().getResource("/assets/icons/operation/mapClosedWhite.png"),
                getClass().getResource("/assets/icons/operation/mapClosedBlack.png"),
                40);
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null && oldTile.getHighlightingTwo() != HighlightingTwo.SELECTED)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if (newTile != null && newTile.getHighlightingTwo() != HighlightingTwo.SELECTED)
        {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if (newTile != null)
        {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
        miniMapDrawer.drawMinimap(tileMap);
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
                    unit.positionProperty().addListener(unitPositionListener);
                }
            }

            if (c.wasRemoved())
            {
                for (Unit unit : c.getRemoved())
                {
                    unit.positionProperty().removeListener(unitPositionListener);
                }
            }
        }
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition)
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
        game = context.getGameState();
        HashMap<String, Player> playerMap=new HashMap<>();
        HashMap<String, Node> playerNodeMap=new HashMap<>();
        HashMap<String, Pane> playerPaneMap=new HashMap<>();
        ArrayList<Pane> playerCardList = new ArrayList<Pane>();

        playerCardList.add(player1);
        playerCardList.add(player2);
        playerCardList.add(player3);
        playerCardList.add(player4);

        playerListController=new PlayerListController(this.game);

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

        playerNodeMap.get(this.game.getCurrentPlayer().getId()).setStyle("-fx-background-color: -selected-background-color");
        this.game.currentPlayerProperty().addListener((observable, oldVal, newVal) -> {
            Player oldPlayer = playerMap.get(oldVal);
            playerNodeMap.get(oldPlayer.getId()).setStyle("-fx-background-color: -root-background-color");
            Player newPlayer = playerMap.get(newVal);
            playerNodeMap.get(newPlayer.getId()).setStyle("-fx-background-color: -selected-background-color");
        });
        this.game.getPlayers().addListener((ListChangeListener) l -> {
            if(!l.next()){
                return;
            }
            List<Player> removedPlayer = l.getRemoved();
            Platform.runLater(()->{
                for(Player player : removedPlayer){
                    playerPaneMap.get(player.getId()).getChildren().remove(0);
                    playerPaneMap.get(player.getId()).getChildren().add(playerListController.createLoserCard(player));
                }
            });
        });


        roundCount=0;
        phaseCount=0;
        this.game.phaseProperty().addListener((observable, oldVal, newVal) -> {
            if(phaseCount==3){
                phaseCount=0;
                roundCount++;
            }
            phaseCount++;
        });
        roundCountLabel.textProperty().bind(new SimpleIntegerProperty(roundCount).asString());
        phaseLabel.textProperty().bind(this.game.phaseProperty());
        //roundTextLabel.textProperty().setValue(getResources().getString("round"));
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

        if (handleMovement(tile)) {
            return;
        }

        onTileSelection(tile);
    }

    private boolean handleMovement(Tile tile) {
        if (!context.isMyTurn()) {
            return false;
        }

        Cell cell = tile.getCell();

        if (cell.unitProperty().get() != null) {
            return false;
        }

        Unit selectedUnit = context.getGameState().getSelectedUnit();
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
        context.getGameEventManager().sendMessage(command);
        context.getGameState().setInitiallyMoved(true);
        selectedUnit.setRemainingMovePoints(
                selectedUnit.getRemainingMovePoints() - tour.getCost()
        );

        return true;
    }

    protected void onTileSelection(Tile tileClicked) {
        if(tileClicked.equals(selectedTile.get())) {
            selectedTile.set(null);
            hoveredTile.set(null);
        }
        else{
            selectedTile.set(tileClicked);
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
                        () -> this.context.getGameEventManager().sendEndPhaseCommand(),
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

    @Override
    public void configure(@Nonnull IngameContext context) {
        this.context = context;

        configureSelectedUnit();

        context.getGameState().currentPlayerProperty().addListener(this::onNextPlayer);
        if (context.getGameState().getCurrentPlayer() != null) {
            onNextPlayer(null, null, context.getGameState().getCurrentPlayer());
        }

        Game gameState = context.getGameState();
        game = gameState;
        if (game == null) {
            // exception
        } else
        {
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
                unit.positionProperty().addListener(unitPositionListener);
            }

            initCanvas();
            initMiniMap();
            initPlayerBar();
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(unitListListener);
        selectedTile.addListener(selectedTileListener);
        hoveredTile.addListener(hoveredTileListener);

        configureEndPhase();
    }

    private void configureEndPhase() {
        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty(false);
        ObjectProperty<Player> currentPlayerProperty = this.context.getGameState().currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> (context.isMyTurn() && this.context.getGameState().getInitiallyMoved()),
                currentPlayerProperty, this.context.getGameState().initiallyMovedProperty()
        ));

        playerCanEndPhase.addListener(((observable, oldValue, newValue) -> {}) );

        currentPlayerProperty.addListener((observable, oldValue, newValue) -> {
            this.context.getGameState().setInitiallyMoved(false);
        });

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

    private void configureSelectedUnit() {
        this.context.getGameState()
            .selectedUnitProperty().bind(Bindings.createObjectBinding(
                () -> {

                    Tile selectedTile = this.selectedTile.get();
                    if(selectedTile == null){
                        return null;
                    }
                    Cell selectedCell = selectedTile.getCell();
                    if (selectedCell.unitProperty() == null){
                        if (game.selectedUnitProperty() != null){
                            game.selectedUnitProperty().get().setSelected(false);
                        }
                        return null;
                    }

                    ReadOnlyObjectProperty<Unit> selectedUnitProperty = selectedCell.unitProperty();
                    Unit selectedUnit = selectedUnitProperty.get();

                    if (selectedUnit != null) {
                        selectedUnit.setSelected(true);
                    }
                    return selectedUnit;
                },
                this.selectedTile
        ));
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
            unit.positionProperty().removeListener(unitPositionListener);

        }

        units.removeListener(unitListListener);
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
}
