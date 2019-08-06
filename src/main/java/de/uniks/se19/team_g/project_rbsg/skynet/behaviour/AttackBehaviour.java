package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import de.uniks.se19.team_g.project_rbsg.skynet.action.AttackAction;
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.exception.AttackBehaviourException;
import de.uniks.se19.team_g.project_rbsg.util.Tuple;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;

import java.util.Optional;

public class AttackBehaviour implements Behaviour {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Optional<Action> apply(@NonNull final Game game,
                                  @NonNull final Player player) {
        try {
            final Tuple<Unit, Unit> tuple = player
                    .getUnits()
                    .stream()
                    .map(unit -> new Tuple<>(unit, getAttackTarget(unit)))
                    .filter(t -> t.first != t.second)
                    .findFirst()
                    .orElseThrow(() -> new AttackBehaviourException("message"));
            return Optional.of(new AttackAction(tuple.first, tuple.second));
        } catch (final AttackBehaviourException e) {
            logger.info(e.getMessage());
        }
        return Optional.empty();
    }

    private Unit getAttackTarget(@NonNull final Unit unit) {
        return unit
                .getNeighbors()
                .stream()
                .filter(other -> !other.getLeader().equals(unit.getLeader()))
                .findFirst()
                .orElse(unit);
    }
}
