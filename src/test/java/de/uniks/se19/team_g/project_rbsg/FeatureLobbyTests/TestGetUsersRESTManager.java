package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.GetUsersRESTManager;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Georg Siebert
 */

public class TestGetUsersRESTManager
{
    @Test
    public void testRequest()
    {
        //Todo: ordentlich testen
        User user = new User("hello1", "hello1", "e1a47212-3af1-4798-8ec9-e49adfbe8b49");
        GetUsersRESTManager getUsersRESTManager = new GetUsersRESTManager(user);
        ArrayList<Player> players = new ArrayList<Player>(getUsersRESTManager.getUsers());
        for (Player player : players)
        {
            System.out.println(player.getName());
        }
    }

}
