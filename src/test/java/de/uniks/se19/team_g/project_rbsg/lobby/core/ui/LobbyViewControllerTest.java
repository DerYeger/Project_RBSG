package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class LobbyViewControllerTest {

    @Test
    public void testArmyBuilderRouting()
    {
        final SceneManager sceneManager = mock(SceneManager.class);
        LobbyViewController sut = new LobbyViewController(
                mock(GameProvider.class),
                mock(UserProvider.class),
                sceneManager,
                mock(JoinGameManager.class),
                mock(PlayerManager.class),
                mock(GameManager.class),
                mock(SystemMessageManager.class),
                mock(ChatController.class),
                mock(LobbyChatClient.class),
                mock(CreateGameFormBuilder.class),
                mock(MusicManager.class),
                mock(DefaultLogoutManager.class),
                null,
                null
        );

        sut.goToArmyBuilder(null);

        verify(sceneManager).setArmyBuilderScene();
        verifyNoMoreInteractions(sceneManager);
    }

}