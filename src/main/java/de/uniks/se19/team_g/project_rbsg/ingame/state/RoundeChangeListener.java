package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import org.springframework.stereotype.Component;

@Component
public class RoundeChangeListener implements GameEventDispatcher.Listener {

    private Game game;

    RoundeChangeListener(){
        game.getPhase();
    }

    private void publishRoundChange(GameEventDispatcher dispatcher) {
        dispatcher.getModelManager().addAction(
                new RoundChangeAction()
        );
    }

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher gameEventDispatcher) {
        if(!(gameEvent instanceof  GameChangeObjectEvent)){
            return
        }
    }
}
