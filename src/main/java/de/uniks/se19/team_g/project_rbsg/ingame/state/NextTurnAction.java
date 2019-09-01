package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;

public class NextTurnAction implements Action {

    private Game game;
    private  Player player;

    public NextTurnAction(Game game, Player player){
        this.game =game;
        this.player = player;
    }
    @Override
    public void undo() {
        game.setTurnCount( game.getTurnCount() - 1);
    }

    @Override
    public void run() {
        game.setTurnCount( game.getTurnCount() + 1);
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
