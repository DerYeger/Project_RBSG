package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.scene.SceneManager;
import de.uniks.se19.team_g.project_rbsg.scene.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.configuration.LocaleConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.event.IngameApi;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import de.uniks.se19.team_g.project_rbsg.overlay.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.overlay.menu.MenuBuilder;
import de.uniks.se19.team_g.project_rbsg.server.websocket.WebSocketClient;
import de.uniks.se19.team_g.project_rbsg.skynet.action.ActionExecutor;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.testfx.framework.junit.ApplicationTest;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {
        FXMLLoaderFactory.class,
        BattleFieldController.class,
        IngameConfig.class,
        ChatBuilder.class,
        ChatController.class,
        ChatTabManager.class,
        UserProvider.class,
        ChatCommandManager.class,
        GameEventManager.class,
        LocaleConfig.class,
        IngameApi.class,
        BattleFieldLogicTest.ContextConfiguration.class
})
public class BattleFieldLogicTest extends ApplicationTest {

    private ViewComponent<BattleFieldController> battleFieldComponent;

    @SpyBean
    SceneManager sceneManager;

    @MockBean
    AlertBuilder alertBuilder;

    @MockBean
    ActionExecutor actionExecutor;

    @MockBean
    MenuBuilder menuBuilder;

    @MockBean
    MovementManager movementManager;

    @MockBean
    MusicManager musicManager;

    @Autowired
    ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory;

    @TestConfiguration
    static class ContextConfiguration {
        @Bean
        public GameProvider gameProvider() {
            final de.uniks.se19.team_g.project_rbsg.model.Game defaultGame = new de.uniks.se19.team_g.project_rbsg.model.Game("id", "testGame", 2, 1);
            final GameProvider gameProvider = new GameProvider();
            gameProvider.set(defaultGame);
            return gameProvider;
        }
    }

    @Override
    public void start(@NonNull final Stage ignored) {
        battleFieldComponent = battleFieldFactory.getObject();
    }

    @Test
    public void testEndRound(){

        BattleFieldController battleFieldController = battleFieldComponent.getController();

        IngameApi ingameApi = Mockito.mock(IngameApi.class);

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        game.withPlayer(player);

        game.setPhase(Game.Phase.movePhase.name());
        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(user, null);

        GameEventManager gameEventManager = new GameEventManager(
                new WebSocketClient(),
                ingameApi
        );
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        battleFieldController.configure(context);

        battleFieldController.doEndRound();

        verify(ingameApi, times(3)).endPhase();

        game.setPhase(Game.Phase.attackPhase.name());
        battleFieldController.doEndRound();
        verify(ingameApi, times(5)).endPhase();

        game.setPhase(Game.Phase.lastMovePhase.name());
        battleFieldController.doEndRound();
        verify(ingameApi, times(6)).endPhase();
    }
}
