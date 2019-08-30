package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import javafx.beans.property.SimpleIntegerProperty;

public class NextTurnAction implements Action {

    SimpleIntegerProperty roundCounter;
    private  Player currentPlayer;

    public NextTurnAction(SimpleIntegerProperty roundCounter, Player currentPlayer){
        this.roundCounter=roundCounter;
        this.currentPlayer=currentPlayer;
    }
    @Override
    public void undo() {
        roundCounter.set(roundCounter.getValue()-1);
    }

    @Override
    public void run() {
        roundCounter.set(roundCounter.getValue()+1);
    }

    public SimpleIntegerProperty getRoundCounter() {
        return roundCounter;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
