package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.Lobby.UI.ViewModels.LobbyViewModel;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Georg Siebert
 */

public class LobbyViewModelTest
{
    private LobbyViewModel lobbyViewModel;

    /*@Before
    public void initialise() {
        lobbyViewModel = new LobbyViewModel();
    }*/

    @Test
    public void testInitilizing() {
        assertEquals("Advanced WASP Wars" , lobbyViewModel.getLobbyTitle().get());
        assertNotNull(lobbyViewModel.getPlayerObservableCollection());
    }

}
