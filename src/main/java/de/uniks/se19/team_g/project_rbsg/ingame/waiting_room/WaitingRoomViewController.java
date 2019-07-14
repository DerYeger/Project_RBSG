package de.uniks.se19.team_g.project_rbsg.ingame.waiting_room;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.RootController;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.army_builder.army_selection.ArmySelectorController;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameViewController;
import de.uniks.se19.team_g.project_rbsg.ingame.event.CommandBuilder;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.waiting_room.preview_map.PreviewMapBuilder;
import de.uniks.se19.team_g.project_rbsg.login.SplashImageBuilder;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Controller;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.Function;


/**
 * @author  Keanu Stückrad
 * @author Jan Müller
 */
@Scope("prototype")
@Controller
public class WaitingRoomViewController implements RootController, IngameViewController {

    private Logger logger = LoggerFactory.getLogger(getClass());

    private static final int ICON_SIZE = 40;

    public Pane player1Pane;
    public Pane player2Pane;
    public Pane player3Pane;
    public Pane player4Pane;
    public Pane chatContainer;
    public Pane mapPreviewPane;
    public Pane miniGamePane; // TODO Tic-Tac-Toe?
    public VBox armySelector;
    public Button soundButton;
    public Button leaveButton;
    public Button showInfoButton;
    public AnchorPane root;

    // TODO: Ask Jan, wether this can be removed
    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private ChatController chatController;

    private PlayerCardBuilder playerCard;
    private PlayerCardBuilder playerCard2;
    private PlayerCardBuilder playerCard3;
    private PlayerCardBuilder playerCard4;
    private ObservableList<PlayerCardBuilder> playerCardBuilders;

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final MusicManager musicManager;
    private final SplashImageBuilder splashImageBuilder;
    private final ApplicationState applicationState;
    private final ChatBuilder chatBuilder;
    private final AlertBuilder alertBuilder;
    @Nonnull
    private final Function<VBox, ArmySelectorController> armySelectorComponent;
    private final PreviewMapBuilder previewMapBuilder;
    public ModelManager modelManager;

    private ObjectProperty<Army> selectedArmy = new SimpleObjectProperty<>();

    /**
     * keep reference for WeakReferences further down the road
     */
    @SuppressWarnings("FieldCanBeLocal")
    private ArmySelectorController armySelectorController;
    private IngameContext context;

    // weak ref binding
    @SuppressWarnings("FieldCanBeLocal")
    private BooleanBinding startGameBinding;

    @Autowired
    public WaitingRoomViewController(
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
            @Nonnull final ModelManager modelManager
    ) {
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
    }

    public void initialize() {
        initPlayerCardBuilders();
        setPlayerCardNodes();
        JavaFXUtils.setButtonIcons(
                leaveButton,
                getClass().getResource("/assets/icons/navigation/arrowBackWhite.png"),
                getClass().getResource("/assets/icons/navigation/arrowBackBlack.png"),
                ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                showInfoButton,
                getClass().getResource("/assets/icons/navigation/infoWhite.png"),
                getClass().getResource("/assets/icons/navigation/infoBlack.png"),
                ICON_SIZE
        );
        musicManager.initButtonIcons(soundButton);
        root.setBackground(new Background(splashImageBuilder.getSplashImage()));
    }

    private void withChatSupport() throws Exception {
        final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(context.getGameEventManager());
        chatContainer.getChildren().add(chatComponents.getRoot());
        chatController = chatComponents.getController();
    }

    private void initPlayerCardBuilders() {
        playerCard = new PlayerCardBuilder();
        playerCard2 = new PlayerCardBuilder();
        playerCardBuilders = FXCollections.observableArrayList();
        playerCardBuilders.add(playerCard2);
        if(gameProvider.get().getNeededPlayer() == 4) {
            playerCard3 = new PlayerCardBuilder();
            playerCard4 = new PlayerCardBuilder();
            playerCardBuilders.add(playerCard3);
            playerCardBuilders.add(playerCard4);
        }
    }

    private void setPlayerCardNodes() {
        player1Pane.getChildren().add(playerCard.buildPlayerCard());
        player2Pane.getChildren().add(playerCard2.buildPlayerCard());
        playerCard2.switchColumns();
        if(gameProvider.get().getNeededPlayer() == 4) {
            // if visibility was disabled before for example when leaving game
            player3Pane.setVisible(true);
            player4Pane.setVisible(true);
            AnchorPane.setTopAnchor(player1Pane, 102.0);
            AnchorPane.setTopAnchor(player2Pane, 102.0);
            player3Pane.getChildren().add(playerCard3.buildPlayerCard());
            player4Pane.getChildren().add(playerCard4.buildPlayerCard());
            playerCard4.switchColumns();
        } else {
            AnchorPane.setTopAnchor(player1Pane, 180.0);
            AnchorPane.setTopAnchor(player2Pane, 180.0);
            player3Pane.setVisible(false);
            player4Pane.setVisible(false);
        }
    }

    public void leaveRoom() {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.EXIT,
                        this::leaveWaitingRoom,
                        null);
    }

    private void leaveWaitingRoom() {
        gameProvider.clear();
        sceneManager.setScene(SceneManager.SceneIdentifier.LOBBY, false, null);
    }

    public void toggleSound() {
        musicManager.updateMusicButtonIcons(soundButton);
    }

    private void showMapPreview(@NonNull final List<Cell> cells) {
        final Node previewMap = previewMapBuilder.buildPreviewMap(cells, mapPreviewPane.getWidth(), mapPreviewPane.getHeight());
        Platform.runLater(() -> mapPreviewPane.getChildren().add(previewMap));
    }

    public void setPlayerCards(Game game) {
        // init PlayerCards
        boolean skipped = false;
        Player user = null;
        for(Player p: game.getPlayers()) {
            if(p.getName().equals(userProvider.get().getName())) {
                user = p;
            }
        }
        if (user == null) {
            // Exception
            return;
        }
        playerCard.setPlayer(user, Color.valueOf(user.getColor()));
        for (Player p : game.getPlayers()) {
            if(p.equals(user) && !skipped){
                skipped = true;
                continue;
            }
            for(PlayerCardBuilder playerC: playerCardBuilders){
                if(playerC.isEmpty) {
                    playerC.setPlayer(p, Color.valueOf(p.getColor()));
                    break;
                }
            }
        }
        // ListChangeListener for Player (+ PlayerCards)
        game.getPlayers().addListener((ListChangeListener<Player>) c -> {
            while (c.next()) {
                if (c.wasAdded()) {
                    for (Player p : c.getAddedSubList()) {
                        for(PlayerCardBuilder playerC: playerCardBuilders){
                            if(playerC.isEmpty) {
                                playerC.setPlayer(p, Color.valueOf(p.getColor()));
                                break;
                            }
                        }
                    }
                }
                if (c.wasRemoved()) {
                    for (Player p : c.getRemoved()) {
                        for(PlayerCardBuilder playerC: playerCardBuilders){
                            if(!playerC.isEmpty) {
                                if(playerC.getPlayer().equals(p)) {
                                    playerC.playerLeft();
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    protected void configureArmySelection() {
        armySelectorController = armySelectorComponent.apply(armySelector);

        selectedArmy.addListener((observable, oldValue, newValue) -> {
            context.getGameEventManager().sendMessage(CommandBuilder.changeArmy(newValue));
            context.getGameEventManager().sendMessage(CommandBuilder.readyToPlay());
        });

        /*
         * normally, an observable list is only aware of items added and removed
         * we can wrap our armies in a bound observable list with extractor to also receive update events of items in the list
         */
        final ObservableList<Army> playableAwareArmies = FXCollections.observableArrayList(
            army -> new Observable[] {army.isPlayable}
        );
        Bindings.bindContent( playableAwareArmies, applicationState.armies);

        armySelectorController.setSelection(playableAwareArmies.filtered(a -> a.isPlayable.get()), selectedArmy);
    }

    @Override
    public void configure(@Nonnull IngameContext context) {

        this.context = context;

        if (context.isInitialized()) {
            onInitialized();
        } else {
            final ReadOnlyBooleanProperty initializedProperty = context.initializedProperty();

            initializedProperty.addListener(
                new ChangeListener<>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        WaitingRoomViewController.this.onInitialized();
                        initializedProperty.removeListener(this);
                    }
                }
            );
        }

        try {
            withChatSupport();
        } catch (Exception e) {
            logger.error("couldn't setup chat", e);
        }

        configureArmySelection();
    }

    private void onInitialized() {

        configureAutoStartHook();
        setPlayerCards(context.getGameState());
        showMapPreview(context.getGameState().getCells());
    }

    private void configureAutoStartHook() {
        ObservableList<Player> readyPlayers = FXCollections.observableArrayList(
                player -> new Observable[] {player.isReadyProperty()}
        );

        Bindings.bindContent(readyPlayers, context.getGameState().getPlayers());

        startGameBinding = Bindings.createBooleanBinding(
                () -> readyPlayers.stream().filter(Player::getIsReady).count() == gameProvider.get().getNeededPlayer(),
                readyPlayers
        );

        if (startGameBinding.get()) {
            mayStartGame();
        } else {
            startGameBinding.addListener((observable, oldValue, newValue) -> {
                if (newValue) {
                    mayStartGame();
                }
            });
        }
    }

    private void mayStartGame() {
        if (context.getGameData().getCreator() == context.getUser()) {
            logger.debug("trigger game start of our own game");
            context.getGameEventManager().sendMessage(CommandBuilder.startGame());
        }
    }
}
