package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.stereotype.Component;

@Component
public class RoundeChangeListener implements GameEventDispatcher.Listener {

    private Game game;
    private Player oldPlayer;
    int round;

    RoundeChangeListener(){
        //game.getPhase();
    }

    private void publishRoundChange(GameEventDispatcher dispatcher) {
        dispatcher.getModelManager().addAction(
                new RoundChangeAction(round)
        );
    }

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher gameEventDispatcher) {
        if(!(gameEvent instanceof  GameChangeObjectEvent)){
            return;
        }

        Object roundChangeObject = gameEventDispatcher.getModelManager().getEntityById(((GameChangeObjectEvent) gameEvent).getEntityId());

        if(!(roundChangeObject instanceof Game)){
            return;
        }
        System.out.println(roundChangeObject.toString());
        Game game = (Game) roundChangeObject;

        if(game.getPhase()==null){
            return;
        }
        if(game.getPhase().equals("movePhase") && game.getCurrentPlayer()!=oldPlayer){
            oldPlayer=game.getCurrentPlayer();
            RoundChangeAction roundChangeAction = new RoundChangeAction(round);
            publishRoundChange(gameEventDispatcher);
        }

    }
}
