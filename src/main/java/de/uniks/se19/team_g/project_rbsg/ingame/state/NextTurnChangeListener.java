package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class NextTurnChangeListener implements GameEventDispatcher.Listener {

    private Player lastPlayer;
    private boolean newPlayerFlag;
    private Game game;
    private boolean isFirstPlayer;
    private ArrayList<Player> donePlayers;

    NextTurnChangeListener(){
        lastPlayer=null;
        isFirstPlayer=false;
        donePlayers = new ArrayList<>();
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

        Object mayBeAGame = gameEventDispatcher.getModelManager().getEntityById(((GameChangeObjectEvent) gameEvent).getEntityId());
        Object maybeAPlayer = gameEventDispatcher.getModelManager().getEntityById(((GameChangeObjectEvent) gameEvent).getNewValue());
        String gamePhase;
        Player player;

        if(!(mayBeAGame instanceof Game)){
            if(this.game!=null){
                gamePhase=this.game.getPhase();
                player= game.getCurrentPlayer();
            }
            else {
                return;
            }
        }
        else{
            this.game = (Game) mayBeAGame;
            gamePhase=this.game.getPhase();
            player = this.game.getCurrentPlayer();
        }

        if(game.getTurnCounter().get() == 0 && donePlayers.size()==0 && player!=null){
            donePlayers.add(player);
        }

        if(gamePhase==null){
            return;
        }
        if(game.getPhase().equals("movePhase") && !donePlayers.contains(player)){
            System.out.println(game.getPhase());
            publishNextTurnChange(gameEventDispatcher, player);
        }
        if(game.getPlayers().size()==donePlayers.size()){
            donePlayers.clear();
        }

        /*
        if(!(maybeAPlayer instanceof Player) && lastPlayer==null){
            return;
        }
        else{
            if((maybeAPlayer instanceof Player)){
                Player candidate = (Player) maybeAPlayer;
                newPlayerFlag = isNewPlayer(candidate);

                if(lastPlayer==null && !isFirstPlayer){
                    lastPlayer = candidate;
                    newPlayerFlag=false;
                    isFirstPlayer = true;
                }
                if(newPlayerFlag){
                    lastPlayer = candidate;
                    isFirstPlayer=false;
                }
            }
        }
        if(gamePhase==null){
            return;
        }

        if(gamePhase.equals("movePhase") && newPlayerFlag){
            publishNextTurnChange(gameEventDispatcher, lastPlayer);
            newPlayerFlag=false;
        }*/

    }

    private boolean isNewPlayer(Player player){
        if(!player.equals(lastPlayer)){
            return true;
        }
        return false;
    }
}
