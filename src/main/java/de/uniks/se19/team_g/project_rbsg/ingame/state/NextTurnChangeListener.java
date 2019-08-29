package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class NextTurnChangeListener implements GameEventDispatcher.Listener {

    private Player firstPlayer;

    NextTurnChangeListener(){
    }

    private void publishNextTurnChange(GameEventDispatcher dispatcher, Player player) {
        dispatcher.getModelManager().addAction(
                new NextTurnAction(player.getCurrentGame().getRoundCounter(), player)
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
        Game game = (Game) roundChangeObject;
        Player player = game.getCurrentPlayer();
        if(this.firstPlayer==null){
            this.firstPlayer=player;
        }

        if(game.getPhase()==null){
            return;
        }
        if(game.getPhase().equals("movePhase")){
            if(player.equals(firstPlayer)){
                game.setRoundCounter(game.getRoundCounter()+1);
            }
            publishNextTurnChange(gameEventDispatcher, player);
        }
    }
}
