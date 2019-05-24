package de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.Contract;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

/**
 * @author Georg Siebert
 */

public interface ILobbyViewModel
{
    StringProperty getLobbyTitle();

    ObservableList<Player> getPlayerObservableCollection();
}
