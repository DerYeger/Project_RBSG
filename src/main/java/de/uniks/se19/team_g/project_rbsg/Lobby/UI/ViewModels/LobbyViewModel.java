package de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.Contract.ILobbyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Georg Siebert
 */

public class LobbyViewModel implements ILobbyViewModel
{

    private StringProperty lobbyTitle;
    private ObservableList<Player> playerObservableList;

    @Override
    public StringProperty getLobbyTitle()
    {
        return lobbyTitle;
    }

    @Override
    public ObservableList<Player> getPlayerObservableCollection()
    {
        return playerObservableList;
    }

    public LobbyViewModel()
    {
        lobbyTitle = new SimpleStringProperty("Advanced WASP Wars");
        playerObservableList = FXCollections.observableArrayList();
        //Todo: GetUsersRESTManager "erzeugen"
    }

}
