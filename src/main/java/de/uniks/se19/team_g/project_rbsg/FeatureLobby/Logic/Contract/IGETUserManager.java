package de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;

import java.util.Collection;

/**
 * @author Georg Siebert
 */

public interface IGETUserManager
{
    Collection<Player> getUsers();
}
