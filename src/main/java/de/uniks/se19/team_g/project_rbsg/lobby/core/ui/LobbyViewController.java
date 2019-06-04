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
import io.rincl.*;
import javafx.beans.binding.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
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
public class LobbyViewController implements Rincled
{

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @FXML
    public StackPane lobbyView;

    private ChatBuilder chatBuilder;
    private ChatController chatController;
    private CreateGameFormBuilder createGameFormBuilder;

    private Node gameForm;

    private static final int iconSize = 30;

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

    private final GameProvider gameProvider;
    private final UserProvider userProvider;
    private final SceneManager sceneManager;
    private final JoinGameManager joinGameManager;

    @Autowired
    public LobbyViewController(@NonNull final GameProvider gameProvider, @NonNull final UserProvider userProvider, @NonNull final SceneManager sceneManager, @NonNull final JoinGameManager joinGameManager, @NonNull final PlayerManager playerManager, @NonNull final GameManager gameManager, @NonNull final SystemMessageManager systemMessageManager, @NonNull final ChatController chatController, @NonNull final CreateGameFormBuilder createGameFormBuilder)
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
    public void setChatBuilder(ChatBuilder chatBuilder)
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

        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell());
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> new GameListViewCell(gameProvider, userProvider, sceneManager, joinGameManager));

        configureSystemMessageManager();

        lobby.addAllPlayer(playerManager.getPlayers());
        lobby.addAllGames(gameManager.getGames());




        setCreateGameIcons();

        //For ui Desgin
//        lobby.addPlayer(new Player("Hallo1"));
//        lobby.addPlayer(new Player("Hallo2"));
//        lobby.addPlayer(new Player("Hallo3"));
//        lobby.addPlayer(new Player("Hallo4"));
//        lobby.addGame(new Game("an id", "GameOfHallo1", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo2", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo3", 4, 2));
//        lobby.addGame(new Game("an id", "GameOfHallo4", 4, 2));

        withChatSupport();

        updateLabels();
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

    private void setCreateGameIcons()
    {
        Rincl.setLocale(Locale.ENGLISH);
        logger.debug(getResources().getString("createGameButton"));
        ImageView createGameNonHover = new ImageView();
        ImageView createGameHover = new ImageView();

        createGameHover.fitHeightProperty().setValue(iconSize);
        createGameHover.fitWidthProperty().setValue(iconSize);

        createGameNonHover.fitHeightProperty().setValue(iconSize);
        createGameNonHover.fitWidthProperty().setValue(iconSize);

        createGameNonHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_add_circle_white_48dp.png"))));
        createGameHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_add_circle_black_48dp.png"))));

        createGameButton.graphicProperty().bind(Bindings.when(createGameButton.hoverProperty())
                                                        .then(createGameHover)
                                                        .otherwise(createGameNonHover));
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
        if (this.gameForm == null) {
            try {
                this.gameForm = this.createGameFormBuilder.getCreateGameForm();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if ((this.gameForm != null) && (!lobbyView.getChildren().contains(this.gameForm))) {
            lobbyView.getChildren().add(gameForm);
        } else if ((this.gameForm != null) && (lobbyView.getChildren().contains(this.gameForm))){
            this.gameForm.setVisible(true);
        }

    }

    public void terminate()
    {
        lobby.getSystemMessageManager().stopSocket();
    }

    private void updateLabels()
    {
        createGameButton.textProperty().setValue(getResources().getString("createGameButton"));
        enButton.textProperty().setValue(getResources().getString("enButton"));
        deButton.textProperty().setValue(getResources().getString("deButton"));
        lobbyTitle.textProperty().setValue(getResources().getString("title"));
    }

    public void changeLangToDE(ActionEvent event)
    {
        Rincl.setLocale(Locale.GERMAN);
        logger.debug("Changed language to " + Locale.GERMAN.toString());
        updateLabels();
    }

    public void changeLangToEN(ActionEvent event)
    {
        logger.debug("Changed language to " + Locale.GERMAN.toString());
        Rincl.setLocale(Locale.ENGLISH);
        updateLabels();
    }

    public void toggleSound(ActionEvent event)
    {
    }

    public void loggoutUser(ActionEvent event)
    {
    }
}
