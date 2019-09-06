package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.scene.SceneConfiguration;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;

import static de.uniks.se19.team_g.project_rbsg.scene.SceneManager.SceneIdentifier.*;
import static org.mockito.Mockito.*;

public class LobbyViewControllerTest {

    @Test
    public void testArmyBuilderRouting()
    {
        final SceneManager sceneManager = mock(SceneManager.class);
        @SuppressWarnings("unchecked") final ObjectFactory<GameListViewCell> mock = mock(ObjectFactory.class);
        LobbyViewController sut = new LobbyViewController(
                mock(UserProvider.class),
                sceneManager,
                mock(PlayerManager.class),
                mock(GameManager.class),
                mock(SystemMessageManager.class),
                mock(ChatController.class),
                mock(LobbyChatClient.class),
                mock(CreateGameFormBuilder.class),
                mock(DefaultLogoutManager.class),
                mock(AlertBuilder.class),
                mock(MenuBuilder.class),
                null,
                null,
                null,
                null,
                null
        );

        sut.goToArmyBuilder(null);

        verify(sceneManager).setScene(eq(SceneConfiguration.of(ARMY_BUILDER).andCache(LOBBY)));
        verifyNoMoreInteractions(sceneManager);
    }

}