package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.stereotype.Component;

@Component
public class NextTurnChangeListener implements GameEventDispatcher.Listener {

    private Player lastPlayer;
    private boolean movePhaseFlag;
    private boolean newPlayerFlag;
    private Game game;

    NextTurnChangeListener(){
        movePhaseFlag=false;
        lastPlayer=null;
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

        if(roundChangeObject instanceof Game){
            this.game = (Game) roundChangeObject;
            movePhaseFlag = isMovePhase(game) ? true : false;
        }

        if(maybeAPlayer instanceof Player){
            Player newPlayer = (Player) maybeAPlayer;
            newPlayerFlag = isNewPlayer(newPlayer) ? true : false;
            lastPlayer=newPlayer;
        }

        if(newPlayerFlag==true && movePhaseFlag == true){
            publishNextTurnChange(gameEventDispatcher, game.getCurrentPlayer());
        }
    }

    private boolean isNewPlayer(Player player){
        if(lastPlayer==null){
            lastPlayer=player;
            return true;
        }

        if(!player.equals(lastPlayer)){
            return true;
        }
        return false;
    }
    private boolean isMovePhase(Game game){
        if(game.getPhase() ==null){
           return false;
        }
        if(game.getPhase().equals("movePhase")){
            return true;
        }
        return false;
    }
}
