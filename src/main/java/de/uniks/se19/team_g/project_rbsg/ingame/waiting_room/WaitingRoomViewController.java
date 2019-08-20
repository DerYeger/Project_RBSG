package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.army_builder.army_selection.*;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.configuration.*;
import de.uniks.se19.team_g.project_rbsg.egg.*;
import de.uniks.se19.team_g.project_rbsg.ingame.*;
import de.uniks.se19.team_g.project_rbsg.ingame.event.*;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_army.*;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_map.*;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.*;
import de.uniks.se19.team_g.project_rbsg.util.*;
import io.rincl.*;
import javafx.application.*;
import javafx.beans.Observable;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import javax.annotation.*;
import java.util.*;
import java.util.function.*;


/**
 * @author Keanu Stückrad
 * @author Jan Müller
 */
@Scope("prototype")
@Controller
public class WaitingRoomViewController implements RootController, IngameViewController, Rincled
{

    private static final int ICON_SIZE = 40;
    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final MusicManager musicManager;
    private final SplashImageBuilder splashImageBuilder;
    private final ApplicationState applicationState;
    private final ChatBuilder chatBuilder;
    private final AlertBuilder alertBuilder;
    private final ArmyPreviewBuilder armyPreviewBuilder;
    @Nonnull
    private final Function<VBox, ArmySelectorController> armySelectorComponent;
    private final PreviewMapBuilder previewMapBuilder;
    private final EasterEggController easterEggController;
    private final Property<Locale> selectedLocale;
    public Pane player1Pane;
    public Pane player2Pane;
    public Pane player3Pane;
    public Pane player4Pane;
    public Label gameName;
    public Pane chatContainer;
    public Pane mapPreviewPane;
    public VBox armySelector;
    public Button soundButton;
    public Button leaveButton;
    public Button readyButton;
    public Pane readyButtonContainer;
    public AnchorPane root;
    public ModelManager modelManager;
    public StackPane rootStackPane;
    private Logger logger = LoggerFactory.getLogger(getClass());
    private boolean ready;
    private boolean selectButtonDoubleClicked;
    // TODO: Ask Jan, wether this can be removed
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ChatController chatController;
    private PlayerCardBuilder playerCard;
    private PlayerCardBuilder playerCard2;
    private PlayerCardBuilder playerCard3;
    private PlayerCardBuilder playerCard4;
    private ObservableList<PlayerCardBuilder> playerCardBuilders;
    private ObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();
    private SimpleBooleanProperty disabledReadyButton = new SimpleBooleanProperty();

    private ChangeListener<Army> HoveredArmyListener = this::hoveredArmyChanged;
    private Node lastArmyPreview = null;
    /**
     * keep reference for WeakReferences further down the road
     */
    @SuppressWarnings("FieldCanBeLocal")
    private ArmySelectorController armySelectorController;
    private IngameContext context;

    // weak ref binding
    @SuppressWarnings("FieldCanBeLocal")
    private BooleanBinding startGameBinding;
    private SimpleIntegerProperty readyCounter = new SimpleIntegerProperty(0);

    @Autowired
    public WaitingRoomViewController (
            @Nonnull final GameProvider gameProvider,
            @Nonnull final UserProvider userProvider,
            @Nonnull final SceneManager sceneManager,
            @Nonnull final MusicManager musicManager,
            @Nonnull final SplashImageBuilder splashImageBuilder,
            @Nonnull final ApplicationState applicationState,
            @Nonnull final ChatBuilder chatBuilder,
            @Nonnull final PreviewMapBuilder previewMapBuilder,
            @Nonnull final AlertBuilder alertBuilder,
            @Nonnull final Function<VBox, ArmySelectorController> armySelectorComponent,
            @Nonnull final ModelManager modelManager,
            @Nonnull final Property<Locale> selectedLocale,
            @NonNull final EasterEggController easterEggController
    )
    {
        this.selectedLocale = selectedLocale;
        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.musicManager = musicManager;
        this.splashImageBuilder = splashImageBuilder;
        this.applicationState = applicationState;
        this.chatBuilder = chatBuilder;
        this.alertBuilder = alertBuilder;
        this.armySelectorComponent = armySelectorComponent;
        this.modelManager = modelManager;
        this.previewMapBuilder = previewMapBuilder;
        this.easterEggController = easterEggController;
        this.armyPreviewBuilder = new ArmyPreviewBuilder();
    }

    public void initialize ()
    {
        gameName.textProperty().setValue(gameProvider.get().getName());
        initPlayerCardBuilders();
        setPlayerCardNodes();
        ready = false;
        selectButtonDoubleClicked = false;
        disabledReadyButton.set(false);
        JavaFXUtils.setButtonIcons(
                readyButton,
                getClass().getResource("/assets/icons/navigation/crossWhiteBig.png"),
                getClass().getResource("/assets/icons/navigation/checkBlackBig.png"),
                200
        );
        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                ICON_SIZE
        );
        JavaFXUtils.bindButtonDisableWithTooltip(
                readyButton,
                readyButtonContainer,
                new SimpleStringProperty(getResources().getString("readyTooltip")),
                disabledReadyButton
        );
        musicManager.initButtonIcons(soundButton);
        root.setBackground(new Background(splashImageBuilder.getSplashImage()));

        readyCounter.addListener(((observable, oldValue, newValue) -> egg(newValue.intValue())));
    }

    private void withChatSupport () throws Exception
    {
        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(context.getGameEventManager());
        chatContainer.getChildren().add(chatComponents.getRoot());
        chatController = chatComponents.getController();
        /* Chat is enbaled for user in spectator modus
            by commenting in the following lines the chat can be disabled again
        if(this.context.getGameData().isSpectatorModus()){
            chatController.getChatChannelControllers().get("General").getInputField().setDisable(true);
        }
         */
    }

    private void initPlayerCardBuilders ()
    {
        playerCard = new PlayerCardBuilder();
        playerCard2 = new PlayerCardBuilder();
        playerCardBuilders = FXCollections.observableArrayList();
        playerCardBuilders.add(playerCard);
        playerCardBuilders.add(playerCard2);
        if (gameProvider.get().getNeededPlayer() == 4)
        {
            playerCard3 = new PlayerCardBuilder();
            playerCard4 = new PlayerCardBuilder();
            playerCardBuilders.add(playerCard3);
            playerCardBuilders.add(playerCard4);
        }
    }

    private void setPlayerCardNodes ()
    {
        player1Pane.getChildren().add(playerCard.buildPlayerCard(selectedLocale));
        player2Pane.getChildren().add(playerCard2.buildPlayerCard(selectedLocale));
        playerCard2.switchColumns();
        if (gameProvider.get().getNeededPlayer() == 4)
        {
            // if visibility was disabled before for example when leaving game
            player3Pane.setVisible(true);
            player4Pane.setVisible(true);
            AnchorPane.setTopAnchor(player1Pane, 102.0);
            AnchorPane.setTopAnchor(player2Pane, 102.0);
            player3Pane.getChildren().add(playerCard3.buildPlayerCard(selectedLocale));
            player4Pane.getChildren().add(playerCard4.buildPlayerCard(selectedLocale));
            playerCard4.switchColumns();
        }
        else
        {
            AnchorPane.setTopAnchor(player1Pane, 180.0);
            AnchorPane.setTopAnchor(player2Pane, 180.0);
            player3Pane.setVisible(false);
            player4Pane.setVisible(false);
        }
    }

    public void leaveRoom ()
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::leaveWaitingRoom,
                        null);
    }

    private void leaveWaitingRoom ()
    {
        gameProvider.clear();
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

    public void toggleSound ()
    {
        musicManager.toggleMusicAndUpdateButtonIconSet(soundButton);
    }

    private void showMapPreview (@NonNull final List<Cell> cells)
    {
        final Node previewMap = previewMapBuilder.buildPreviewMap(cells, 256, 256);
        Platform.runLater(() -> mapPreviewPane.getChildren().add(previewMap));
    }

    public void setPlayerCards (Game game)
    {
        // init PlayerCards
        for (Player p : game.getPlayers())
        {
            for (PlayerCardBuilder playerC : playerCardBuilders)
            {
                if (playerC.isEmpty)
                {
                    playerC.setPlayer(p, Color.valueOf(p.getColor()));
                    break;
                }
            }
        }
        // ListChangeListener for Player (+ PlayerCards)
        game.getPlayers().addListener((ListChangeListener<Player>) c ->
        {
            while (c.next())
            {
                if (c.wasAdded())
                {
                    for (Player p : c.getAddedSubList())
                    {
                        for (PlayerCardBuilder playerC : playerCardBuilders)
                        {
                            if ((playerC.isEmpty) && (p.getColor() != null))
                            {
                                playerC.setPlayer(p, Color.valueOf(p.getColor()));
                                break;
                            }
                        }
                    }
                }
                if (c.wasRemoved())
                {
                    for (Player p : c.getRemoved())
                    {
                        for (PlayerCardBuilder playerC : playerCardBuilders)
                        {
                            if (!playerC.isEmpty)
                            {
                                if (playerC.getPlayer().equals(p))
                                {
                                    Platform.runLater(playerC::playerLeft);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    protected void configureArmySelection ()
    {
        armySelectorController = armySelectorComponent.apply(armySelector);
        if(armySelectorController != null && armySelectorController.hoveredArmyProperty() != null) {
            armySelectorController.hoveredArmyProperty().addListener(HoveredArmyListener);
        }
        selectedArmy.addListener((observable, oldValue, newValue) ->
                                 {
                                     JavaFXUtils.setButtonIcons(
                                             readyButton,
                                             getClass().getResource("/assets/icons/navigation/crossWhiteBig.png"),
                                             getClass().getResource("/assets/icons/navigation/checkBlackBig.png"),
                                             200
                                     );
                                     disabledReadyButton.set(true);
                                     ready = false;
                                     if (selectButtonDoubleClicked)
                                     {
                                         Army army = new Army();
                                         army.id.set("notReady");
                                         disabledReadyButton.set(false);
                                         newValue = army;
                                         selectButtonDoubleClicked = false;
                                     }
                                     context.getGameEventManager().sendMessage(CommandBuilder.changeArmy(newValue));
                                 });

        /*
         * normally, an observable list is only aware of items added and removed
         * we can wrap our armies in a bound observable list with extractor to also receive update events of items in the list
         */
        final ObservableList<Army> playableAwareArmies = FXCollections.observableArrayList(
                army -> new Observable[] {army.isPlayable}
        );
        Bindings.bindContent(playableAwareArmies, applicationState.armies);

        armySelectorController.setSelection(playableAwareArmies.filtered(a -> a.isPlayable.get()), selectedArmy);

        if (this.context.getGameData().isSpectatorModus())
        {
            armySelector.setDisable(true);
            readyButton.setDisable(true);
        }
    }


    @Override
    public void configure (@Nonnull IngameContext context)
    {

        this.context = context;

        if (context.isInitialized())
        {
            onInitialized();
        }
        else
        {
            final ReadOnlyBooleanProperty initializedProperty = context.initializedProperty();

            initializedProperty.addListener(
                    new ChangeListener<>()
                    {
                        @Override
                        public void changed (ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue)
                        {
                            WaitingRoomViewController.this.onInitialized();
                            initializedProperty.removeListener(this);
                        }
                    }
            );
        }

        try
        {
            withChatSupport();
        }
        catch (Exception e)
        {
            logger.error("couldn't setup chat", e);
        }

        configureArmySelection();
    }

    private void onInitialized ()
    {

        configureAutoStartHook();
        setPlayerCards(context.getGameState());
        showMapPreview(context.getGameState().getCells());
    }

    private void configureAutoStartHook ()
    {
        ObservableList<Player> readyPlayers = FXCollections.observableArrayList(
                player -> new Observable[] {player.isReadyProperty()}
        );

        Bindings.bindContent(readyPlayers, context.getGameState().getPlayers());

        startGameBinding = Bindings.createBooleanBinding(
                () -> readyPlayers.stream().filter(Player::getIsReady).count() == gameProvider.get().getNeededPlayer(),
                readyPlayers
        );

        if (startGameBinding.get())
        {
            mayStartGame();
        }
        else
        {
            startGameBinding.addListener((observable, oldValue, newValue) ->
                                         {
                                             if (newValue)
                                             {
                                                 mayStartGame();
                                             }
                                         });
        }
    }

    private void mayStartGame ()
    {
        logger.debug("trigger game start");
        context.getGameEventManager().sendMessage(CommandBuilder.startGame());
    }

    public void setReady (@SuppressWarnings("unused") ActionEvent actionEvent)
    {
        if (ready)
        {
            selectButtonDoubleClicked = true;
            armySelectorController.unselect();
        }
        else
        {
            JavaFXUtils.setButtonIcons(
                    readyButton,
                    getClass().getResource("/assets/icons/navigation/checkWhiteBig.png"),
                    getClass().getResource("/assets/icons/navigation/crossBlackBig.png"),
                    200
            );
            context.getGameEventManager().sendMessage(CommandBuilder.readyToPlay());
            ready = true;
            readyCounter.set(readyCounter.get() + 1);
        }
    }

    private void egg (final int readyCounter)
    {
        if (readyCounter == 5)
        {
            Platform.runLater(easterEggController::start);
        }
    }

    private void hoveredArmyChanged (ObservableValue<? extends Army> observableValue, Army oldArmy, Army newArmy)
    {
        if (newArmy != null)
        {
            lastArmyPreview = armyPreviewBuilder.build(newArmy);
            rootStackPane.getChildren().add(lastArmyPreview);
        }
        else {
            rootStackPane.getChildren().remove(lastArmyPreview);
        }
    }
}
