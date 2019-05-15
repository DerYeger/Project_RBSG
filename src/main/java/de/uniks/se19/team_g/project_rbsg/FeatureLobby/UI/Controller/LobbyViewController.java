package de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Controller;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.CustomControls.Controller.UserListViewCell;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.ILobbyViewModel;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.LobbyViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Georg Siebert
 */


public class LobbyViewController implements Initializable
{

    private static final ILobbyViewModel viewModel = new LobbyViewModel();

    @FXML
    private Label lobbyTitle;

    @FXML

    private ListView<User> lobbyUserListView;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lobbyTitle.textProperty().bindBidirectional(viewModel.getLobbyTitle());
        lobbyUserListView.setItems(viewModel.getUserObservableCollection());
        lobbyUserListView.setCellFactory(studentListView -> new UserListViewCell());
    }
}
