package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;

import javax.annotation.Nullable;

/**
 * The movement manager should provide information about cells that can be reached for a given unit
 */
public class MovementManager {

    private final MovementEvaluator movementEvaluator;

    public MovementManager(MovementEvaluator movementEvaluator) {

        this.movementEvaluator = movementEvaluator;
    }

    @Nullable
    public Tour getTour(Unit unit, Cell target1) {

        return null;
    }
}
