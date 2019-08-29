package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;

public class NextTurnAction implements Action {

    int roundCounter;
    private  Player currentPlayer;

    public NextTurnAction(int roundCounter, Player currentPlayer){
        this.roundCounter=roundCounter;
        this.currentPlayer=currentPlayer;
    }
    @Override
    public void undo() {
        //roundCounter--;
    }

    @Override
    public void run() {
        //roundCounter++;
    }

    public int getRoundCounter() {
        return roundCounter;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
