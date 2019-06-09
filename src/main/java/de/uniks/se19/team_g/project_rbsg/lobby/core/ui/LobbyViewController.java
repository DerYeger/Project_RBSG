package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.*;
import de.uniks.se19.team_g.project_rbsg.lobby.model.*;
import de.uniks.se19.team_g.project_rbsg.lobby.system.*;
import de.uniks.se19.team_g.project_rbsg.model.*;
import de.uniks.se19.team_g.project_rbsg.server.rest.*;
import de.uniks.se19.team_g.project_rbsg.termination.*;
import io.rincl.*;
import javafx.beans.binding.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.lang.*;
import org.springframework.stereotype.*;

import java.io.*;
import java.util.*;

/**
 * @author Georg Siebert
 */


@Component
public class LobbyViewController implements RootController, Terminable, Rincled
{
    private static final int iconSize = 30;

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final SceneManager sceneManager;
    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final JoinGameManager joinGameManager;

    private ChatBuilder chatBuilder;
    private ChatController chatController;
    private boolean musicRunning = true;
    private CreateGameFormBuilder createGameFormBuilder;

    private Node gameForm;

    public StackPane mainStackPane;
    public Button soundButton;
    public Button logoutButton;
    public Button enButton;
    public Button deButton;
    public Button createGameButton;
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
                               @NonNull final CreateGameFormBuilder createGameFormBuilder)
    {
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

        lobby.addAllPlayer(playerManager.getPlayers());
        lobby.addAllGames(gameManager.getGames());

        if(Locale.getDefault().equals(Locale.GERMAN)) {
            deButton.disableProperty().setValue(true);
        }
        else {
            deButton.disableProperty().setValue(false);
        }
        enButton.disableProperty().bind(Bindings.when(deButton.disableProperty()).then(false).otherwise(true));

        setButtonIcons(createGameButton, "baseline_add_circle_black_48dp.png" , "baseline_add_circle_white_48dp.png");
        setButtonIcons(logoutButton, "iconfinder_exit_black_2676937.png", "iconfinder_exit_white_2676937.png");

        updateMusicButtonIcons();


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

        Font.loadFont(getClass().getResource("Font/Retronoid/Retronoid.ttf").toExternalForm(), 10);
        Font.loadFont(getClass().getResource("Font/Roboto/Roboto-Regular.ttf").toExternalForm(), 16);
        Font.loadFont(getClass().getResource("Font/Cinzel/Cinzel-Regular.ttf").toExternalForm(), 28);

        updateLabels(null);

        setAsRootController();
    }

    private void updateMusicButtonIcons()
    {
        if(musicRunning) {
            setButtonIcons(soundButton, "baseline_music_note_black_48dp.png", "baseline_music_note_white_48dp.png");
            if(sceneManager.audioPlayed == false) {
                sceneManager.playAudio();
            }
        } else {
            sceneManager.stopAudio();
            setButtonIcons(soundButton, "baseline_music_off_black_48dp.png", "baseline_music_off_white_48dp.png");
        }
    }

    private void setButtonIcons(Button button, String hoverIconName, String nonHoverIconName) {
        ImageView hover = new ImageView();
        ImageView nonHover = new ImageView();

        nonHover.fitWidthProperty().setValue(iconSize);
        nonHover.fitHeightProperty().setValue(iconSize);

        hover.fitWidthProperty().setValue(iconSize);
        hover.fitHeightProperty().setValue(iconSize);

        hover.setImage(new Image(String.valueOf(getClass().getResource("Images/" + hoverIconName))));
        nonHover.setImage(new Image(String.valueOf(getClass().getResource("Images/" + nonHoverIconName))));

        button.graphicProperty().bind(Bindings.when(button.hoverProperty())
                                                    .then(hover)
                                                    .otherwise(nonHover));
    }

    private void setBackgroundImage()
    {
        Image backgroundImage = new Image(String.valueOf(getClass().getResource("splash_darker_verschwommen.jpg")),
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
            Node chatNode = null;
            try
            {
                chatNode = chatBuilder.getChat();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
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
        musicRunning = !musicRunning;
        updateMusicButtonIcons();
    }

    public void logoutUser(ActionEvent event)
    {
        logger.debug("Pressed the logout button");
    }
}
