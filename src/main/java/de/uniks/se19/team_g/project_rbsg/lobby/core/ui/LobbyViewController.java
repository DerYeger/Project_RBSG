package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.EmailManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.NotificationModalController;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.termination.Terminable;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import io.rincl.Rincl;
import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * @author Georg Siebert
 */


@Component
@Scope("prototype")
public class LobbyViewController implements RootController, Terminable
{

    private static final int ICON_SIZE = 30;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final SceneManager sceneManager;
    private final UserProvider userProvider;
    private final LobbyChatClient lobbyChatClient;
    private final LogoutManager logoutManager;
    private final AlertBuilder alertBuilder;
    private final MenuBuilder menuBuilder;
    private final EmailManager emailManager;

    private final ObjectFactory<GameListViewCell> gameListCellFactory;
    @Nonnull
    private final Property<Locale> selectedLocale;

    private final ApplicationState appState;
    @Nullable
    private final Function<VBox, NotificationModalController> notificationRenderer;

    public VBox modal;
    public Pane modalBackground;

    public Button bugReportButton;

    private ChatBuilder chatBuilder;
    private ChatController chatController;
    private CreateGameFormBuilder createGameFormBuilder;

    private Node gameForm;

    public StackPane mainStackPane;
    public Button menuButton;
    public Button logoutButton;
    public Button createGameButton;
    public Pane createGameButtonContainer;
    public Button armyBuilderLink;
    public GridPane mainGridPane;
    public Label lobbyTitle;
    public ListView<Player> lobbyPlayerListView;
    public ListView<Game> lobbyGamesListView;
    public VBox chatContainer;


    @Autowired
    public LobbyViewController(
            @Nonnull final UserProvider userProvider,
            @Nonnull final SceneManager sceneManager,
            @Nonnull final PlayerManager playerManager,
            @Nonnull final GameManager gameManager,
            @Nonnull final SystemMessageManager systemMessageManager,
            @Nonnull final ChatController chatController,
            @Nonnull final LobbyChatClient lobbyChatClient,
            @Nonnull final CreateGameFormBuilder createGameFormBuilder,
            @Nonnull final LogoutManager logoutManager,
            @NonNull final AlertBuilder alertBuilder,
            @NonNull final MenuBuilder menuBuilder,
            @Nonnull final ObjectFactory<GameListViewCell> gameListCellFactory,
            @Nonnull final Property<Locale> selectedLocale,
            @NonNull final EmailManager emailManager,
            @Nullable final ApplicationState appState,
            @Nullable final Function<VBox, NotificationModalController> notificationRenderer
    )
    {
        this.lobbyChatClient = lobbyChatClient;
        this.logoutManager = logoutManager;
        this.alertBuilder = alertBuilder;
        this.menuBuilder = menuBuilder;
        this.gameListCellFactory = gameListCellFactory;
        this.selectedLocale = selectedLocale;
        this.appState = appState;
        this.notificationRenderer = notificationRenderer;

        this.lobby = new Lobby();

        this.playerManager = playerManager;
        this.gameManager = gameManager;

        this.createGameFormBuilder = createGameFormBuilder;

        this.lobby.setSystemMessageManager(systemMessageManager);
        this.lobby.setChatController(chatController);

        this.userProvider = userProvider;
        this.sceneManager = sceneManager;

        this.emailManager = emailManager;
    }

    public Lobby getLobby()
    {
        return this.lobby;
    }

    @Autowired
    public void setChatBuilder(@Nonnull final ChatBuilder chatBuilder)
    {
        this.chatBuilder = chatBuilder;
    }

    public void initialize() throws Exception
    {
        //Gives the cells of the ListViews a fixed height
        //Needed for cells which are empty to fit them to the height of filled cells
        lobbyGamesListView.setFixedCellSize(50);
        lobbyPlayerListView.setFixedCellSize(50);

        lobbyPlayerListView.setItems(lobby.getPlayers());
        lobbyGamesListView.setItems(lobby.getGames());

        lobbyTitle.textProperty().setValue("Advanced WASP War");

        withChatSupport();

        onLobbyOpen();

        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell(chatController, userProvider.get().getName()));
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> gameListCellFactory.getObject());

        configureSystemMessageManager();

        JavaFXUtils.setButtonIcons(
                menuButton,
                getClass().getResource("/assets/icons/navigation/menuWhite.png"),
                getClass().getResource("/assets/icons/navigation/menuBlack.png"),
                40
        );

        JavaFXUtils.setButtonIcons(
                createGameButton,
                getClass().getResource("/assets/icons/navigation/addCircleWhite.png"),
                getClass().getResource("/assets/icons/navigation/addCircleBlack.png"),
                LobbyViewController.ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                logoutButton,
                getClass().getResource("/assets/icons/navigation/exitWhite.png"),
                getClass().getResource("/assets/icons/navigation/exitBlack.png"),
                LobbyViewController.ICON_SIZE
        );
        JavaFXUtils.setButtonIcons(
                armyBuilderLink,
                getClass().getResource("/assets/icons/army/rallyTroopsWhite.png"),
                getClass().getResource("/assets/icons/army/rallyTroopsBlack.png"),
                LobbyViewController.ICON_SIZE
        );

        JavaFXUtils.setButtonIcons(
                bugReportButton,
                getClass().getResource("/assets/icons/operation/bugReportWhite.png"),
                getClass().getResource("/assets/icons/operation/bugReportBlack.png"),
                40
        );

        menuButton.setTooltip(new Tooltip("ESC/F10"));

        setBackgroundImage();

        Font.loadFont(getClass().getResource("/assets/fonts/retronoid.otf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("/assets/fonts/robotoRegular.ttf").toExternalForm(), 16);
        Font.loadFont(getClass().getResource("/assets/fonts/cinzelRegular.ttf").toExternalForm(), 28);

        bindI18n();
        updateLabels(null);

        if (appState != null)
        {
            JavaFXUtils.bindButtonDisableWithTooltip(
                    createGameButton,
                    createGameButtonContainer,
                    new SimpleStringProperty(Rincl.getResources(ProjectRbsgFXApplication.class).getString("ValidArmyRequired")),
                    appState.hasPlayableArmies
            );
        }

        if (Objects.nonNull(appState) && appState.notifications.size() > 0)
        {
            showNotifications();
        }

        mainStackPane.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.F10)) {
                showMenu(null);
            }
            mainStackPane.setFocusTraversable(true);
        });
    }

    private void showNotifications()
    {
        Objects.requireNonNull(appState);
        if (notificationRenderer == null) return;

        modalBackground.setVisible(true);
        final NotificationModalController modal = notificationRenderer.apply(this.modal);
        modal.setOnDismiss((e, c) ->
        {
            modalBackground.setVisible(false);
            appState.notifications.clear();
            this.modal.getChildren().clear();
        });
        modal.setNotifications(appState.notifications);
    }

    private void bindI18n()
    {
        armyBuilderLink.textProperty().bind(
                JavaFXUtils.bindTranslation(selectedLocale, "ArmyBuilderLink")
        );
        createGameButton.textProperty().bind(
                JavaFXUtils.bindTranslation(selectedLocale, "createGameButton")
        );
    }

    private void onLobbyOpen()
    {
        lobby.clearPlayers();
        lobby.clearGames();

        CompletableFuture.supplyAsync(playerManager::getPlayers).thenAccept(players -> Platform.runLater(() -> lobby.addAllPlayer(players)));
        CompletableFuture.supplyAsync(gameManager::getGames).thenAccept(games -> Platform.runLater(() -> lobby.addAllGames(games)));
    }

    public void setBackgroundImage()
    {

        Image backgroundImage = new Image(String.valueOf(getClass().getResource("/assets/splash.png")),
                                          ProjectRbsgFXApplication.WIDTH, ProjectRbsgFXApplication.HEIGHT, true, true);


        mainStackPane.setBackground(new Background(new BackgroundImage(backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT)));

    }

    private void configureSystemMessageManager() throws Exception
    {
        UserLeftMessageHandler userLeftMessageHandler = new UserLeftMessageHandler(this.lobby);

        UserJoinedMessageHandler userJoinedMessageHandler = new UserJoinedMessageHandler(this.lobby);

        GameCreatedMessageHandler gameCreatedMessageHandler = new GameCreatedMessageHandler(this.lobby);

        GameDeletedMessageHandler gameDeletedMessageHandler = new GameDeletedMessageHandler(this.lobby);

        PlayerJoinedAndLeftGameMessageHandler playerJoinedAndLeftGameMessageHandler = new PlayerJoinedAndLeftGameMessageHandler(this.lobby);

        lobby.getSystemMessageManager().addMessageHandler(userJoinedMessageHandler);
        lobby.getSystemMessageManager().addMessageHandler(userLeftMessageHandler);
        lobby.getSystemMessageManager().addMessageHandler(gameCreatedMessageHandler);
        lobby.getSystemMessageManager().addMessageHandler(gameDeletedMessageHandler);
        lobby.getSystemMessageManager().addMessageHandler(playerJoinedAndLeftGameMessageHandler);
        lobby.getSystemMessageManager().startSocket();
    }

    private void withChatSupport() throws Exception
    {
        if (chatBuilder != null)
        {
            final ViewComponent<ChatController> chatComponents = chatBuilder.buildChat(lobbyChatClient);
            chatContainer.getChildren().add(chatComponents.getRoot());
            chatController = chatComponents.getController();
        }
    }

    public void createGameButtonClicked()
    {
        if (mainStackPane == null)
        {
            return;
        }
        if (this.gameForm == null)
        {
            try
            {
                this.gameForm = this.createGameFormBuilder.getCreateGameForm();

                createGameFormBuilder.getCreateGameController().setLobbyViewController(this);
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        if ((this.gameForm != null) && (!mainStackPane.getChildren().contains(this.gameForm)))
        {
            mainStackPane.getChildren().add(gameForm);
        } else if ((this.gameForm != null) && (mainStackPane.getChildren().contains(this.gameForm)))
        {
            this.gameForm.setVisible(true);
        }

    }

    public void terminate()
    {
        lobbyChatClient.terminate();
        lobby.getSystemMessageManager().stopSocket();
        logger.debug("Terminated " + this);
    }

    private void updateLabels(Locale locale)
    {
        if (Locale.getDefault().equals(locale))
        {
            return;
        }
        if (locale != null)
        {
            selectedLocale.setValue(locale);
        }

        lobbyTitle.textProperty().bind(JavaFXUtils.bindTranslation(selectedLocale, "title"));

        if (createGameFormBuilder != null && createGameFormBuilder.getCreateGameController() != null)
        {
            createGameFormBuilder.getCreateGameController().updateLabels();
            createGameFormBuilder.getCreateGameController().loadingScreenFormBuilder.getLoadingScreenController().updateLabels();
        }
    }

    public void logoutUser()
    {
        alertBuilder
                .confirmation(
                        AlertBuilder.Text.LOGOUT,
                        this::handleLogout,
                        null);
    }

    private void handleLogout()
    {
        sceneManager.setScene(SceneManager.SceneIdentifier.LOGIN, false, null);
        logoutManager.logout(userProvider);
    }

    public void goToArmyBuilder(ActionEvent actionEvent)
    {
        sceneManager.setScene(SceneManager.SceneIdentifier.ARMY_BUILDER, true, SceneManager.SceneIdentifier.LOBBY);
    }

    public void showMenu(final ActionEvent actionEvent) {
        menuBuilder.lobbyMenu();
    }
    
    public void sendButReport(ActionEvent actionEvent)
    {
            emailManager.mailTo();
            if(Objects.nonNull(appState) && appState.notifications.size() > 0) {
                showNotifications();
            }
    }
}
