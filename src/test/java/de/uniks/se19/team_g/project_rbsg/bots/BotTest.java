package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        Bot.class,
        UserProvider.class,
        BotTest.Config.class
})
public class BotTest {

    @TestConfiguration
    public static class Config {
        @Bean
        public CustomScopeConfigurer customScopeConfigurer() {
            CustomScopeConfigurer configurer = new CustomScopeConfigurer();
            configurer.addScope(UserScope.SCOPE_NAME, new UserScope());

            return configurer;
        }
    }

    @MockBean
    UserProvider userProvider;

    @MockBean
    JoinGameManager joinGameManager;

    @MockBean
    ObjectProvider<IngameContext> contextFactory;

    @MockBean
    GameEventManager gameEventManager;

    @MockBean
    ModelManager modelManager;

    @Test
    public void start() throws ExecutionException, InterruptedException, TimeoutException {
        Game gameData = new Game("game", 4);
        User user = new User();

        Bot sut = new Bot(userProvider, joinGameManager, contextFactory);

        IngameContext ingameContext = new IngameContext(user, gameData);
        ingameContext.setModelManager(modelManager);
        ingameContext.setGameEventManager(gameEventManager);
        when(contextFactory.getObject(user, gameData)).thenReturn(ingameContext);

        sut.start(gameData, user);

        try {
            sut.getBootPromise().get(10, TimeUnit.SECONDS);
        } finally {
            sut.shutdown();
        }
        sut.join();
    }
}