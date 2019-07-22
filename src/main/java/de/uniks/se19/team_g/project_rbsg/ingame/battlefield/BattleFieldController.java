package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
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
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.util.Map;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable
{

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH/2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60)/2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);

    private static final String MOVE_PHASE = "movePhase";
    private static final String LAST_MOVE_PHASE = "lastMovePhase";

    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;

    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;

    public VBox root;

    private Canvas canvas;
    private ZoomableScrollPane zoomableScrollPane;
    private int mapSize;

    private Game game;
    private ObservableList<Cell> cells;

    private Tile[][] tileMap;
    private ObservableList<Unit> units;

    private int zoomFactor = 1;

    private TileDrawer tileDrawer;

    @Nonnull
    public Tile getSelectedTile() {
        return selectedTile.get();
    }

    @Nonnull
    public SimpleObjectProperty<Tile> selectedTileProperty() {
        return selectedTile;
    }

    public void setSelectedTile(@Nonnull Tile selectedTile) {
        this.selectedTile.set(selectedTile);
    }

    @Nonnull
    final private SimpleObjectProperty<Tile> selectedTile;
    @Nonnull
    final private SimpleObjectProperty<Tile> hoveredTile;


    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    @Nonnull
    private final MovementManager movementManager;

    private IngameContext context;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);
    }

    public void initialize() {
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
                endPhaseButton,
                getClass().getResource("/assets/icons/operation/endPhaseWhite.png"),
                getClass().getResource("/assets/icons/operation/endPhaseBlack.png"),
                40
        );
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
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile) {
        if (oldTile != null) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if ((newTile != null) && (newTile.getCell().getUnit() != null) && (isMyUnit(newTile.getCell().getUnit()))){
            newTile.setHighlightingTwo(HighlightingTwo.SELECETD_WITH_UNITS);
        } else if(newTile != null) {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
            this.context.getGameState().setSelectedUnit(null);
        }

    }

    private boolean isMyUnit(@NonNull Unit unit){
        if (unit.getLeader().getName().equals(context.getUser().getName())){
            return true;
        } else {
            return false;
        }
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        if (c.next()) {
            logger.debug(c.toString());
            if (c.wasAdded()) {
                for (int i = c.getFrom(); i < c.getTo(); i++)
                {
                    units.get(c.getFrom()).positionProperty()
                            .addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, units.get(c.getFrom())));
                }
            }

            if(c.wasRemoved()) {
                for (Unit unit : c.getRemoved())
                {
                    unit.positionProperty()
                            .removeListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, units.get(c.getFrom())));
                }
            }
        }
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition, Unit unit)
    {
        if(lastPosition != null) {
            tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        }
        if(newPosition != null) {
            tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
        }
        game.setSelectedUnit(unit);
        unit.getRemainingMovePoints();// hack
        setCellReachability(unit);
    }

    private void initCanvas() {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        root.getChildren().add(zoomableScrollPane);
        canvas.setHeight(CELL_SIZE*mapSize);
        canvas.setWidth(CELL_SIZE*mapSize);

        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
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
            this.setSelectedTile(tile); //unsicher
            return;
        }

        if (tile.getCell().getUnit() != null){
            Unit unit = tile.getCell().getUnit();
            onUnitSelection(unit, tile);
        } else {
            onTileSelection(tile);
        }
    }

    private void onUnitSelection(Unit unitClicked, Tile tileClicked){
        if(unitClicked.equals(game.getSelectedUnit())){
            game.setSelectedUnit(null);
            selectedTile.set(null);
            hoveredTile.set(null);
            setCellReachability(null);
        } else {
            game.setSelectedUnit(unitClicked);
            selectedTile.set(tileClicked);
            setCellReachability(unitClicked);
        }
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

    public void leaveGame(ActionEvent actionEvent) {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::doLeaveGame,
                        null);
    }

    private void doLeaveGame() {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

    public void zoomIn(ActionEvent actionEvent) {
        if(zoomFactor == 1) {
            zoomableScrollPane.onScroll(20.0, ZOOMPANE_CENTER);
            zoomFactor++;
        } else if(zoomFactor == 0) {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        } else if(zoomFactor == -1 && context.getGameData().getNeededPlayer() == 4) {
            zoomableScrollPane.onScroll(7.5, ZOOMPANE_CENTER);
            zoomFactor++;
        }
    }

    public void zoomOut(ActionEvent actionEvent) {
        if(zoomFactor == 2) {
            zoomableScrollPane.onScroll(-20.0, ZOOMPANE_CENTER);
            zoomFactor--;
        } else if(zoomFactor == 1) {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        } else if(zoomFactor == 0 && context.getGameData().getNeededPlayer() == 4) {
            zoomableScrollPane.onScroll(-7.5, ZOOMPANE_CENTER);
            zoomFactor--;
        }
    }

    public void endPhase() {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_PHASE,
                        () -> this.context.getGameEventManager().sendEndPhaseCommand(),
                        null);
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
        if (game == null) {
            // exception
        } else {
            cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells) {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell);
                tileMap[cell.getY()][cell.getX()].addListener(this::highlightingChanged);
            }

            for (Unit unit : units) {
                //Adds listener for units which are already in the list
                unit.positionProperty().addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                unit.remainingMovePointsProperty().addListener(((observable, oldValue, newValue) -> setCellReachability(unit)));
            }

            initCanvas();
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(this::unitListChanged);
        selectedTile.addListener(this::selectedTileChanged);
        hoveredTile.addListener(this::hoveredTileChanged);

        this.context.getGameState().selectedUnitProperty()
                .addListener((observable, oldUnit, newUnit) -> setCellReachability(newUnit));

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
        }
    }

    private void setCellReachability(@Nullable Unit selectedUnit){
        if(selectedUnit == null){
            for(Cell cell: this.context.getGameState().getCells()) {
                cell.setIsReachable(false);
            }
            return;
        }

        if (!isMyUnit(selectedUnit)) {
            return;
        }

        if (! (this.context.getGameState().getPhase().equals(MOVE_PHASE)
                || this.context.getGameState().getPhase().equals(LAST_MOVE_PHASE))){
            return;
        }

        for(Cell cell: this.context.getGameState().getCells()) {
            if (this.movementManager.getTour(selectedUnit, cell) != null){
                cell.setIsReachable(true);
            } else{
                cell.setIsReachable(false);
            }
        }
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

    @Override
    public void terminate()
    {
        selectedTile.removeListener(this::selectedTileChanged);
        hoveredTile.removeListener(this::hoveredTileChanged);
        for (Tile[] tileArray : tileMap)
        {
            for (Tile tile : tileArray)
            {
                tile.removeListener(this::highlightingChanged);
            }
        }

        for (Unit unit : units)
        {
            unit.positionProperty()
                    .removeListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
        }
    }
}
