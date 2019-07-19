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
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincled;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.beans.PropertyChangeEvent;
import java.util.Map;

import java.beans.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author  Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable, Rincled
{

    private static final double CELL_SIZE = 64;
    private static final int ZOOMPANE_WIDTH_CENTER = ProjectRbsgFXApplication.WIDTH/2;
    private static final int ZOOMPANE_HEIGHT_CENTER = (ProjectRbsgFXApplication.HEIGHT - 60)/2;
    private static final Point2D ZOOMPANE_CENTER = new Point2D(ZOOMPANE_WIDTH_CENTER, ZOOMPANE_HEIGHT_CENTER);

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
    final private SimpleObjectProperty<Tile> selectedTile;
    @Nonnull
    final private SimpleObjectProperty<Tile> hoveredTile;

    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
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
    public StackPane ingameField;
    @FXML
    public Label roundTextLabel;
    @FXML
    public Label roundCountLabel;
    @FXML
    public Label phaseLabel;

    private PlayerListController playerListController;

    private final Logger logger = LoggerFactory.getLogger(getClass());

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
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile != null && oldTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if(newTile != null && newTile.getHighlightingTwo() != HighlightingTwo.SELECTED) {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if(oldTile != null) {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if(newTile != null) {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
    }

    private void unitListChanged(ListChangeListener.Change<? extends Unit> c)
    {
        if (c.next()) {
            logger.debug(c.toString());
            if (c.wasAdded()) {
                for (int i = c.getFrom(); i < c.getTo(); i++)
                {
                    units.get(c.getFrom()).positionProperty().addListener(this::unitChangedPosition);
                }
            }

            if(c.wasRemoved()) {
                for (Unit unit : c.getRemoved())
                {
                    unit.positionProperty().removeListener(this::unitChangedPosition);
                }
            }
        }
    }


    private void unitChangedPosition(ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition)
    {
        if(lastPosition != null) {
            tileDrawer.drawTile(tileMap[lastPosition.getY()][lastPosition.getX()]);
        }
        if(newPosition != null) {
            tileDrawer.drawTile(tileMap[newPosition.getY()][newPosition.getX()]);
        }
    }

    private void initCanvas() {
        canvas = new Canvas();
        canvas.setId("canvas");
        zoomableScrollPane = new ZoomableScrollPane(canvas);
        ingameField.getChildren().add(zoomableScrollPane);
        canvas.setHeight(CELL_SIZE*mapSize);
        canvas.setWidth(CELL_SIZE*mapSize);

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
        ingameField.setPickOnBounds(false);

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

        configureSelectedUnit();

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
                unit.positionProperty().addListener(this::unitChangedPosition);
            }

            initCanvas();
            initPlayerBar();
        }

        //Add Event handler for actions on canvas
        canvas.addEventHandler(MouseEvent.MOUSE_MOVED, this::canvasHandleMouseMove);
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::canvasHandleMouseClicked);

        //Listener for unit list
        units.addListener(this::unitListChanged);
        selectedTile.addListener(this::selectedTileChanged);
        hoveredTile.addListener(this::hoveredTileChanged);

        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty();

        ObjectProperty<Player> currentPlayerProperty = gameState.currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> {
                    boolean active = context.getUser().getName().equals(currentPlayerProperty.getName());

                    return (active && gameState.initiallyMovedProperty().get());
                },
                currentPlayerProperty, gameState.initiallyMovedProperty()
        ));

        currentPlayerProperty.addListener((observable, oldValue, newValue) -> {
            gameState.setInitiallyMoved(false);
        });

        endPhaseButton.disableProperty().bind(playerCanEndPhase.not());
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
            unit.positionProperty().removeListener(this::unitChangedPosition);
        }
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
