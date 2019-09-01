package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.ModelManager;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NextTurnListenerTest {

    @Test
    public void accept() {
        NextTurnListener sut = new NextTurnListener();

        Game game = new Game();
        Player player = new Player("player");
        GameChangeObjectEvent nextPlayerEvent = new GameChangeObjectEvent("game", "currentPlayer", "player");
        GameChangeObjectEvent firstMovePhaseEvent = new GameChangeObjectEvent("game", "phase", Game.Phase.movePhase.name());
        GameChangeObjectEvent attackPhaseEvent = new GameChangeObjectEvent("game", "phase", Game.Phase.attackPhase.name());

        ModelManager modelManager = mock(ModelManager.class);
        GameEventDispatcher dispatcher = mock(GameEventDispatcher.class);

        when(dispatcher.getModelManager()).thenReturn(modelManager);
        when(modelManager.getGame()).thenReturn(game);
        when(modelManager.getEntityById("game")).thenReturn(game);
        when(modelManager.getEntityById("player")).thenReturn(player);

        game.setTurnCount(0);
        sut.accept(nextPlayerEvent, dispatcher);
        verify(modelManager, times(0)).addAction(any());
        sut.accept(attackPhaseEvent, dispatcher);
        verify(modelManager, times(0)).addAction(any());
        sut.accept(firstMovePhaseEvent, dispatcher);
        verify(modelManager).addAction(any(NextTurnAction.class));
    }
}