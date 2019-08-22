package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.Behaviour;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class AttackBehaviour implements Behaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Optional<AttackAction> apply(@NonNull final Game game,
                                  @NonNull final Player player) {
        try {
            final Unit unit = player
                    .getUnits()
                    .stream()
                    .filter(this::canAttackAnyTarget)
                    .findFirst()
                    .orElseThrow(() -> new AttackBehaviourException("No unit can attack"));
            final Unit target = unit
                    .getNeighbors()
                    .stream()
                    .filter(unit::canAttack)
                    .findFirst()
                    .orElseThrow(() -> new AttackBehaviourException("An unexpected error occurred"));
            return Optional.of(new AttackAction(unit, target));
        } catch (final AttackBehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private boolean canAttackAnyTarget(@NonNull final Unit unit) {
        return unit.attackReadyProperty().get()
                &&
                unit.getNeighbors()
                        .stream()
                        .anyMatch(unit::canAttack);
    }
}
