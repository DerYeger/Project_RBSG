package de.uniks.se19.team_g.project_rbsg.skynet.action;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import org.springframework.lang.NonNull;

public class AttackAction implements Action {

    public final Unit unit;
    public final Unit target;

    public AttackAction(@NonNull final Unit unit, @NonNull final Unit target) {
        this.unit = unit;
        this.target = target;
    }
}
