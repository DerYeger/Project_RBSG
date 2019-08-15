package de.uniks.se19.team_g.project_rbsg.lobby.core.ui;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.lobby.chat.LobbyChatClient;
import de.uniks.se19.team_g.project_rbsg.lobby.core.PlayerManager;
import de.uniks.se19.team_g.project_rbsg.lobby.credits.CreditsFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.CreateGameFormBuilder;
import de.uniks.se19.team_g.project_rbsg.lobby.game.GameManager;
import de.uniks.se19.team_g.project_rbsg.lobby.system.SystemMessageManager;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.DefaultLogoutManager;
import org.junit.Test;
import org.springframework.beans.factory.ObjectFactory;

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
                mock(CreditsFormBuilder.class),
                mock(MusicManager.class),
                mock(DefaultLogoutManager.class),
                mock(AlertBuilder.class),
                mock,
                null,
                null,
                null
        );

        sut.goToArmyBuilder(null);

        verify(sceneManager).setScene(SceneManager.SceneIdentifier.ARMY_BUILDER, true, SceneManager.SceneIdentifier.LOBBY);
        verifyNoMoreInteractions(sceneManager);
    }

}