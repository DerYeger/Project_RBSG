package de.uniks.se19.team_g.project_rbsg.skynet.behaviour;

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Game;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Player;
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit;
import de.uniks.se19.team_g.project_rbsg.skynet.action.Action;
import de.uniks.se19.team_g.project_rbsg.skynet.action.SurrenderAction;

import java.util.Optional;
import java.util.stream.Collectors;

public class SurrenderBehaviour implements Behaviour
{
    @Override
    public Optional<? extends Action> apply(Game game, Player player)
    {
        boolean cantAttackAnEnemyByType = isCantAttackAnEnemyByType(game, player);

        if(cantAttackAnEnemyByType) {
            return Optional.of(new SurrenderAction());
        }

        boolean firstMovingPhase = game.getPhase().equals("movePhase");

        if(firstMovingPhase) {
            boolean NoUnitCanMove = isNoUnitCanMove(player);
            if(NoUnitCanMove) {
                return Optional.of(new SurrenderAction());
            }
        }

        return Optional.empty();
    }

    private boolean isNoUnitCanMove(Player player)
    {
        return player
                .getUnits()
                .stream()
                .allMatch( unit -> unit.getNeighbors().size() == getSizeOfPassableNeighbors(unit));
    }

    private int getSizeOfPassableNeighbors(Unit unit)
    {
        return (int) unit.getPosition().getNeighbors()
                .stream()
                .filter(Cell::isPassable).count();
    }

    private boolean isCantAttackAnEnemyByType(Game game, Player player)
    {
        return game
                .getUnits()
                .stream()
                .filter(enemy -> enemy.getLeader() != player)
                .noneMatch(enemy -> player
                        .getUnits()
                        .stream()
                        .anyMatch(unit -> unit.canAttack(enemy)));
    }
}
