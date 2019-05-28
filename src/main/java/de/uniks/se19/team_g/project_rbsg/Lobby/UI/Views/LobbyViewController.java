package de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Game;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Lobby;
import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.GameManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views.GameListViewCell;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views.PlayerListViewCell;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.view.ChatBuilder;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author Georg Siebert
 */


@Component
public class LobbyViewController
{

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private ChatBuilder chatBuilder;
    private ChatController chatController;

    private static final int iconSize = 30;

    @FXML
    public Button createGameButton;
    @FXML
    public GridPane mainGridPane;
    @FXML
    private HBox headerHBox;
    @FXML
    private Label lobbyTitle;
    @FXML
    private ListView<Player> lobbyPlayerListView;
    @FXML
    private VBox gameListContainer;
    @FXML
    private ListView<Game> lobbyGamesListView;
    @FXML
    private VBox chatContainer;

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
        lobbyGamesListView.setFixedCellSize(50);
        lobbyPlayerListView.setFixedCellSize(50);

        lobbyPlayerListView.setItems(lobby.getPlayers());
        lobbyGamesListView.setItems(lobby.getGames());

        lobbyTitle.textProperty().setValue("Advanced WASP War");

        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell());
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> new GameListViewCell());

        lobby.getSystemMessageManager().startSocket();

        lobby.addAllPlayer(playerManager.getPlayers());
        lobby.addAllGames(gameManager.getGames());

        ImageView createGameNonHover = new ImageView();



        ImageView createGameHover = new ImageView();

//        createGameNonHover.getStyleClass().add("iconView");
//        createGameHover.getStyleClass().add("iconView");
        createGameHover.fitHeightProperty().setValue(iconSize);
        createGameHover.fitWidthProperty().setValue(iconSize);

        createGameNonHover.fitHeightProperty().setValue(iconSize);
        createGameNonHover.fitWidthProperty().setValue(iconSize);

        createGameNonHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_add_circle_white_48dp.png"))));
        createGameHover.setImage(new Image(String.valueOf(getClass().getResource("Images/baseline_add_circle_black_48dp.png"))));

        createGameButton.graphicProperty().bind(Bindings.when(createGameButton.hoverProperty())
                                                        .then(createGameHover)
                                                        .otherwise(createGameNonHover));

        //For UI Desgin
        lobby.addPlayer(new Player("Hallo1"));
        lobby.addPlayer(new Player("Hallo2"));
        lobby.addPlayer(new Player("Hallo3"));
        lobby.addPlayer(new Player("Hallo4"));
        lobby.addGame(new Game("an id", "GameOfHallo1", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo2", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo3", 4, 2));
        lobby.addGame(new Game("an id", "GameOfHallo4", 4, 2));

        withChatSupport();
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
}
