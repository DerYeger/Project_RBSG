package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;

public class RoundChangeAction implements Action {

    int roundCounter;

    public RoundChangeAction(int roundCounter){
        this.roundCounter=roundCounter;
    }
    @Override
    public void undo() {
        //roundCounter--;
    }

    @Override
    public void run() {
        //roundCounter++;
    }
}
