package de.uniks.se19.team_g.project_rbsg.LobbyTests;

import de.uniks.se19.team_g.project_rbsg.JavaConfig;
import de.uniks.se19.team_g.project_rbsg.Lobby.Logic.*;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewBuilder;
import de.uniks.se19.team_g.project_rbsg.Lobby.UI.Views.LobbyViewController;
import de.uniks.se19.team_g.project_rbsg.controller.ChatController;
import de.uniks.se19.team_g.project_rbsg.view.ChatBuilder;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.testfx.framework.junit.ApplicationTest;

import static org.junit.Assert.assertNotNull;

/**
 * @author Georg Siebert
 */


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        JavaConfig.class,
        LobbyViewBuilder.class,
        LobbyBuilderTest.ContextConfiguration.class,
        ChatBuilder.class,
        ChatController.class
})
public class LobbyBuilderTest extends ApplicationTest
{

    @Autowired
    private ApplicationContext context;

    private Node lobbyView;

    @Override
    public void start(@NonNull final Stage stage) {
        LobbyViewBuilder lobbyViewBuilder = context.getBean(LobbyViewBuilder.class);
        lobbyView = lobbyViewBuilder.buildLobbyScene();

        final Scene scene = new Scene((Parent) lobbyView);
        stage.setScene(scene);
        stage.show();
        stage.toFront();
    }

    @Test
    public void testGetLobbyView()
    {
        assertNotNull(lobbyView);
        assertNotNull(lookup("#lobbyTitle").query());
        assertNotNull(lookup("#chatContainer").query());
        assertNotNull(lookup("#gameListContainer").query());
        assertNotNull(lookup("#lobbyPlayerListView").query());

    }

    @TestConfiguration
    static class ContextConfiguration
    {
        @Bean
        public LobbyViewController lobbyViewController() {
            return new LobbyViewController(new PlayerManager(new RESTClient(new RestTemplate())),
                                           new GameManager(new RESTClient(new RestTemplate())),
                                           new SystemMessageManager(new WebSocketFactory())) {
                @Override
                public void init() {

                }
            };
        }
    }
}
