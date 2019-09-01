package de.uniks.se19.team_g.project_rbsg.bots;

import de.uniks.se19.team_g.project_rbsg.model.Game;
import de.uniks.se19.team_g.project_rbsg.model.User;
import de.uniks.se19.team_g.project_rbsg.server.rest.LoginManager;
import de.uniks.se19.team_g.project_rbsg.server.rest.user.GetTempUserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InOrder;
import org.mockito.internal.stubbing.answers.ReturnsArgumentAt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {
        BotManager.class
})
public class BotManagerTest {

    @MockBean
    Bot bot;

    @MockBean
    GetTempUserService getTempUserService;

    @MockBean
    LoginManager loginManager;

    @Autowired
    BotManager sut;

    @Test
    public void requestBot() throws ExecutionException, InterruptedException {
        Game gameData = new Game("game", 4);
        User user = new User("Kermit the Bot", "Kermit the Bot");

        when(getTempUserService.get()).thenReturn(user);
        when(loginManager.login(user)).thenAnswer(new ReturnsArgumentAt(0));
        when(bot.start(gameData, user)).thenReturn(bot);
        when(bot.getBootPromise()).thenReturn(CompletableFuture.completedFuture(bot));

        assertEquals(0, sut.getBots().size());
        Bot response = sut.requestBot(gameData).get();
        assertSame(this.bot, response);
        assertEquals(1, sut.getBots().size());

        InOrder inOrder = inOrder(getTempUserService, loginManager, bot);

        inOrder.verify(getTempUserService).get();
        inOrder.verify(loginManager).login(user);
        inOrder.verify(bot).start(gameData, user);
        //noinspection ResultOfMethodCallIgnored
        inOrder.verify(bot).getBootPromise();
        inOrder.verifyNoMoreInteractions();

        sut.shutdown();
        verify(bot).shutdown();
    }

}