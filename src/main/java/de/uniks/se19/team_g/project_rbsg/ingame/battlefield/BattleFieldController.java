package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.alert.*;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.component.*;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.*;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo.*;
import de.uniks.se19.team_g.project_rbsg.ingame.event.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import de.uniks.se19.team_g.project_rbsg.util.*;
import io.rincl.*;
import javafx.application.*;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.Nullable;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.beans.*;
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
    private final ChatBuilder chatBuilder;
    @Nonnull
    final private SimpleObjectProperty<Tile> selectedTile;
    @Nonnull
    final private SimpleObjectProperty<Tile> hoveredTile;
    @Nonnull
    final private SimpleObjectProperty<Unit> hoveredUnit;
    @Nonnull
    private final MovementManager movementManager;
    @Nonnull
    private final MusicManager musicManager;
    public Button leaveButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public Button mapButton;
    public Canvas miniMapCanvas;
    public Button endPhaseButton;
    public Pane endPhaseButtonContainer;
    public VBox root;
    public VBox unitInformationContainer;
    public Button actionButton;
    public Button Cancel;
    public Button playerButton;
    public Button chatButton;
    public Button musicButton;
    public StackPane battlefieldStackPane;
    public AnchorPane overlayAnchorPane;
    public StackPane miniMapStackPane;

    public StackPane chatPane;
    public Button ingameInformationsButton;
    public HBox playerBar;
    public Pane player1;
    public Pane player2;
    public Pane player3;
    public Pane player4;
    public Label roundTextLabel;
    public Label roundCountLabel;
    public Label phaseLabel;
    public ImageView phaseImage;
    public HBox ingameInformationHBox;

    private UnitInfoBoxBuilder unitInfoBoxBuilder;
    private Game game;
    private ObservableList<Cell> cells;
    private ObservableList<Unit> units;

    private TileDrawer tileDrawer;
    private Tile[][] tileMap;
    private ZoomableScrollPane zoomableScrollPane;
    private Canvas canvas;

    private final PropertyChangeListener highlightingListener = this::highlightingChanged;
    private final ChangeListener<Tile> hoveredTileListener = this::hoveredTileChanged;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    private final ChangeListener<Tile> selectedTileListener = this::selectedTileChanged;
    private final ListChangeListener<Player> playerListListener = this::playerListChanged;


    private final ChangeListener<Number> cameraViewChangedListener = this::cameraViewChanged;
    private int mapSize;
    private int zoomFactor = 1;
    private Camera camera;
    private ChatController chatController;

    private IngameContext context;

    private PlayerListController playerListController;
    private final HashMap<String, Player> playerMap = new HashMap<>();
    private final HashMap<String, Node> playerNodeMap = new HashMap<>();
    private final HashMap<String, Pane> playerPaneMap = new HashMap<>();
    private final ArrayList<Pane> playerCardList = new ArrayList<>();
    private SimpleIntegerProperty roundCount;
    private int roundCounter;

    @Autowired
    public BattleFieldController(
            @NonNull final SceneManager sceneManager,
            @NonNull final AlertBuilder alertBuilder,
            @Nonnull final MovementManager movementManager,
            @NonNull final ChatBuilder chatBuilder,
            @NonNull final ChatController chatController,
            @Nonnull final MusicManager musicManager
    )
    {
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.movementManager = movementManager;
        this.musicManager = musicManager;
        this.tileDrawer = new TileDrawer();
        this.miniMapDrawer = new MiniMapDrawer();
        this.selectedTile = new SimpleObjectProperty<>(null);
        this.hoveredTile = new SimpleObjectProperty<>(null);

        this.hoveredUnit = new SimpleObjectProperty<>(null);
        this.unitInfoBoxBuilder = new UnitInfoBoxBuilder();

        this.chatBuilder = chatBuilder;
        this.chatController = chatController;
        this.roundCount = new SimpleIntegerProperty();
        this.roundCounter = 1;

    }

    public SimpleObjectProperty<Tile> selectedTileProperty()
    {
        return selectedTile;
    }

    public Tile getSelectedTile()
    {
        return selectedTile.get();
    }

    public void setSelectedTile(@Nullable Tile selectedTile)
    {
        this.selectedTile.set(selectedTile);
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
        JavaFXUtils.setButtonIcons(
                chatButton,
                getClass().getResource("/assets/icons/operation/chatBubbleWhite.png"),
                getClass().getResource("/assets/icons/operation/chatBubbleBlack.png"),
                40
        );

        musicManager.initButtonIcons(musicButton);
    }

    private void highlightingChanged(PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    private void hoveredTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null && oldTile.getHighlightingTwo() == HighlightingTwo.HOVERED)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if (newTile != null && newTile.getHighlightingTwo() == HighlightingTwo.NONE)
        {
            newTile.setHighlightingTwo(HighlightingTwo.HOVERED);
        }

        if (newTile != null && newTile.getCell().getUnit() != null)
        {
            hoveredUnit.set(newTile.getCell().getUnit());
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void selectedTileChanged(ObservableValue<? extends Tile> observableValue, Tile oldTile, Tile newTile)
    {
        if (oldTile != null)
        {
            oldTile.setHighlightingTwo(HighlightingTwo.NONE);
        }

        if ((newTile != null) && (newTile.getCell().getUnit() != null) && (isMyUnit(newTile.getCell().getUnit())))
        {
            newTile.setHighlightingTwo(HighlightingTwo.SELECETD_WITH_UNITS);
        }
        else if (newTile != null)
        {
            newTile.setHighlightingTwo(HighlightingTwo.SELECTED);
        }
        miniMapDrawer.drawMinimap(tileMap);
    }

    private boolean isMyUnit(@NonNull Unit unit)
    {
        if (unit.getLeader() == null)
        {
            return false;
        }
        if (unit.getLeader().getName().equals(context.getUser().getName()))
        {
            return true;
        }
        else
        {
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
        canvas.setHeight(CELL_SIZE * mapSize);
        canvas.setWidth(CELL_SIZE * mapSize);
        battlefieldStackPane.getChildren().add(0, zoomableScrollPane);
        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
    }

    private void initPlayerBar()
    {


        playerCardList.add(player1);
        playerCardList.add(player2);
        playerCardList.add(player3);
        playerCardList.add(player4);

        playerListController = new PlayerListController(this.game);
        playerBar.setAlignment(Pos.TOP_CENTER);

        int counter = 0;
        if (this.game.getPlayers().size() == 2)
        {
            playerBar.getChildren().remove(player1);
            playerCardList.remove(player1);
            playerBar.getChildren().remove(player4);
            playerCardList.remove(player4);
        }

        for (Player player : this.game.getPlayers())
        {

            playerCardList.get(counter).getChildren().add(playerListController.getPlayerCards().get(counter));
            playerPaneMap.put(player.getId(), playerCardList.get(counter));
            playerMap.put(player.getId(), player);
            playerNodeMap.put(player.getId(), playerListController.getPlayerCards().get(counter));

            counter++;
        }

        playerListController = new PlayerListController(game);
        playerBar.setVisible(false);
        playerBar.setPickOnBounds(false);
        battlefieldStackPane.setPickOnBounds(false);
        miniMapStackPane.setPickOnBounds(false);

        if (!playerNodeMap.isEmpty() && this.game.getCurrentPlayer() != null)
        {
            playerNodeMap.get(this.game.getCurrentPlayer().getId()).setStyle("-fx-background-color: -selected-background-color");
        }
        this.game.currentPlayerProperty().addListener((observable, oldPlayer, newPlayer) -> {
            if (oldPlayer != null)
            {
                playerNodeMap.get(oldPlayer.getId()).setStyle("-fx-background-color: -root-background-color");
            }
            if (newPlayer != null)
            {
                playerNodeMap.get(newPlayer.getId()).setStyle("-fx-background-color: -selected-background-color");
            }
        });

        this.game.getPlayers().addListener(playerListListener);

//                (ListChangeListener<Player>) l -> {
//        }
        this.game.phaseProperty().addListener((observable, oldVal, newVal) -> {

            if (oldVal == null)
            {
                return;
            }
            if (oldVal.equals("lastMovePhase") && (roundCounter % this.game.getPlayers().size()) == 0)
            {
                Platform.runLater(() -> {
                    roundCount.set(roundCount.get() + 1);
                });
                roundCounter = 0;
            }
            roundCounter++;
        });

        this.game.phaseProperty().addListener(((observable, oldValue, newValue) -> {
            switch (newValue)
            {
                case "movePhase":
                {
                    Image image = new Image(getClass().getResource("/assets/icons/operation/footstepsWhite.png").toExternalForm());
                    phaseImage.imageProperty().setValue(image);
                }
                break;
                case "attackPhase":
                {
                    Image image = new Image(getClass().getResource("/assets/icons/operation/swordClashWhite.png").toExternalForm());
                    phaseImage.imageProperty().setValue(image);
                }
                break;
                case "lastMovePhase":
                {
                    Image image = new Image(getClass().getResource("/assets/icons/operation/footprintWhite.png").toExternalForm());
                    phaseImage.imageProperty().setValue(image);
                }
                break;
            }
        }));

        roundCount.set(0);
        roundCountLabel.textProperty().bind(roundCount.asString());
        phaseLabel.setText("Phase");
        ingameInformationHBox.setStyle("-fx-background-color: -surface-elevation-8-color");
        //ingameInformationHBox.setSpacing(10);
        HBox.setMargin(ingameInformationHBox, new Insets(10, 10, 10, 10));
        //phaseLabel.textProperty().bind(this.game.phaseProperty());
        roundTextLabel.textProperty().setValue("Round");
    }

    private void playerListChanged(ListChangeListener.Change<? extends Player> c)
    {
            while (c.next())
            {
                if (c.wasRemoved())
                {
                    for (Player player : c.getRemoved())
                    {
                        Platform.runLater(() -> {
                            playerPaneMap.get(player.getId()).getChildren().remove(0);
                            playerPaneMap.get(player.getId()).getChildren().add(playerListController.createLoserCard(player));
                        });
                    }
                }
            }
    }

    protected Tile resolveTargetTile(MouseEvent event)
    {
        int xPos = (int) (event.getX() / CELL_SIZE);
        int yPos = (int) (event.getY() / CELL_SIZE);

        return tileMap[yPos][xPos];
    }

    public void canvasHandleMouseMove(MouseEvent event)
    {
        Tile tile = resolveTargetTile(event);
        hoveredTile.set(tile);
    }

    public void canvasHandleMouseClicked(MouseEvent event)
    {
        Tile tile = resolveTargetTile(event);

        if (tile == null)
        {
            return;
        }

        if (handleAttack(tile))
        {
            return;
        }

        if (handleMovement(tile))
        {
            setSelectedTile(null);
            this.context.getGameState().setSelectedUnit(null);
            return;
        }

        if (tile.getCell().getUnit() != null)
        {
            Unit unit = tile.getCell().getUnit();
            onUnitSelection(unit, tile);
        }
        else
        {
            onTileSelection(tile);
        }
    }

    private boolean handleAttack(Tile tile)
    {

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
        )
        {
            return false;
        }

        context.getGameEventManager().api().attack(selectedUnit, targetUnit);
        game.setSelectedUnit(null);
        setSelectedTile(null);

        return true;
    }

    private void onUnitSelection(Unit unitClicked, Tile tileClicked)
    {
        if (unitClicked.equals(game.getSelectedUnit()))
        {
            game.setSelectedUnit(null);
            selectedTile.set(null);
            hoveredTile.set(null);
            setCellProperty(null);
        }
        else
        {
            game.setSelectedUnit(unitClicked);
            selectedTile.set(tileClicked);
        }
    }

    private boolean handleMovement(Tile tile)
    {
        if (!context.isMyTurn())
        {
            return false;
        }
        Game game = context.getGameState();

        if (
                !game.isPhase(Game.Phase.movePhase)
                        && !game.isPhase(Game.Phase.lastMovePhase)
        )
        {
            return false;
        }

        Cell cell = tile.getCell();

        if (cell.unitProperty().get() != null)
        {
            return false;
        }

        Unit selectedUnit = game.getSelectedUnit();
        if (selectedUnit == null)
        {
            return false;
        }

        if (selectedUnit.getLeader() != context.getUserPlayer())
        {
            return false;
        }

        Tour tour = movementManager.getTour(selectedUnit, cell);
        if (tour == null)
        {
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

    protected void onTileSelection(Tile tileClicked)
    {
        if (tileClicked.equals(selectedTile.get()))
        {
            selectedTile.set(null);
            hoveredTile.set(null);
        }
        else
        {
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

    private void doEndPhase()
    {
        this.context.getGameEventManager().sendEndPhaseCommand();
        this.context.getGameState().setSelectedUnit(null);
        setSelectedTile(null);
    }

    @Override
    public void configure(@Nonnull IngameContext context)
    {
        this.context = context;

        context.getGameState().currentPlayerProperty().addListener(this::onNextPlayer);
        if (context.getGameState().getCurrentPlayer() != null)
        {
            onNextPlayer(null, null, context.getGameState().getCurrentPlayer());
        }

        game = context.getGameState();
        if (game != null)
        {
            cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells)
            {
                tileMap[cell.getY()][cell.getX()] = new Tile(cell);
                tileMap[cell.getY()][cell.getX()].addListener(highlightingListener);
            }

            for (Unit unit : units)
            {
                //Adds listener for units which are already in the list
                unit.positionProperty().addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
            }

            initCanvas();
            camera = new Camera(zoomableScrollPane.scaleValueProperty(), zoomableScrollPane.hvalueProperty(),
                                zoomableScrollPane.vvalueProperty(), mapSize, zoomableScrollPane.heightProperty(),
                                zoomableScrollPane.widthProperty());
            initMiniMap();
            miniMapDrawer.setCamera(camera);
        }
        else
        {
            // exception
        }

        initPlayerBar();


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


        unitInformationContainer.getChildren().add(unitInfoBoxBuilder.build(game.selectedUnitProperty()));
        unitInformationContainer.getChildren().add(unitInfoBoxBuilder.build(hoveredUnit));

        try
        {
            initChat();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void initChat() throws Exception
    {
        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(context.getGameEventManager());
        chatPane.getChildren().add(chatComponents.getRoot());
        chatController = chatComponents.getController();
        chatPane.setVisible(false);
    }

    public void openChat()
    {

        if (chatPane.isVisible())
        {
            chatPane.setVisible(false);
            JavaFXUtils.setButtonIcons(
                    chatButton,
                    getClass().getResource("/assets/icons/operation/chatBubbleWhite.png"),
                    getClass().getResource("/assets/icons/operation/chatBubbleBlack.png"),
                    40
            );
        }
        else
        {
            chatPane.setVisible(true);
            JavaFXUtils.setButtonIcons(
                    chatButton,
                    getClass().getResource("/assets/icons/operation/chatWhite.png"),
                    getClass().getResource("/assets/icons/operation/chatBlack.png"),
                    40
            );
        }
    }

    private void configureCells()
    {
        for (Cell cell : this.context.getGameState().getCells())
        {
            cell.isReachableProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue)
                {
                    cell.getTile().setHighlightingOne(HighlightingOne.MOVE);
                }
                else
                {
                    cell.getTile().setHighlightingOne(HighlightingOne.NONE);
                }
            }));

            cell.isAttackableProperty().addListener(((observable, oldValue, newValue) -> {
                if (newValue)
                {
                    cell.getTile().setHighlightingOne(HighlightingOne.ATTACK);
                    setUnitAttackProperty(cell);
                }
                else
                {
                    cell.getTile().setHighlightingOne(HighlightingOne.NONE);
                    removeUnitAttackProperty(cell);
                }
            }));
        }
    }

    private void removeUnitAttackProperty(@NonNull Cell cell)
    {
        if (cell.getUnit() == null)
        {
            return;
        }
        Unit unit = cell.getUnit();
        if (isMyUnit(unit))
        {
            return;
        }
        unit.setAttackable(false);
    }

    private void setUnitAttackProperty(@NonNull Cell cell)
    {
        if (cell.getUnit() == null)
        {
            return;
        }
        Unit unit = cell.getUnit();
        if (isMyUnit(unit))
        {
            return;
        }
        unit.setAttackable(true);
    }

    private void setCellProperty(@Nullable Unit selectedUnit)
    {
        if (selectedUnit == null)
        {
            for (Cell cell : this.context.getGameState().getCells())
            {
                cell.setIsReachable(false);
                cell.setIsAttackable(false);
                removeUnitAttackProperty(cell);
            }
            return;
        }

        if ((!isMyUnit(selectedUnit)) || (!this.context.isMyTurn()))
        {
            return;
        }
        if (this.context.getGameState().isPhase(Game.Phase.attackPhase))
        {
            setAttackRadius(selectedUnit);
        }
        else
        {
            setMoveRadius(selectedUnit);
        }
        selectedUnit.getPosition().getTile().setHighlightingOne(HighlightingOne.NONE);
    }

    private void setMoveRadius(@NonNull Unit selectedUnit)
    {
        for (Cell cell : this.context.getGameState().getCells())
        {
            if (this.movementManager.getTour(selectedUnit, cell) != null)
            {
                cell.setIsReachable(true);
            }
            else
            {
                cell.setIsReachable(false);
            }
        }
    }


    private void setAttackRadius(@NonNull Unit selectedUnit)
    {
        Cell position = selectedUnit.getPosition();
        for (Cell cell : this.context.getGameState().getCells())
        {
            cell.setIsAttackable(false);
        }
        if (position.getLeft() != null)
        {
            position.getLeft().setIsAttackable(true);
        }
        if (position.getTop() != null)
        {
            position.getTop().setIsAttackable(true);
        }
        if (position.getRight() != null)
        {
            position.getRight().setIsAttackable(true);
        }
        if (position.getBottom() != null)
        {
            position.getBottom().setIsAttackable(true);
        }
    }

    private void miniMapHandleMouseClick(MouseEvent mouseEvent)
    {
        int xPos = miniMapDrawer.getXPostionOnMap(mouseEvent.getX());
        int yPos = miniMapDrawer.getYPostionOnMap(mouseEvent.getY());
        camera.TryToCenterToPostition(xPos, yPos);
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void configureEndPhase()
    {
        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty(false);
        ObjectProperty<Player> currentPlayerProperty = this.context.getGameState().currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> (context.isMyTurn() && this.context.getGameState().getInitiallyMoved()),
                currentPlayerProperty, this.context.getGameState().initiallyMovedProperty()
        ));

        playerCanEndPhase.addListener(((observable, oldValue, newValue) -> {
        }));

        currentPlayerProperty.addListener((observable, oldValue, newValue) -> this.context.getGameState().setInitiallyMoved(false));

        endPhaseButton.disableProperty().bind(playerCanEndPhase.not());

        endPhaseButton.disableProperty().addListener(((observable, oldValue, newValue) -> {
        }));
    }

    private void onNextPlayer(Observable observable, Player lastPlayer, Player nextPlayer)
    {
        if (context.isMyTurn())
        {
            onBeforeUserTurn();
        }
    }

    private void onBeforeUserTurn()
    {
        for (Unit unit : context.getUserPlayer().getUnits())
        {
            unit.setRemainingMovePoints(unit.getMp());
        }
    }

    private void initMiniMap()
    {
        miniMapDrawer.setCanvas(miniMapCanvas, mapSize);
        miniMapDrawer.setCamera(camera);
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

        game.getPlayers().removeListener(playerListListener);
    }

    public void openPlayerBar(@Nonnull final ActionEvent event)
    {
        if (!playerBar.visibleProperty().get())
        {
            playerBar.visibleProperty().setValue(true);
            playerBar.toFront();
        }
        else
        {
            playerBar.visibleProperty().setValue(false);
            playerBar.toBack();
        }
    }

    public void toggleMusic()
    {
        musicManager.toggleMusicAndUpdateButtonIconSet(musicButton);
    }
}
