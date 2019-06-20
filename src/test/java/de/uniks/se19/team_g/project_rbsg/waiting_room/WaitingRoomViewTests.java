package de.uniks.se19.team_g.project_rbsg.waiting_room;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.configuration.ApplicationState;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.waiting_room.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.login.*;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

/**
 * @author  Keanu Stückrad
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        SceneManager.class,
        WaitingRoomSceneBuilder.class,
        SplashImageBuilder.class,
        WaitingRoomViewBuilder.class,
        WaitingRoomViewController.class,
        UserProvider.class,
        WaitingRoomViewTests.ContextConfiguration.class,
        FXMLLoaderFactory.class,
        MusicManager.class
})
public class WaitingRoomViewTests extends ApplicationTest {

    @Autowired
    private ApplicationContext context;

    @TestConfiguration
    static class ContextConfiguration implements ApplicationContextAware {

        private ApplicationContext context;

        @Bean
        @Scope("prototype")
        public FXMLLoader fxmlLoader()
        {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setControllerFactory(this.context::getBean);
            return fxmlLoader;
        }
        @Bean
        public GameProvider gameProvider() {
            return new GameProvider() {
                @Override
                public Game get() {
                    return new Game("id", "testGame", 4, 1);
                }
            };
        }

        @Bean
        public GameEventManager gameEventManager() {
            return new GameEventManager(new WebSocketClient()) {
                @Override
                public void startSocket(@NonNull final String gameID, @NonNull final String armyID) {
                    //do nothing
                }
            };
        }

        @Bean
        public ApplicationState applicationState() {
            ApplicationState applicationState = new ApplicationState();
            applicationState.selectedArmy.set(new Army());
            return applicationState;
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
            this.context = applicationContext;
        }
    }

    @Test
    public void testBuildWaitingRoomView() throws Exception {
        final Node waitingRoomView = context.getBean(WaitingRoomViewBuilder.class).buildWaitingRoomView();
        Assert.assertNotNull(waitingRoomView);
    }
}
