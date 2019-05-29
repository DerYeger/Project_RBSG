package de.uniks.se19.team_g.project_rbsg.Lobby.Logic.Contract;

import de.uniks.se19.team_g.project_rbsg.Lobby.CrossCutting.DataClasses.Player;

import java.util.Collection;

/**
 * @author Georg Siebert
 */

public interface IPlayerManager
{
     Collection<Player> getPlayers();
}
