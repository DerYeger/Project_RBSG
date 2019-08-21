package de.uniks.se19.team_g.project_rbsg.ingame.state;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;

public class UnitDeathAction implements Action {

    private final Unit unit;
    private final Player leader;

    public UnitDeathAction(Unit unit, Player leader) {
        this.unit = unit;

        this.leader = leader;
    }

    @Override
    public void undo() {

    }

    @Override
    public void run() {

    }

    public Unit getUnit() {
        return unit;
    }

    public Player getPlayer() {
        return leader;
    }
}
