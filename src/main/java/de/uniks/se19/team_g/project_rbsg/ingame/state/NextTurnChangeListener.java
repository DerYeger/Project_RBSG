package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.stereotype.Component;

@Component
public class NextTurnChangeListener implements GameEventDispatcher.Listener {

    private Player lastPlayer;

    NextTurnChangeListener(){
    }

    private void publishNextTurnChange(GameEventDispatcher dispatcher, Player player) {
        dispatcher.getModelManager().addAction(
                new NextTurnAction(player.getCurrentGame().getTurnCounter(), player)
        );
    }

    @Override
    public void accept(GameEvent gameEvent, GameEventDispatcher gameEventDispatcher) {

        if(!(gameEvent instanceof  GameChangeObjectEvent)){
            return;
        }

        Object roundChangeObject = gameEventDispatcher.getModelManager().getEntityById(((GameChangeObjectEvent) gameEvent).getEntityId());
        Object maybeAPlayer = gameEventDispatcher.getModelManager().getEntityById(((GameChangeObjectEvent) gameEvent).getNewValue());

        if(!(roundChangeObject instanceof Game) && !(maybeAPlayer instanceof Player)){
            return;
        }
        Game game = (Game) roundChangeObject;
        Player newPlayer = (Player) maybeAPlayer;

        if(this.lastPlayer==null){
            this.lastPlayer=newPlayer;
        }

        if(game.getPhase()==null){
            return;
        }
        if(game.getPhase()=="movePhase"){
            if(!newPlayer.equals(lastPlayer)){
                return;
            }
            publishNextTurnChange(gameEventDispatcher, newPlayer);
            lastPlayer=newPlayer;
        }
    }
}
