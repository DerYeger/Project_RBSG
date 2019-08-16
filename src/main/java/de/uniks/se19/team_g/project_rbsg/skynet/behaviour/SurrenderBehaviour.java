package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.model.*;
import de.uniks.se19.team_g.project_rbsg.skynet.action.*;
import org.slf4j.*;

import java.util.*;

public class SurrenderBehaviour implements Behaviour
{
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Optional<? extends Action> apply (Game game, Player player)
    {
        boolean cantAttackAnEnemyByType = isCantAttackAnEnemyByType(game, player);

        if (cantAttackAnEnemyByType)
        {
            logger.debug("Cant Attack Enemies by type!");
            return Optional.of(new SurrenderAction());
        }

        boolean firstMovingPhase = game.getPhase().equals("movePhase");

        if (firstMovingPhase)
        {
            boolean NoUnitCanMove = isNoUnitCanMove(player);
            if (NoUnitCanMove)
            {
                logger.debug("Cant move a Unit its not possible to change turn!");
                return Optional.of(new SurrenderAction());
            }
        }

        return Optional.empty();
    }

    private boolean isNoUnitCanMove (Player player)
    {
        return player
                .getUnits()
                .stream()
                .allMatch(unit -> unit.getNeighbors().size() == getSizeOfPassableNeighbors(unit));
    }

    private int getSizeOfPassableNeighbors (Unit unit)
    {
        return (int) unit.getPosition().getNeighbors()
                .stream()
                .filter(Cell::isPassable).count();
    }

    private boolean isCantAttackAnEnemyByType (Game game, Player player)
    {
        return game
                .getUnits()
                .stream()
                .filter(enemy -> enemy.getLeader() != player)
                .noneMatch(enemy -> player
                        .getUnits()
                        .stream()
                        .anyMatch(unit -> unit.getCanAttack().contains(enemy.getUnitType())));
    }
}
