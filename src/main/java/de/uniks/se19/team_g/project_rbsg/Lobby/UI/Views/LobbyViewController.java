package de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.CustomControls.Views.PlayerListViewCell;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.Contract.ILobbyViewModel;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.LobbyViewModel;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.view.ChatBuilder;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
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
public class LobbyViewController implements Initializable
{

    private final ILobbyViewModel viewModel;

    @Autowired
    public void setChatBuilder(ChatBuilder chatBuilder)
    {
        this.chatBuilder = chatBuilder;
    }

    private ChatBuilder chatBuilder;
    private ChatController chatController;

    @FXML
    private Label lobbyTitle;

    @FXML
    private ListView<Player> lobbyPlayerListView;

    @FXML
    private VBox gameListContainer;

    @FXML
    private VBox chatContainer;

//    public ILobbyViewModel getViewModel() {
//        return viewModel;
//    }

    public LobbyViewController(ILobbyViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lobbyTitle.textProperty().bindBidirectional(viewModel.getLobbyTitle());
        lobbyPlayerListView.setItems(viewModel.getPlayerObservableCollection());
        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell());

        withChatSupport();
    }

    private void withChatSupport()
    {
        if(chatBuilder != null) {
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
