/*package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.MusicManager;
import de.uniks.se19.team_g.project_rbsg.SceneManager;
import de.uniks.se19.team_g.project_rbsg.ViewComponent;
import de.uniks.se19.team_g.project_rbsg.alert.AlertBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ChatController;
import de.uniks.se19.team_g.project_rbsg.chat.command.ChatCommandManager;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatBuilder;
import de.uniks.se19.team_g.project_rbsg.chat.ui.ChatTabManager;
import de.uniks.se19.team_g.project_rbsg.configuration.FXMLLoaderFactory;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameConfig;
import de.uniks.se19.team_g.project_rbsg.ingame.IngameContext;
import de.uniks.se19.team_g.project_rbsg.ingame.event.GameEventManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.model.GameProvider;
import de.uniks.se19.team_g.project_rbsg.model.IngameGameProvider;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.model.UserProvider;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;


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
        TileDrawer.class,
        GameEventManager.class
})
public class GameEndTest {

    private ViewComponent<BattleFieldController> battleFieldComponent;

    @SpyBean
    SceneManager sceneManager;

    @MockBean
    AlertBuilder alertBuilder;

    @MockBean
    MovementManager movementManager;

    @MockBean
    MusicManager musicManager;

    @MockBean
    BattleFieldController battleFieldController;

    @Autowired
    ObjectFactory<ViewComponent<BattleFieldController>> battleFieldFactory;

    @Test
    public void testGameWon(){
        battleFieldComponent = battleFieldFactory.getObject();

        when(battleFieldController.configure();
        GameEventManager gameEventManager = Mockito.mock(GameEventManager.class);
        Game game = new Game("123");
        User user = new User();
        user.setName("Bob");
        Player player = new Player("Bob").setName("Bob").setColor("RED");
        Player enemy = new Player("Karl").setName("Karl").setColor("BLUE");
        game.withPlayer(player).withPlayer(enemy);

        game.setCurrentPlayer(player);

        IngameContext context = new IngameContext(
                new UserProvider().set(user),
                new GameProvider(),
                new IngameGameProvider()
        );
        context.gameInitialized(game);
        context.setGameEventManager(gameEventManager);
        battleFieldComponent.getController().configure(context);

        context.getUser().setName("Bob");

        context.getGameState().setWinner(player);

        verify(alertBuilder).priorityInformation(
                eq(AlertBuilder.Text.GAME_WON),
                any(),
                eq(player.getName()));
    }
}

 */
