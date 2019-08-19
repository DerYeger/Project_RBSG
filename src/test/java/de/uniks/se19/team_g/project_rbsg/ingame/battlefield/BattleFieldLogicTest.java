package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
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
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
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
        IngameApi.class
})
public class BattleFieldLogicTest extends ApplicationTest {

    private ViewComponent<BattleFieldController> battleFieldComponent;

    @SpyBean
    SceneManager sceneManager;

    @MockBean
    AlertBuilder alertBuilder;

    @MockBean
    MenuBuilder menuBuilder;

    @MockBean
    MovementManager movementManager;

    @MockBean
    MusicManager musicManager;

    @Autowired
    ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory;

    @Override
    public void start(@NonNull final Stage ignored) {
        battleFieldComponent = battleFieldFactory.getObject();
    }

    @Test
    public void testEndRound(){

        BattleFieldController battleFieldController = Mockito.mock(BattleFieldController.class);

        TestGameBuilder.Definition definition = TestGameBuilder.sampleGameAlpha();
        Game game = definition.game;

        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        game.withPlayer(player);

        game.setPhase(Game.Phase.movePhase.name());

        IngameContext context = new IngameContext(
                new UserProvider().set(user),
                new GameProvider(),
                new IngameGameProvider()
        );

        GameEventManager gameEventManager = new GameEventManager(
                new WebSocketClient(),
                new IngameApi()
        );
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);

        battleFieldController.configure(context);

        battleFieldController.endRound();

        verify(battleFieldController, times(3)).doEndPhase();

        game.setPhase(Game.Phase.attackPhase.name());
        battleFieldController.endRound();
        verify(battleFieldController, times(2)).doEndPhase();

        game.setPhase(Game.Phase.lastMovePhase.name());
        battleFieldController.endRound();
        verify(battleFieldController, times(1)).doEndPhase();

        verifyNoMoreInteractions(battleFieldController);

    }


}
