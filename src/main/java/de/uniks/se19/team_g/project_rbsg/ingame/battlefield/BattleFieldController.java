package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import animatefx.animation.Bounce;
import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatChannelController;
import de.uniks.se19.team_g.project_rbsg.component.ZoomableScrollPane;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.PlayerListController;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animations.AttackAnimationManager;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animations.DeathAnimationManager;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animations.MovementAnimationManager;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.animations.RevivalAnimationManager;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.history.HistoryViewProvider;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.uiModel.Tile;
import de.uniks.se19.team_g.project_rbsg.ingame.battlefield.unitInfo.UnitInfoBoxBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.state.History;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.Entry;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.scene.*;
import de.uniks.se19.team_g.project_rbsg.skynet.Skynet;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.MovementAction;
import de.uniks.se19.team_g.project_rbsg.skynet.action.PassAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.SurrenderBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack.AttackBehaviour;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement.MovementBehaviour;
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
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
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

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.LOBBY;
import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.LOGIN;

/**
 * @author Keanu Stückrad
 */
@Scope("prototype")
@Controller
public class BattleFieldController implements RootController, IngameViewController, Terminable, Rincled
{

    private static final double CELL_SIZE = 64;
    private final ExceptionHandler exceptionHandler;
    private final SceneManager sceneManager;
    private final AlertBuilder alertBuilder;
    private final MenuBuilder menuBuilder;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final MiniMapDrawer miniMapDrawer;
    private final ChatBuilder chatBuilder;
    @Nonnull
    private final MovementManager movementManager;
    private final HashMap<String, Player> playerMap = new HashMap<>();
    private final HashMap<String, Node> playerNodeMap = new HashMap<>();
    private final HashMap<String, Pane> playerPaneMap = new HashMap<>();
    private final ArrayList<Pane> playerCardList = new ArrayList<>();
    private final Property<Locale> selectedLocale;
    private final GameProvider gameProvider;
    public Button menuButton;
    public Button hpBarButton;
    public Button zoomOutButton;
    public Button zoomInButton;
    public Canvas miniMapCanvas;
    public Button endPhaseButton;
    public Button endRoundButton;
    public Pane endPhaseButtonContainer;
    public VBox root;
    public VBox unitInformationContainer;
    //TODO: readd
//    public Button actionButton;
//    public Button cancelButton;
    public Button skynetTurnButton;
    //public Button playerButton;
    public Button chatButton;
    public StackPane battlefieldStackPane;
    public AnchorPane overlayAnchorPane;
    public StackPane miniMapStackPane;
    public StackPane chatPane;
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
    public StackPane rootPane;
    public VBox history;
    public Button skynetButton;
    public Label gameName;
    public ColumnConstraints player;
    public Button button;
    public Button animationButton;
    private ChatController chatController;
    private Game game;
    private ObservableList<Unit> units;
    private TileDrawer tileDrawer;
    private final PropertyChangeListener highlightingListener = this::highlightingChanged;
    private Tile[][] tileMap;
    private final ChangeListener<Hoverable> onHoveredChanged = this::onHoveredChanged;
    private final ListChangeListener<Unit> unitListListener = this::unitListChanged;
    private final ChangeListener<Number> cameraViewChangedListener = this::cameraViewChanged;
    private final ChangeListener<Number> stageSizeListener = this::stageSizeChanged;
    private final ChangeListener<Number> disableOverlaysListener = this::disableOverlaysChanged;
    private ZoomableScrollPane zoomableScrollPane;
    private Canvas canvas;
    private int mapSize;
    private Camera camera;
    private IngameContext context;
    private final ChangeListener<Cell> onSelectedUnitMoved = this::onSelectedUnitMoved;
    private final ChangeListener<Selectable> onSelectedChanged = this::onSelectedChanged;
    private PlayerListController playerListController;
    private final ListChangeListener<Player> playerListListener = this::playerListChanged;
    private SimpleIntegerProperty roundCount;
    private int playerCounter;
    private final ChangeListener<String> phaseChangedListener = this::phaseChanged;
    private Skynet skynet;
    private ActionExecutor actionExecutor;
    private boolean openWhenResizedPlayer, openWhenResizedChat;
    private HistoryViewProvider historyViewProvider;

    private MovementAnimationManager movementAnimationManager;
    private DeathAnimationManager deathAnimationManager;
    private AttackAnimationManager attackAnimationManager;
    private RevivalAnimationManager revivalAnimationManager;

    private final BooleanProperty animationsAllowed;

    private final Button fullscreenButton = new Button();
    private BooleanProperty isUsersTurn;
    private SimpleBooleanProperty skynetRunningProperty;
    private SimpleBooleanProperty historyIsTailProperty = new SimpleBooleanProperty(true);
    private final MusicManager musicManager;

    private Node phaseLabelView;

    private Point2D center = new Point2D(100, 100);
    public ColumnConstraints columnPlayer;

    @Autowired
    public BattleFieldController (
            @Nonnull final SceneManager sceneManager,
            @Nonnull final AlertBuilder alertBuilder,
            @NonNull final MenuBuilder menuBuilder,
            @Nonnull final MovementManager movementManager,
            @Nonnull final ChatBuilder chatBuilder,
            @Nonnull final ChatController chatController,
            @NonNull final GameProvider gameProvider,
            @Nonnull Property<Locale> selectedLocale,
            @NonNull MusicManager musicManager
    ) {
        this.gameProvider = gameProvider;
        this.sceneManager = sceneManager;
        this.alertBuilder = alertBuilder;
        this.menuBuilder = menuBuilder;
        this.movementManager = movementManager;
        this.tileDrawer = new TileDrawer();
        this.miniMapDrawer = new MiniMapDrawer();

        this.chatBuilder = chatBuilder;
        this.chatController = chatController;

        this.roundCount = new SimpleIntegerProperty();
        //this.roundCounter = 1;
        this.playerCounter = 0;

        this.selectedLocale = selectedLocale;
        this.musicManager = musicManager;

        movementAnimationManager = new MovementAnimationManager();
        deathAnimationManager = new DeathAnimationManager();
        revivalAnimationManager = new RevivalAnimationManager();
        attackAnimationManager = new AttackAnimationManager();

        animationsAllowed = new SimpleBooleanProperty(true);



        exceptionHandler = new WebSocketExceptionHandler(alertBuilder)
                .onRetry(this::doLeaveGame)
                .onCancel(() -> sceneManager.setScene(SceneConfiguration.of(LOGIN)));
    }

    public void initialize ()
    {
        gameName.textProperty().setValue(gameProvider.get().getName());
        JavaFXUtils.setButtonIcons(
                menuButton,
                getClass().getResource("/assets/icons/navigation/menuWhite.png"),
                getClass().getResource("/assets/icons/navigation/menuBlack.png"),
                50
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
        JavaFXUtils.setButtonIcons(
                chatButton,
                getClass().getResource("/assets/icons/operation/chatBubbleWhite.png"),
                getClass().getResource("/assets/icons/operation/chatBubbleBlack.png"),
                40
        );

        JavaFXUtils.setButtonIcons(
                hpBarButton,
                getClass().getResource("/assets/icons/navigation/lifeBarWhite.png"),
                getClass().getResource("/assets/icons/navigation/lifeBarBlack.png"),
                40
        );
        JavaFXUtils.setButtonIcons(
                endRoundButton,
                getClass().getResource("/assets/icons/operation/endRoundWhite.png"),
                getClass().getResource("/assets/icons/operation/endRoundBlack.png"),
                40
        );

        JavaFXUtils.setButtonIcons(
                animationButton,
                getClass().getResource("/assets/icons/operation/animation_white.png"),
                getClass().getResource("/assets/icons/operation/animation_black.png"),
                40
        );


        menuButton.setTooltip(new Tooltip("ESC/F10"));
        //TODO readd
//        JavaFXUtils.setButtonIcons(
//                cancelButton,
//                getClass().getResource("/assets/icons/navigation/crossWhite.png"),
//                getClass().getResource("/assets/icons/navigation/crossBlack.png"),
//                40
//        );
//
//
//        cancelButton.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "cancel"));
    }


    private void setFullscreenButton() {
        if(!sceneManager.isFullscreen()) {
            JavaFXUtils.setButtonIcons(
                    fullscreenButton,
                    getClass().getResource("/assets/icons/navigation/fullscreenExitWhite.png"),
                    getClass().getResource("/assets/icons/navigation/fullscreenExitBlack.png"),
                    40
            );
        } else {
            JavaFXUtils.setButtonIcons(
                    fullscreenButton,
                    getClass().getResource("/assets/icons/navigation/fullscreenWhite.png"),
                    getClass().getResource("/assets/icons/navigation/fullscreenBlack.png"),
                    40
            );
        }
    }

    private void initListenersForFullscreen() {
        setCenter();
        sceneManager.getStageHeightProperty().addListener(stageSizeListener);
        sceneManager.getStageHeightProperty().addListener(cameraViewChangedListener);
        sceneManager.getStageWidhtProperty().addListener(stageSizeListener);
        sceneManager.getStageWidhtProperty().addListener(disableOverlaysListener);
        sceneManager.getStageWidhtProperty().addListener(cameraViewChangedListener);
        openWhenResizedPlayer = false;
        openWhenResizedChat = false;
        zoomInButton.disableProperty().bindBidirectional(zoomableScrollPane.getDisablePlusZoom());
        zoomOutButton.disableProperty().bindBidirectional(zoomableScrollPane.getDisableMinusZoom());
        setFullscreen(null);
    }

    private void disableOverlaysChanged (
            @SuppressWarnings("unused") ObservableValue<? extends Number> observableValue,
            @SuppressWarnings("unused") Number oldVal,
            Number newVal
    )
    {
        if ((double) newVal < 1250)
        {
            if (playerBar.visibleProperty().get())
            {
                //openPlayerBar(null);
                setVisibilityPlayerBar();
                columnPlayer.setMaxWidth(0);
                button.setMinWidth(0);
                openWhenResizedPlayer = true;
            }
            if (chatPane.visibleProperty().get())
            {
                openChat();
                openWhenResizedChat = true;
            }
            //playerButton.setDisable(true);
            chatButton.setDisable(true);
        }
        else
        {
            if (openWhenResizedPlayer)
            {
                //openPlayerBar(null);
                setVisibilityPlayerBar();
                columnPlayer.setMaxWidth(3000);
                if (this.game.getPlayers().size() == 2) {
                    button.setMinWidth(380);
                } else {
                    button.setMinWidth(750);
                }
                openWhenResizedPlayer = false;
            }
            if (openWhenResizedChat)
            {
                openChat();
                openWhenResizedChat = false;
            }
            //playerButton.setDisable(false);
            chatButton.setDisable(false);
        }
    }

    private void stageSizeChanged (@SuppressWarnings("unused") ObservableValue<? extends Number> observableValue, Number oldVal, Number newVal)
    {
        double change = (double) newVal < (double) oldVal ? -((double) newVal / (double) oldVal) : (double) oldVal / (double) newVal;
        setCenter();
        zoomableScrollPane.onScroll(change, center);
    }

    private void highlightingChanged (PropertyChangeEvent propertyChangeEvent)
    {
        Tile tile = (Tile) propertyChangeEvent.getOldValue();
        tileDrawer.drawTile(tile);
    }

    @SuppressWarnings("unused")
    private void onHoveredChanged (
            ObservableValue<? extends Hoverable> observableValue,
            Hoverable last,
            Hoverable next
    )
    {
        // update the mini map on next draw cycle so that it doesn't cause issues with listener order
        Platform.runLater(() -> miniMapDrawer.drawMinimap(tileMap));
    }

    @SuppressWarnings("unused")
    private void onSelectedChanged (
            ObservableValue<? extends Selectable> observableValue,
            Selectable last,
            Selectable next
    )
    {
        Unit selectedUnit = context.getGameState().getSelectedUnit();

        if (last instanceof Unit)
        {
            ((Unit) last).positionProperty().removeListener(onSelectedUnitMoved);
        }
        if (next instanceof Unit)
        {
            ((Unit) next).positionProperty().addListener(onSelectedUnitMoved);
        }

        Platform.runLater(() ->
                          {
                              miniMapDrawer.drawMinimap(tileMap);

                              // in this case, last and current selected units are null, so no update needed
                              if (selectedUnit == null && !(last instanceof Unit))
                              {
                                  return;
                              }

                              setCellProperty(selectedUnit);
                          });
    }

    @SuppressWarnings("unused")
    private void onSelectedUnitMoved (Observable observable, Cell last, Cell next)
    {
        Platform.runLater(() ->
                          {
                              miniMapDrawer.drawMinimap(tileMap);
                              setCellProperty(context.getGameState().getSelectedUnit());
                          });
    }

    private void unitListChanged (ListChangeListener.Change<? extends Unit> c)
    {
        while (c.next())
        {
            logger.debug(c.toString());
            if (c.wasAdded())
            {
                for (Unit unit : c.getAddedSubList())
                {
                    unit.positionProperty().addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                    unit.hpProperty().addListener((observableValue, oldHp, newHp) -> unitHpChanged(observableValue,
                                                                                               oldHp, newHp, unit));
                }
            }

            if (c.wasRemoved())
            {
                for (Unit unit : c.getRemoved())
                {
                    unit.positionProperty().removeListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                    unit.hpProperty().removeListener((observableValue, oldHp, newHp) -> unitHpChanged(observableValue,
                                                                                                   oldHp, newHp, unit));
                }
            }
        }
    }

    private void unitHpChanged (ObservableValue<? extends Number> observableValue, Number oldHp, Number newHp, Unit unit)
    {
        if(unit == null) {
            return;
        }

        if(unit.getPosition() == null) {
            return;
        }

        tileDrawer.drawTile(getTileOf(unit.getPosition()));

        if(animationsAllowed.get() && newHp.floatValue() > 0) {
            attackAnimationManager.startAttackAnimation(canvas, unit);
        }
    }

    @SuppressWarnings("unused")
    private void unitChangedPosition (ObservableValue<? extends Cell> observableValue, Cell lastPosition, Cell newPosition, Unit unit)
    {
        if(animationsAllowed.get()) {

            if(lastPosition != null && newPosition != null)
            {
                movementAnimationManager.startMovementAnimation(canvas, unit, lastPosition, newPosition);
            }
            else if(lastPosition == null && newPosition != null)
            {
                revivalAnimationManager.startRevivalAnimation(canvas, unit);
            }
            else if(lastPosition != null && newPosition == null) {
                deathAnimationManager.startDeathAnimation(canvas, lastPosition);
            }
        }



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
    private void cameraViewChanged (ObservableValue<? extends Number> observableValue, Number number, Number number1)
    {
        miniMapDrawer.drawMinimap(tileMap);
    }


    private void initCanvas ()
    {
        canvas = new Canvas();

        canvas.setId("canvas");

        zoomableScrollPane = new ZoomableScrollPane(canvas);

        canvas.setHeight(CELL_SIZE * mapSize);
        canvas.setWidth(CELL_SIZE * mapSize);
        battlefieldStackPane.getChildren().add(0, zoomableScrollPane);
        tileDrawer.setCanvas(canvas);
        tileDrawer.drawMap(tileMap);
        rootPane.setFocusTraversable(true);
        rootPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && !endPhaseButton.disableProperty().get())
            {
                endPhase();
            } else if (event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.F10)) {
                showMenu(null);
            }
            if (event.getCode().equals(KeyCode.F11))
            {
                setFullscreen(null);
            }
        });

        movementAnimationManager.setMapRedraw(() -> tileDrawer.drawMap(tileMap));
        deathAnimationManager.setMapRedraw(() -> tileDrawer.drawMap(tileMap));
        attackAnimationManager.setMapRedraw(() -> tileDrawer.drawMap(tileMap));
        revivalAnimationManager.setMapRedraw(() -> tileDrawer.drawMap(tileMap));
    }

    private void initPlayerBar ()
    {

        HashMap<Pane, Player> panePlayerMap = new HashMap<>();
        playerCardList.add(player1);
        playerCardList.add(player2);
        playerCardList.add(player3);
        playerCardList.add(player4);

        playerListController = new PlayerListController(this.game);
        playerBar.setAlignment(Pos.TOP_CENTER);
        button.setMinWidth(750);
        int counter = 0;
        if (this.game.getPlayers().size() == 2)
        {
            playerBar.getChildren().remove(player1);
            playerCardList.remove(player1);
            playerBar.getChildren().remove(player4);
            playerCardList.remove(player4);
            button.setMinWidth(380);
        }

        for (Player player : this.game.getPlayers())
        {

            playerCardList.get(counter).getChildren().add(playerListController.getPlayerCards().get(counter));
            playerPaneMap.put(player.getId(), playerCardList.get(counter));
            playerMap.put(player.getId(), player);
            playerNodeMap.put(player.getId(), playerListController.getPlayerCards().get(counter));
            panePlayerMap.put(playerCardList.get(counter), player);

            counter++;
        }

        for(Pane playerPane : playerCardList){
            playerPane.setOnMouseClicked((mouseEvent)->{
                Player player = panePlayerMap.get(playerPane);
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY) && !player.equals(context.getUserPlayer())) {
                    chatController.chatTabManager().openTab('@' + player.getName());
                }
            });
        }

        playerListController = new PlayerListController(game);
        //playerBar.setVisible(false);
        //playerBar.setPickOnBounds(false);
        battlefieldStackPane.setPickOnBounds(false);
        miniMapStackPane.setPickOnBounds(false);

        if (!playerNodeMap.isEmpty() && this.game.getCurrentPlayer() != null)
        {
            playerNodeMap.get(this.game.getCurrentPlayer().getId()).setStyle("-fx-background-color: -selected-background-color");
        }
        this.game.currentPlayerProperty().addListener((observable, oldPlayer, newPlayer) ->
                                                      {
                                                          if (oldPlayer != null)
                                                          {
                                                              playerNodeMap.get(oldPlayer.getId()).setStyle("-fx-background-color: -surface-elevation-2-color");
                                                          }
                                                          if (newPlayer != null)
                                                          {
                                                              playerNodeMap.get(newPlayer.getId()).setStyle("-fx-background-color: -selected-background-color");
                                                          }
                                                      });

        this.game.getPlayers().addListener(playerListListener);
        phaseImage.imageProperty().setValue(new Image(getClass().getResource("/assets/icons/operation/footstepsWhite.png").toExternalForm()));
        this.game.phaseProperty().addListener(phaseChangedListener);
        roundCountLabel.textProperty().bind(roundCount.asString());
        phaseLabel.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "phaseLabel"));
        ingameInformationHBox.setStyle("-fx-background-color: -surface-elevation-2-color");
        //ingameInformationHBox.setSpacing(10);
        HBox.setMargin(ingameInformationHBox, new Insets(10, 10, 10, 10));
        //phaseLabel.textProperty().bind(this.game.phaseProperty());
        roundTextLabel.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "roundLabel"));
    }

    private void playerListChanged (ListChangeListener.Change<? extends Player> c)
    {
        while (c.next())
        {
            if (c.wasRemoved())
            {
                for (Player player : c.getRemoved())
                {
                    Platform.runLater(() ->
                                      {
                                          playerPaneMap.get(player.getId()).getChildren().remove(0);
                                          playerPaneMap.get(player.getId()).getChildren().add(playerListController.createLoserCard(player));
                                      });
                }
            }
        }
    }


    private void phaseChanged(@SuppressWarnings("unused") ObservableValue<? extends String> observableValue, String oldPhase, String newPhase)
    {
        switch (newPhase)
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

        phaseLabelView.visibleProperty().set(true);
        Bounce bounce = new Bounce(phaseLabelView);
        bounce.setCycleCount(1);
        if(skynet.isBotRunning()) {
            double speed = skynet.getBot().frequency.getValue() / 10.0 < 1 ? 1 : skynet.getBot().frequency.getValue()/10;
            bounce.setSpeed(speed);
        } else {
            bounce.setSpeed(1);
        }
        bounce.play();
        bounce.setOnFinished((e) -> phaseLabelView.visibleProperty().set(false));

        //TODO readd
//        if (context.isMyTurn())
//        {
//            actionButton.setDisable(false);
//            cancelButton.setDisable(false);
//        } else
//        {
//            actionButton.setDisable(true);
//            cancelButton.setDisable(true);
//        }
//        ChangeActionButtonIcon(newPhase);
    }

    //TODO readd
//    private void ChangeActionButtonIcon(String phase)
//    {
//        switch (phase)
//        {
//            case "movePhase":
//            {
//                JavaFXUtils.setButtonIcons(actionButton,
//                        getClass().getResource("/assets/icons/operation/footstepsWhite.png"),
//                        getClass().getResource("/assets/icons/operation/footstepsBlack.png"),
//                        40);
//
//                actionButton.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "move"));
//            }
//            break;
//            case "attackPhase":
//            {
//                JavaFXUtils.setButtonIcons(actionButton,
//                        getClass().getResource("/assets/icons/operation/swordClashWhite.png"),
//                        getClass().getResource("/assets/icons/operation/swordClashBlack.png"),
//                        40);
//
//                actionButton.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "attack"));
//            }
//            break;
//            case "lastMovePhase":
//            {
//                JavaFXUtils.setButtonIcons(actionButton,
//                        getClass().getResource("/assets/icons/operation/footprintWhite.png"),
//                        getClass().getResource("/assets/icons/operation/footprintBlack.png"),
//                        40);
//
//                actionButton.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "move"));
//            }
//            break;
//
//        }
//    }

    protected Tile resolveTargetTile (MouseEvent event)
    {
        int xPos = (int) (event.getX() / CELL_SIZE);
        int yPos = (int) (event.getY() / CELL_SIZE);

        return tileMap[yPos][xPos];
    }

    protected Cell resolveTargetCell (MouseEvent event)
    {
        return resolveTargetTile(event).getCell();
    }

    public void canvasHandleMouseMove (MouseEvent event)
    {
        Cell cell = resolveTargetCell(event);
        Unit unit = cell.getUnit();

        Objects.requireNonNullElse(unit, cell).setHoveredIn(context.getGameState());
    }

    public void canvasHandleMouseClicked (MouseEvent event)
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
            return;
        }

        handleSelection(tile);
    }

    protected void handleSelection (Tile tile)
    {
        Cell cell = tile.getCell();
        Unit unit = cell.getUnit();
        Selectable selectable = Objects.requireNonNullElse(unit, cell);

        if (selectable == context.getGameState().getSelected())
        {
            selectable.clearSelection();
        }
        else
        {
            selectable.setSelectedIn(context.getGameState());
        }
    }

    private boolean handleAttack (Tile tile)
    {

        Cell targetCell = tile.getCell();
        Unit selectedUnit = context.getGameState().getSelectedUnit();
        Unit targetUnit = targetCell.getUnit();

        if (
                !context.isMyTurn()
                        || !context.getGameState().isPhase(Game.Phase.attackPhase)
                        || Objects.isNull(selectedUnit)
                        || selectedUnit.getLeader() != context.getUserPlayer()
                        || !selectedUnit.canAttack(targetUnit)
                        || targetUnit == null
        )
        {
            return false;
        }

        actionExecutor.execute(new AttackAction(selectedUnit, targetUnit));

        return true;
    }

    private boolean handleMovement (Tile tile)
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

        actionExecutor.execute(new MovementAction(selectedUnit, tour));

        return true;
    }


    private void leaveGame(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        alertBuilder
                .priorityConfirmation(
                        AlertBuilder.Text.EXIT,
                        this::doLeaveGame,
                        null);
    }

    private void doLeaveGame ()
    {

        sceneManager
                .setScene(SceneConfiguration
                        .of(LOBBY)
                        .withExceptionHandler(exceptionHandler)
                );
    }

    public void zoomIn (@SuppressWarnings("unused") ActionEvent actionEvent)
    {
            zoomableScrollPane.onScroll(20.0, center);
    }

    public void zoomOut (@SuppressWarnings("unused") ActionEvent actionEvent)
    {

            zoomableScrollPane.onScroll(-20.0, center);
    }

    public void endPhase ()
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_PHASE,
                        this::doEndPhase,
                        null);
    }

    public void endRound(){
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.END_ROUND,
                        this::doEndRound,
                        null);
    }

    public void doEndRound(){
        String phase = this.context.getGameState().getPhase();
        if (phase.equals(Game.Phase.movePhase.name())){
            doEndPhase();
            doEndPhase();
            doEndPhase();
        } else if(phase.equals(Game.Phase.attackPhase.name())){
            doEndPhase();
            doEndPhase();
        } else if(phase.equals(Game.Phase.lastMovePhase.name())){
            doEndPhase();
        }
    }

    public void doEndPhase()
    {
        actionExecutor.execute(new PassAction(game));
    }

    @Override
    public void configure (@Nonnull IngameContext context)
    {
        this.context = context;

        configureHistory();

        game = context.getGameState();
        roundCount.bind(
            Bindings.createIntegerBinding(
                () -> ((game.getTurnCount()-1) / context.getGameData().getNeededPlayer()) + 1,
                game.turnCountProperty()
            )
        );


        if (phaseLabelView == null) {
            phaseLabelView = new PhaseLabelController().buildPhaseLabel(selectedLocale, phaseImage.imageProperty(), context.getGameState().currentPlayerProperty(), roundCount);
            rootPane.getChildren().add(phaseLabelView);
            phaseLabelView.visibleProperty().set(false);
        }

        game.getUnits().addListener((ListChangeListener) change -> {
            while(change.next()) {
                if (change.wasRemoved()) {
                    musicManager.playDeathSound();
                }
            }
        });
        if (game != null)
        {
            ObservableList<Cell> cells = game.getCells();
            units = game.getUnits();

            mapSize = (int) Math.sqrt(cells.size());
            tileMap = new Tile[mapSize][mapSize];

            for (Cell cell : cells)
            {
                Tile tile = new Tile(cell);
                tileMap[cell.getY()][cell.getX()] = tile;
                tile.addListener(highlightingListener);
            }

            for (Unit unit : units)
            {
                //Adds listener for units which are already in the list
                unit.positionProperty()
                        .addListener((observableValue, lastPosition, newPosition) -> unitChangedPosition(observableValue, lastPosition, newPosition, unit));
                unit.hpProperty()
                        .addListener((observableValue, oldHp, newHp) -> unitHpChanged(observableValue,
                                                                                                 oldHp, newHp,
                                                                                                     unit));
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

        rootPane.addEventHandler(KeyEvent.KEY_PRESSED, this::switchThroughUnits);

        miniMapCanvas.addEventHandler(MouseEvent.MOUSE_CLICKED, this::miniMapHandleMouseClick);

        this.context.getGameState().selectedUnitProperty()
                .addListener((observable, oldUnit, newUnit) -> setCellProperty(newUnit));

        this.context.getGameState().phaseProperty()
                .addListener(this::onNextPhase);

        configureEndPhaseAndEndRound();

        unitInformationContainer.getChildren().add(new UnitInfoBoxBuilder<Selectable>().build(game.selectedProperty(), "unitSelected", selectedLocale));
        unitInformationContainer.getChildren().add(new UnitInfoBoxBuilder<Hoverable>().build(game.hoveredProperty(), "unitHovered", selectedLocale));

        try
        {
            addAlertListeners();
            initChat();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        initActionExecutor();
        initSkynet();
        initSkynetButtons();
        if (sceneManager.isStageInit())
        {
            initListenersForFullscreen();
        }
    }

    public void setCenter(){
        Bounds bounds = zoomableScrollPane.localToScreen(zoomableScrollPane.getBoundsInLocal());
        center = new Point2D(bounds.getCenterX(), bounds.getCenterY());
    }




    @Autowired(required = false)
    public void setHistoryViewProvider(HistoryViewProvider historyViewProvider) {

        this.historyViewProvider = historyViewProvider;
    }

    private void configureHistory() {
        if (historyViewProvider == null) {
            return;
        }

        historyViewProvider.mountHistory(history, context);
    }

    private void switchThroughUnits(KeyEvent keyEvent){
        if(this.context.getGameState().getSelectedUnit() == null){
            return;
        }
        Unit selectedUnit = this.context.getGameState().getSelectedUnit();
        if (selectedUnit.getLeader() != this.context.getUserPlayer())
        {
            return;
        }
        int currentIndex = this.context.getUserPlayer().getUnits().indexOf(selectedUnit);
        if (keyEvent.getCode().equals(KeyCode.E))
        {
            getNextUnit(currentIndex);
        }
        else if (keyEvent.getCode().equals(KeyCode.Q))
        {
            getPreviousUnit(currentIndex);
        }
    }

    private void getNextUnit (int currentIndex)
    {
        if (this.context.getUserPlayer().getUnits().isEmpty())
        {
            return;
        }
        Unit nextSelected;
        if ((currentIndex + 1) < this.context.getUserPlayer().getUnits().size())
        {
            nextSelected = this.context.getUserPlayer().getUnits().get(currentIndex + 1);

        }
        else
        {
            nextSelected = this.context.getUserPlayer().getUnits().get(0);
        }
        selectNextAndCenterCamera(nextSelected);
    }

    private void getPreviousUnit (int currentIndex)
    {
        if (this.context.getUserPlayer().getUnits().isEmpty())
        {
            return;
        }
        Unit nextSelected;
        if ((currentIndex - 1) >= 0)
        {
            nextSelected = this.context.getUserPlayer().getUnits().get(currentIndex - 1);

        }
        else
        {
            int lastIndex = this.context.getUserPlayer().getUnits().size() - 1;
            nextSelected = this.context.getUserPlayer().getUnits().get(lastIndex);
        }
        selectNextAndCenterCamera(nextSelected);
    }


    private void selectNextAndCenterCamera (@Nullable Unit nextSelected)
    {
        if (nextSelected == null)
        {
            return;
        }
        game.setSelectedUnit(nextSelected);
        nextSelected.setSelected(true);
        Cell cell = nextSelected.getPosition();
        camera.TryToCenterToPostition(cell.getX(), cell.getY());
    }


    private void onNextPhase(@SuppressWarnings("unused") Observable observable, @SuppressWarnings("unused") String lastPhase, @SuppressWarnings("unused") String nextPhase)
    {
        setCellProperty(null);
    }

    private void addAlertListeners ()
    {
        game.winnerProperty().addListener(((observable, oldValue, newValue) ->
        {
            if (newValue != null)
            {
                if (skynet.isBotRunning())
                {
                    skynetRunningProperty.set(false);
                    skynet.stopBot();
                }
                showWinnerLoserAlert(newValue);
            }
        }));
        this.context.getUserPlayer().getUnits().addListener((ListChangeListener<Unit>) unitList ->
        {
            if (unitList.getList().isEmpty() && this.context.getGameState().getPlayers().size() > 2)
            {
                if (skynet.isBotRunning())
                {
                    skynetRunningProperty.set(false);
                    skynet.stopBot();
                }

                alertBuilder.priorityConfirmation(
                        AlertBuilder.Text.GAME_LOST,
                        () -> this.context.getGameData().setSpectatorModus(true),
                        this::doLeaveGame
                );
            }
        });
    }

    private void showWinnerLoserAlert (Player winner)
    {
        if (winner.equals(this.context.getUserPlayer()))
        {
            alertBuilder.priorityInformation(
                    AlertBuilder.Text.GAME_WON,
                    this::doLeaveGame);
        }
        else
        {
            alertBuilder.priorityInformation(
                    AlertBuilder.Text.GAME_SOMEBODY_ELSE_WON,
                    this::doLeaveGame,
                    winner.getName());
        }
    }


    private void initChat () throws Exception
    {
        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(context.getGameEventManager());
        chatPane.getChildren().add(chatComponents.getRoot());
        chatController = chatComponents.getController();
        chatPane.setVisible(false);
    }

    public void openChat ()
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
            // Fix for black label, but its rather a JavaFX bug
            for(ChatChannelController c: chatController.getChatChannelControllers().values()) {
                c.setWhiteColor();
            }
            chatPane.setVisible(true);
            JavaFXUtils.setButtonIcons(
                    chatButton,
                    getClass().getResource("/assets/icons/operation/chatWhite.png"),
                    getClass().getResource("/assets/icons/operation/chatBlack.png"),
                    40
            );
        }
    }


    private Tile getTileOf(Object positioned)
    {

        Cell position;

        if (positioned instanceof Unit)
        {
            position = ((Unit) positioned).getPosition();
        }
        else
        {
            position = (Cell) positioned;
        }

        return tileMap[position.getY()][position.getX()];
    }


    private void setCellProperty (@Nullable Unit selectedUnit)
    {
        if (selectedUnit == null)
        {
            for (Cell cell : this.context.getGameState().getCells())
            {
                cell.setIsReachable(false);
            }
            return;
        }

        if (
                selectedUnit.getLeader() == null
                        || !selectedUnit.getLeader().isPlayer()
                        || (!this.context.isMyTurn())
        )
        {
            return;
        }


        if (!this.context.getGameState().isPhase(Game.Phase.attackPhase))
        {
            setMoveRadius(selectedUnit);
        }
    }

    private void setMoveRadius (@Nonnull Unit selectedUnit)
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


    private void miniMapHandleMouseClick (MouseEvent mouseEvent)
    {
        int xPos = miniMapDrawer.getXPostionOnMap(mouseEvent.getX());
        int yPos = miniMapDrawer.getYPostionOnMap(mouseEvent.getY());
        camera.TryToCenterToPostition(xPos, yPos);
        miniMapDrawer.drawMinimap(tileMap);
    }

    private void configureEndPhaseAndEndRound()
    {
        BooleanProperty playerCanEndPhase = new SimpleBooleanProperty(false);
        ObjectProperty<Player> currentPlayerProperty = this.context.getGameState().currentPlayerProperty();

        playerCanEndPhase.bind(Bindings.createBooleanBinding(
                () -> (context.isMyTurn() && this.context.getGameState().getInitiallyMoved() && context.getModelManager().getHistory().isLatest()),
                currentPlayerProperty, this.context.getGameState().initiallyMovedProperty(), context.getModelManager().getHistory().currentProperty()
        ));

        skynetRunningProperty = new SimpleBooleanProperty(false);
        isUsersTurn = new SimpleBooleanProperty(false);
        isUsersTurn.bind(Bindings.createBooleanBinding(
                () -> context.isMyTurn(), currentPlayerProperty
        ));

        playerCanEndPhase.addListener(((observable, oldValue, newValue) -> {}));
        isUsersTurn.addListener(((observable, oldValue, newValue) -> {}));

        endPhaseButton.disableProperty().bind(playerCanEndPhase.not().or(skynetRunningProperty).or(historyIsTailProperty.not()));

        endPhaseButton.disableProperty().addListener(((observable, oldValue, newValue) -> {}));

        endPhaseButton.setTooltip(new Tooltip("ENTER"));
        endPhaseButton.setOnMouseClicked(event -> {
            if (event.getButton().equals(MouseButton.PRIMARY))
            {
                endPhase();
            }
        });

        endRoundButton.disableProperty().bind(playerCanEndPhase.not().or(skynetRunningProperty).or(historyIsTailProperty.not()));

        endRoundButton.disabledProperty().addListener((((observable, oldValue, newValue) -> {})));
    }


    private void initMiniMap ()
    {
        miniMapDrawer.setCanvas(miniMapCanvas, mapSize);
        miniMapDrawer.setCamera(camera);
        miniMapDrawer.drawMinimap(tileMap);
    }

    /**
     * i don't think, we need to remove any listeners at all in the battlefield controller,
     * since we throw away the whole game anyway.
     */
    @Override
    public void terminate ()
    {
        Game gameState = context.getGameState();

        if (gameState == null)
        {
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
            unit.hpProperty().removeListener((observableValue, oldHp, newHp) -> unitHpChanged(observableValue,
                                                                                               oldHp, newHp,
                                                                                              unit));
        }

        units.removeListener(unitListListener);

        zoomableScrollPane.scaleValueProperty().removeListener(cameraViewChangedListener);
        zoomableScrollPane.hvalueProperty().removeListener(cameraViewChangedListener);
        zoomableScrollPane.vvalueProperty().removeListener(cameraViewChangedListener);

        game.getPlayers().removeListener(playerListListener);

        if (skynet.isBotRunning())
        {
            skynetRunningProperty.set(false);
            skynet.stopBot();
        }
    }

    /*public void openPlayerBar (@SuppressWarnings("unused") ActionEvent event)
    {
        if (!playerBar.visibleProperty().get())
        {
            playerBar.visibleProperty().setValue(true);
            playerBar.toFront();
            JavaFXUtils.setButtonIcons(
                    playerButton,
                    getClass().getResource("/assets/icons/navigation/accountWhite.png"),
                    getClass().getResource("/assets/icons/navigation/accountBlack.png"),
                    40
            );
        }
        else
        {
            playerBar.visibleProperty().setValue(false);
            playerBar.toBack();
            JavaFXUtils.setButtonIcons(
                    playerButton,
                    getClass().getResource("/assets/icons/navigation/outlineAccountWhite.png"),
                    getClass().getResource("/assets/icons/navigation/outlineAccountBlack.png"),
                    40
            );
        }
    }*/

    private void setVisibilityPlayerBar()
    {
        if (!playerBar.visibleProperty().get())
        {
            playerBar.visibleProperty().setValue(true);
        }
        else
        {
            playerBar.visibleProperty().setValue(false);
        }
    }

    public void toggleHpBar (@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (tileDrawer.isHpBarVisibility())
        {
            tileDrawer.setHpBarVisibility(false);
        }
        else
        {
            tileDrawer.setHpBarVisibility(true);
        }
        tileDrawer.drawMap(tileMap);
    }

    private void initActionExecutor ()
    {
        actionExecutor = new ActionExecutor(context.getGameEventManager().api())
                .setTileDrawer(tileDrawer).setSurrenderGameAction(this::surrender);
    }

    private void initSkynet ()
    {
        skynet = new Skynet(actionExecutor,
                            game,
                            context.getUserPlayer())
                .addBehaviour(new MovementBehaviour(), "movePhase", "lastMovePhase")
                .addBehaviour(new AttackBehaviour(), "attackPhase")
                .addBehaviour(new SurrenderBehaviour(), "surrender");
    }

    private void initSkynetButtons ()
    {
        JavaFXUtils.setButtonIcons(
                skynetTurnButton,
                getClass().getResource("/assets/icons/operation/oneRoundPlaneWhite.png"),
                getClass().getResource("/assets/icons/operation/oneRoundPlaneBlack.png"),
                40
        );
        skynetTurnButton.setOnAction((e) -> {
            jumpToHistoryTail();
            skynet.turn();
        });
        skynetTurnButton.disableProperty().bind(isUsersTurn.not().or(skynetRunningProperty).or(historyIsTailProperty.not()));
        skynetTurnButton.disabledProperty().addListener((((observable, oldValue, newValue) -> {})));
        JavaFXUtils.setButtonIcons(
                skynetButton,
                getClass().getResource("/assets/icons/operation/skynetInactiveWhite.png"),
                getClass().getResource("/assets/icons/operation/skynetInactiveBlack.png"),
                40
        );
        skynetButton.setOnAction(this::startBot);

        if (context != null && context.getModelManager() != null && context.getModelManager().getHistory() != null) {
            final History history =  context.getModelManager().getHistory();
            history.currentProperty().addListener(((observable, oldValue, newValue) -> {
                if (!history.isLatest()) {
                    historyIsTailProperty.set(false);
                } else {
                    historyIsTailProperty.set(true);
                }
                if (!history.isLatest() && skynet.isBotRunning()) {
                    skynetRunningProperty.set(false);
                    skynet.stopBot();
                }
            }));
        }
    }

    private void jumpToHistoryTail() {
        final History history =  context.getModelManager().getHistory();
        if (!history.isLatest()) {
            history.timeTravel(history.getTail());
        }
    }


    private void startBot(@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (skynet.isBotRunning())
        {
            skynetRunningProperty.set(false);
            JavaFXUtils.setButtonIcons(
                    skynetButton,
                    getClass().getResource("/assets/icons/operation/skynetInactiveWhite.png"),
                    getClass().getResource("/assets/icons/operation/skynetInactiveBlack.png"),
                    40
            );
            skynet.stopBot();
            animationButton.setDisable(false);
            animationsAllowed.set(true);
        }
        else
        {
            jumpToHistoryTail();
            skynetRunningProperty.set(true);
            JavaFXUtils.setButtonIcons(
                    skynetButton,
                    getClass().getResource("/assets/icons/operation/skynetActiveWhite.png"),
                    getClass().getResource("/assets/icons/operation/skynetActiveBlack.png"),
                    40
            );
            animationButton.setDisable(true);
            animationsAllowed.set(false);
            skynet.startBot();
        }
    }


    public void surrender ()
    {
        alertBuilder.priorityInformation(
                AlertBuilder.Text.SURRENDER,
                this::doLeaveGame
        );
    }


    private void setFullscreen(@SuppressWarnings("unused") ActionEvent actionEvent) {
        if (sceneManager.isFullscreen()) {
            sceneManager.unsetFullscreen();
            setFullscreenButton();

        } else {
            sceneManager.setFullscreen();
            setFullscreenButton();
        }
    }

    public void showMenu(@SuppressWarnings("unused") ActionEvent actionEvent) {
        final List<Entry> entries = new ArrayList<>();

        fullscreenButton.getStyleClass().add("icon-button");
        setFullscreenButton();
        fullscreenButton.setOnAction(this::setFullscreen);
        entries.add(new Entry("fullscreen", fullscreenButton, Orientation.HORIZONTAL));
        fullscreenButton.setTooltip(new Tooltip("F11"));

        final Slider slider = new Slider(0.5, 10, skynet.getBot().frequency.getValue());
        slider.valueProperty().bindBidirectional(skynet.getBot().frequency);
        entries.add(new Entry("skynetSpeed", slider, Orientation.VERTICAL));

        final Button leaveGameButton = new Button();
        leaveGameButton.getStyleClass().add("icon-button");
        JavaFXUtils.setButtonIcons(
                leaveGameButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                40
        );
        leaveGameButton.setOnAction(this::leaveGame);
        entries.add(new Entry("leaveGame", leaveGameButton, Orientation.HORIZONTAL));

        menuBuilder.battlefieldMenu(entries);
    }

    public void toggleAnimations (ActionEvent actionEvent)
    {

        animationsAllowed.set(!animationsAllowed.get());
        if(animationsAllowed.get()) {
            JavaFXUtils.setButtonIcons(
                    animationButton,
                    getClass().getResource("/assets/icons/operation/animation_white.png"),
                    getClass().getResource("/assets/icons/operation/animation_black.png"),
                    40
            );
        }
        else {
            JavaFXUtils.setButtonIcons(
                    animationButton,
                    getClass().getResource("/assets/icons/operation/noAnimation_white.png"),
                    getClass().getResource("/assets/icons/operation/noAnimation_black.png"),
                    40
            );
        }
    }
}
