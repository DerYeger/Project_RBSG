package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.ProjectRbsgFXApplication;
import de.uniks.se19.team_g.project_rbsg.chat.*;
import de.uniks.se19.team_g.project_rbsg.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Lobby;
import de.uniks.se19.team_g.project_rbsg.lobby.model.Player;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.LogoutManager;
import de.uniks.se19.team_g.project_rbsg.util.JavaFXUtils;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import java.io.IOException;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import io.rincl.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import java.util.*;

/**
 * @author Georg Siebert
 */


@Component
@Scope("prototype")
public class LobbyViewController extends JavaFXUtils implements RootController, Terminable, Rincled
{

    private static final int ICON_SIZE = 30;

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SceneManager sceneManager;
    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final JoinGameManager joinGameManager;
    @NonNull
    private final LobbyChatClient lobbyChatClient;
    @NonNull
    private final MusicManager musicManager;
    private final LogoutManager logoutManager;

    private ChatBuilder chatBuilder;
    private ChatController chatController;
    private boolean musicRunning;
    private CreateGameFormBuilder createGameFormBuilder;

    private Node gameForm;

    public StackPane mainStackPane;
    public Button soundButton;
    public Button logoutButton;
    public Button enButton;
    public Button deButton;
    public Button createGameButton;
    public Button armyBuilderLink;
    public GridPane mainGridPane;
    public HBox headerHBox;
    public Label lobbyTitle;
    public ListView<Player> lobbyPlayerListView;
    public VBox gameListContainer;
    public ListView<Game> lobbyGamesListView;
    public VBox chatContainer;

    @Autowired
    public LobbyViewController(@NonNull final GameProvider gameProvider,
                               @NonNull final UserProvider userProvider,
                               @NonNull final SceneManager sceneManager,
                               @NonNull final JoinGameManager joinGameManager,
                               @NonNull final PlayerManager playerManager,
                               @NonNull final GameManager gameManager,
                               @NonNull final SystemMessageManager systemMessageManager,
                               @NonNull final ChatController chatController,
                               @NonNull final LobbyChatClient lobbyChatClient,
                               @NonNull final CreateGameFormBuilder createGameFormBuilder,
                               @NonNull final MusicManager musicManager,
                               @NonNull final LogoutManager logoutManager)
    {
        this.lobbyChatClient = lobbyChatClient;
        this.logoutManager = logoutManager;

        this.lobby = new Lobby();

        this.playerManager = playerManager;
        this.gameManager = gameManager;

        this.createGameFormBuilder = createGameFormBuilder;

        this.lobby.setSystemMessageManager(systemMessageManager);
        this.lobby.setChatController(chatController);

        this.gameProvider = gameProvider;
        this.userProvider = userProvider;
        this.sceneManager = sceneManager;
        this.joinGameManager = joinGameManager;
        this.musicManager = musicManager.init();
    }

    public Lobby getLobby()
    {
        return this.lobby;
    }

    @Autowired
    public void setChatBuilder(@NonNull final ChatBuilder chatBuilder)
    {
        this.chatBuilder = chatBuilder;
    }

    public void init()
    {
        //Gives the cells of the ListViews a fixed height
        //Needed for cells which are empty to fit them to the height of filled cells
        lobbyGamesListView.setFixedCellSize(50);
        lobbyPlayerListView.setFixedCellSize(50);

        lobbyPlayerListView.setItems(lobby.getPlayers());
        lobbyGamesListView.setItems(lobby.getGames());

        lobbyTitle.textProperty().setValue("Advanced WASP War");

        withChatSupport();

        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell(chatController, userProvider.get().getName()));
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> new GameListViewCell(gameProvider, userProvider, sceneManager, joinGameManager));

        configureSystemMessageManager();

        lobby.clearPlayers();
        lobby.addAllPlayer(playerManager.getPlayers());

        lobby.clearGames();
        lobby.addAllGames(gameManager.getGames());

        if(Locale.getDefault().equals(Locale.GERMAN)) {
            deButton.disableProperty().setValue(true);
        }
        else {
            deButton.disableProperty().setValue(false);
        }
        enButton.disableProperty().bind(Bindings.when(deButton.disableProperty()).then(false).otherwise(true));

        setButtonIcons(
            createGameButton,
            getClass().getResource("/assets/icons/navigation/add-circle-white.png"),
            getClass().getResource("/assets/icons/navigation/add-circle-black.png"),
            LobbyViewController.ICON_SIZE
        );
        setButtonIcons(
            logoutButton,
            getClass().getResource("/assets/icons/navigation/exit-white.png"),
            getClass().getResource("/assets/icons/navigation/exit-black.png"),
            LobbyViewController.ICON_SIZE
        );
        setButtonIcons(
            armyBuilderLink,
            getClass().getResource("/assets/icons/army/rally-the-troops_dark_background.png"),
            getClass().getResource("/assets/icons/army/rally-the-troops_light_background.png"),
            LobbyViewController.ICON_SIZE
        );

        musicManager.initButtonIcons(soundButton);


        //For UI/UX Design
//        lobby.addPlayer(new Player("Hallo1"));
//        lobby.addPlayer(new Player("Hallo2"));
//        lobby.addPlayer(new Player("Hallo3"));
//        lobby.addPlayer(new Player("Hallo4"));
//        lobby.addPlayer(new Player("Hallo5"));
//        lobby.addPlayer(new Player("Hallo6"));
//        lobby.addPlayer(new Player("Hallo7"));
//        lobby.addPlayer(new Player("Hallo8"));
//        lobby.addPlayer(new Player("Hallo9"));
//        lobby.addPlayer(new Player("Hallo10"));
//        lobby.addPlayer(new Player("Hallo11"));
//        lobby.addPlayer(new Player("Hallo12"));
//
//        lobby.addGame(new Game("an id", "GameOfHallo1", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo2", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo3", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo4", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo5", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo6", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo7", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo8", 4, 2));

        setBackgroundImage();

        Font.loadFont(getClass().getResource("/assets/fonts/retronoid.otf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("/assets/fonts/roboto-regular.ttf").toExternalForm(), 16);
        Font.loadFont(getClass().getResource("/assets/fonts/cinzel-regular.ttf").toExternalForm(), 28);

        updateLabels(null);

        setAsRootController();
    }

    private void setBackgroundImage()
    {
        Image backgroundImage = new Image(String.valueOf(getClass().getResource("/assets/splash.jpg")),
                                          ProjectRbsgFXApplication.WIDTH, ProjectRbsgFXApplication.HEIGHT, true, true);

        mainStackPane.setBackground(new Background(new BackgroundImage(backgroundImage,
                                                                   BackgroundRepeat.NO_REPEAT,
                                                                   BackgroundRepeat.NO_REPEAT,
                                                                   BackgroundPosition.CENTER,
                                                                   BackgroundSize.DEFAULT)));

    }

    private void configureSystemMessageManager()
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

    private void withChatSupport()
    {
        if (chatBuilder != null)
        {
            final Node chatNode = chatBuilder.buildChat(lobbyChatClient);
            chatContainer.getChildren().add(chatNode);
            chatController = chatBuilder.getChatController();
        }
    }

    public void createGameButtonClicked(ActionEvent event)
    {
        if(mainStackPane == null) {
            return;
        }
        if (this.gameForm == null) {
            try {
                this.gameForm = this.createGameFormBuilder.getCreateGameForm();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ((this.gameForm != null) && (!mainStackPane.getChildren().contains(this.gameForm))) {
            mainStackPane.getChildren().add(gameForm);
        } else if ((this.gameForm != null) && (mainStackPane.getChildren().contains(this.gameForm))){
            this.gameForm.setVisible(true);
        }

    }

    public void terminate()
    {
        chatController.terminate();
        lobby.getSystemMessageManager().stopSocket();
        logger.debug("Terminated " + this);
    }

    public void setAsRootController() {
        sceneManager.setRootController(this);
    }

    private void updateLabels(Locale locale)
    {
        if(Locale.getDefault().equals(locale))
        {
            return;
        }
        if(locale != null) {
            Rincl.setLocale(locale);
        }

        createGameButton.textProperty().setValue(getResources().getString("createGameButton"));
        enButton.textProperty().setValue(getResources().getString("enButton"));
        deButton.textProperty().setValue(getResources().getString("deButton"));
        lobbyTitle.textProperty().setValue(getResources().getString("title"));

        if(createGameFormBuilder != null && createGameFormBuilder.getCreateGameController() != null) {
            createGameFormBuilder.getCreateGameController().updateLabels();
        }
    }

    public void changeLangToDE(ActionEvent event)
    {
        deButton.disableProperty().setValue(true);
        updateLabels(Locale.GERMAN);
    }

    public void changeLangToEN(ActionEvent event)
    {
        deButton.disableProperty().setValue(false);
        updateLabels(Locale.ENGLISH);
    }

    public void toggleSound(ActionEvent event)
    {
        logger.debug("Pressed the toggleSound button");
        musicManager.updateMusicButtonIcons(soundButton);
    }

    public void logoutUser(ActionEvent event)
    {
        logoutManager.logout(userProvider);
        sceneManager.setStartScene();
    }

    public void goToArmyBuilder(ActionEvent actionEvent) {
        sceneManager.setArmyBuilderScene();
    }
}
