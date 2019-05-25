package de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.Contract.ILobbyViewModel;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.springframework.stereotype.Component;

/**
 * @author Georg Siebert
 */

@Component
public class LobbyViewModel implements ILobbyViewModel
{

    private final StringProperty lobbyTitle;
    private final ObservableList<Player> playerObservableList;
    private final SystemMessageManager systemMessageManager;
    private final PlayerManager playerManager;

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

    public LobbyViewModel(SystemMessageManager systemMessageManager, PlayerManager playerManager)
    {
        lobbyTitle = new SimpleStringProperty("Advanced WASP Wars");
        playerObservableList = FXCollections.observableArrayList();

        this.systemMessageManager = systemMessageManager;
        this.playerManager = playerManager;

        playerObservableList.addAll(playerManager.getPlayers());
    }

}
