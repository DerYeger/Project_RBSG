package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class RoundeChangeListener implements GameEventDispatcher.Listener {

    private Player firstPlayer;
    int round;

    RoundeChangeListener(){
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
        if(this.firstPlayer==null){
            this.firstPlayer=game.getCurrentPlayer();
        }

        if(game.getPhase()==null){
            return;
        }
        if(game.getPhase().equals("movePhase") && game.getCurrentPlayer().equals(firstPlayer)){
            RoundChangeAction roundChangeAction = new RoundChangeAction(round++);
            System.out.println(round);
            publishRoundChange(gameEventDispatcher);
        }

    }
}
