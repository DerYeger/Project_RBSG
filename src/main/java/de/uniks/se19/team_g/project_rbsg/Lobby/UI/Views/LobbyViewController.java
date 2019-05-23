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
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Georg Siebert
 */


@Component
public class LobbyViewController
{

    private final Lobby lobby;
    private final PlayerManager playerManager;
    private final GameManager gameManager;

    private ChatBuilder chatBuilder;
    private ChatController chatController;

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

    public LobbyViewController(PlayerManager playerManager, GameManager gameManager, SystemMessageManager systemMessageManager)
    {
        this.lobby = new Lobby();

        this.playerManager = playerManager;
        this.gameManager = gameManager;

        this.lobby.setSystemMessageManager(systemMessageManager);
    }

    @Autowired
    public void setChatBuilder(ChatBuilder chatBuilder)
    {
        this.chatBuilder = chatBuilder;
    }

    public void init()
    {
        lobbyPlayerListView.setItems(lobby.getPlayers());
        lobbyGamesListView.setItems(lobby.getGames());

        lobbyTitle.textProperty().setValue("Advanced WASP War");

        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell());
        lobbyGamesListView.setCellFactory(lobbyGamesListView -> new GameListViewCell());

        lobby.getSystemMessageManager().startSocket();

        lobby.getPlayers().addAll(playerManager.getPlayers());
        lobby.getGames().addAll(gameManager.getGames());

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
}
