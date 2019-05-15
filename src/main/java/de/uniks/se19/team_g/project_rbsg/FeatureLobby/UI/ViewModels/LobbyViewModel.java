package de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.IGETUserManager;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.GETUserManager;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.UI.ViewModels.Contract.ILobbyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.validation.constraints.NotNull;

/**
 * @author Georg Siebert
 */

public class LobbyViewModel implements ILobbyViewModel
{

    private StringProperty lobbyTitle;
    private ObservableList<Player> playerObservableList;
    private IGETUserManager getUserManager;

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
        //Todo: GETUserManager "erzeugen"
    }

}
