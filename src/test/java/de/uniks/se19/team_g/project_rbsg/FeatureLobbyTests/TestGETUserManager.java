package de.uniks.se19.team_g.project_rbsg.FeatureLobbyTests;

import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.Player;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.Contract.DataClasses.User;
import de.uniks.se19.team_g.project_rbsg.FeatureLobby.Logic.GETUserManager;
import org.junit.Test;

import java.util.ArrayList;

/**
 * @author Georg Siebert
 */

public class TestGETUserManager
{
    @Test
    public void testRequest()
    {
        //Todo: ordentlich testen
        User user = new User("hello1", "hello1", "e1a47212-3af1-4798-8ec9-e49adfbe8b49");
        GETUserManager getUserManager = new GETUserManager(user);
        ArrayList<Player> players = new ArrayList<Player>(getUserManager.getUsers());
        for (Player player : players)
        {
            System.out.println(player.getName());
        }
    }

}
