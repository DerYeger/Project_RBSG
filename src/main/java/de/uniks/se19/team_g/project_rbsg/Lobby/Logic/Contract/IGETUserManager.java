package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract;

import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract.DataClasses.Player;

import java.util.Collection;

/**
 * @author Georg Siebert
 */

public interface IGETUserManager
{
    Collection<Player> getUsers();
}
