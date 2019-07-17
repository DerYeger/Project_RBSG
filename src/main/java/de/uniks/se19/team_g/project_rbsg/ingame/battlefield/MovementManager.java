package de.uniks.se19.team_g.project_rbsg.ingame.battlefield;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.WeakChangeListener;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

/**
 * The movement manager should provide information about cells that can be reached for a given unit
 */
public class MovementManager {

    private final MovementEvaluator movementEvaluator;

    private Map<Cell, Tour> allowedTours;
    private Unit unit;
    private ChangeListener<Cell> unitPositionListener;

    public MovementManager(MovementEvaluator movementEvaluator) {

        this.movementEvaluator = movementEvaluator;
    }

    @Nullable
    public Tour getTour(final @Nonnull Unit unit, final @Nonnull Cell target1) {
        if (this.unit != unit) {
            manageUnit(unit);
        }

        return allowedTours.get(target1);
    }

    protected void manageUnit(final @Nonnull Unit unit) {
        this.unit = unit;
        allowedTours = movementEvaluator.getAllowedTours(unit);

        unitPositionListener = (observable, oldValue, newValue) -> {
            this.unit = null;
            this.allowedTours = null;
        };
        this.unit.positionProperty().addListener(new WeakChangeListener<>(unitPositionListener));
    }
}
