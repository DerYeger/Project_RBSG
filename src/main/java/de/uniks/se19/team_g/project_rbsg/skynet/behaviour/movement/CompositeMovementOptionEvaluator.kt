package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit

class CompositeMovementOptionEvaluator : MovementOptionEvaluator {

    override fun compare(first: MovementOption,
                         second: MovementOption): Int {
        return when {
            first.tour === second.tour -> compareEnemies(first, second)
            first.enemy === second.enemy -> compareTours(first, second)
            else -> compareMovementOptions(first, second)
        }
    }

    private fun compareEnemies(first: MovementOption,
                               second: MovementOption) : Int {
        return 0
    }

    private fun compareTours(first: MovementOption,
                             second: MovementOption) : Int {
        if (first.distanceToEnemy != second.distanceToEnemy)
            return (first.distanceToEnemy - second.distanceToEnemy).toInt()

        return compareDestinations(first, second)
    }


    private fun compareMovementOptions(first: MovementOption,
                                       second: MovementOption) : Int {
        return 0
    }

    private fun compareDestinations(first: MovementOption,
                                    second: MovementOption) : Int {
        val firstThreats = first.destination.threateningNeighbors(first.unit).size
        val secondThreats = second.destination.threateningNeighbors(second.unit).size
        if (firstThreats != secondThreats)
            return firstThreats - secondThreats

        return 0
    }

    private fun Cell.threateningNeighbors(unit : Unit): List<Unit> {
        return this.neighbors
                .mapNotNull { it.unit }
                .filter { it.canAttack(unit) }
    }
}
