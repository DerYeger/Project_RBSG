package de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.Views;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.CustomControls.Views.PlayerListViewCell;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.DataClasses.Player;
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

    private ListView<Player> lobbyPlayerListView;

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        lobbyTitle.textProperty().bindBidirectional(viewModel.getLobbyTitle());
        lobbyPlayerListView.setItems(viewModel.getPlayerObservableCollection());
        lobbyPlayerListView.setCellFactory(lobbyPlayerListViewListView -> new PlayerListViewCell());
    }
}
