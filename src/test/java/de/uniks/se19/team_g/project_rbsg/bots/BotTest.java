package de.uniks.se19.team_g.project_rbsg.bots;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import de.uniks.se19.team_g.project_rbsg.configuration.army.ArmyGeneratorStrategy;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.Army;
import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.server.rest.JoinGameManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.CreateArmyService;
import de.uniks.se19.team_g.project_rbsg.server.rest.army.persistance.serverResponses.SaveArmyResponse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.config.CustomScopeConfigurer;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.Assert.assertEquals;
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

    @MockBean
    CreateArmyService createArmyService;

    @MockBean
    IngameApi ingameApi;

    @Test
    public void start() throws ExecutionException, InterruptedException, TimeoutException, IOException {
        Game gameData = new Game("game", 4);
        User user = new User().setName("karli");
        Player player = new Player("columnPlayer");
        player.setName(user.getName());
        de.uniks.se19.team_g.project_rbsg.ingame.model.Game gameState = new de.uniks.se19.team_g.project_rbsg.ingame.model.Game("game");

        gameState.withPlayers(player);

        Bot sut = new Bot(
                userProvider,
                joinGameManager,
                contextFactory,
                createArmyService
        );

        when(gameEventManager.api()).thenReturn(ingameApi);

        Army army = new Army();
        ArmyGeneratorStrategy armyGeneratorStrategy = mock(ArmyGeneratorStrategy.class);
        when(armyGeneratorStrategy.createArmy(null)).thenReturn(army);
        sut.setArmyGeneratorStrategy( armyGeneratorStrategy);

        SaveArmyResponse response = new SaveArmyResponse();
        response.data = new SaveArmyResponse.SaveArmyResponseData();
        response.data.id = "army";
        when(createArmyService.createArmy(army)).thenReturn(response);

        IngameContext ingameContext = new IngameContext(user, gameData);
        ingameContext.setModelManager(modelManager);
        ingameContext.setGameEventManager(gameEventManager);
        when(contextFactory.getObject(user, gameData)).thenReturn(ingameContext);

        sut.start(gameData, user);


        try {
            sut.getBootPromise().get(10, TimeUnit.SECONDS);
        } catch (Exception e){
            sut.shutdown();
            throw e;
        }

        // should be called when boot finished
        verify(contextFactory).getObject(user, gameData);
        verify(armyGeneratorStrategy).createArmy(null);
        verify(createArmyService).createArmy(army);
        assertEquals("army", army.id.get());
        verify(ingameApi).selectArmy(army);

        ingameContext.gameInitialized(gameState);

        ObjectNode event = new ObjectMapper().readValue(String.format("{\"action\": \"%s\"}", GameEventManager.GAME_STARTS), ObjectNode.class);
        sut.listenOnGameEventsForStart(event);

        Thread.sleep(500);

        sut.getExecutor().execute(
            () -> gameState.setWinner(player)
        );

        sut.getShutdownPromise().get(300, TimeUnit.MILLISECONDS);

        sut.join();
    }
}