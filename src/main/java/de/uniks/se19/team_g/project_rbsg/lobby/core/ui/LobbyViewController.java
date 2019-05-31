package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.*;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.ui.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.*;
import de.uniks.se19.team_g.project_rbsg.lobby.core.SystemMessageHandler.*;
import de.uniks.se19.team_g.project_rbsg.lobby.game.*;
import de.uniks.se19.team_g.project_rbsg.lobby.model.*;
import de.uniks.se19.team_g.project_rbsg.lobby.system.*;
import io.rincl.*;
import javafx.beans.binding.*;
import javafx.beans.property.*;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
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

    private ChatBuilder chatBuilder;
    private ChatController chatController;
    private boolean musicRunning = true;
    private static final int iconSize = 30;

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

    public LobbyViewController(PlayerManager playerManager, GameManager gameManager, SystemMessageManager systemMessageManager, ChatController chatController)
    {
        this.lobby = new Lobby();

        this.playerManager = playerManager;
        this.gameManager = gameManager;

        this.lobby.setSystemMessageManager(systemMessageManager);
        this.lobby.setChatController(chatController);
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
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> new GameListViewCell());

        configureSystemMessageManager();

        lobby.addAllPlayer(playerManager.getPlayers());
        lobby.addAllGames(gameManager.getGames());

        deButton.disableProperty().setValue(true);
        enButton.disableProperty().bind(Bindings.when(deButton.disableProperty()).then(false).otherwise(true));

        setButtonIcons(createGameButton, "baseline_add_circle_black_48dp.png" , "baseline_add_circle_white_48dp.png");
        setButtonIcons(logoutButton, "iconfinder_exit_black_2676937.png", "iconfinder_exit_white_2676937.png");

        updateMusicButtonIcons();


        //For ui Desgin
        lobby.addPlayer(new Player("Hallo1"));
        lobby.addPlayer(new Player("Hallo2"));
        lobby.addPlayer(new Player("Hallo3"));
        lobby.addPlayer(new Player("Hallo4"));
        lobby.addGame(new Game("an id", "GameOfHallo1", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo2", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo3", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo4", 4, 2));

        withChatSupport();

        setBackgroundImage();

        updateLabels();
    }

    private void updateMusicButtonIcons()
    {
        if(musicRunning) {
            setButtonIcons(soundButton, "baseline_music_note_black_48dp.png", "baseline_music_note_white_48dp.png");
        } else {
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
        //TODO: Create a game
        logger.debug("Creat Game button Clicked");
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
        if(Locale.getDefault().equals(Locale.GERMAN) || Locale.getDefault().equals(Locale.GERMANY)) {
            return;
        }
        deButton.disableProperty().setValue(true);
        Rincl.setLocale(Locale.GERMAN);
        updateLabels();
    }

    public void changeLangToEN(ActionEvent event)
    {
        if(Locale.getDefault().equals(Locale.ENGLISH) || Locale.getDefault().equals(Locale.US)) {
            return;
        }
        deButton.disableProperty().setValue(false);
        Rincl.setLocale(Locale.ENGLISH);
        updateLabels();
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
