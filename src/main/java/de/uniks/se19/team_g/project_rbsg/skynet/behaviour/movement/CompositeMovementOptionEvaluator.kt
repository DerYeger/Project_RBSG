package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.movement

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit
import de.uniks.se19.team_g.project_rbsg.util.squared
import kotlin.math.absoluteValue

class CompositeMovementOptionEvaluator : MovementOptionEvaluator {

    override fun compare(first: MovementOption,
                         second: MovementOption): Int {
        return when {
            first.tour === second.tour -> compareEnemies(first, second)
            first.enemy === second.enemy -> compareTours(first, second)
            else -> compareMovementOptions(first, second)
        }
    }

    private fun compareMovementOptions(first: MovementOption,
                                       second: MovementOption) : Int {
        val enemyComparison = compareEnemies(first, second)
        val tourComparison = compareTours(first, second)
        
        return when {
            enemyComparison.squared() > tourComparison.absoluteValue -> enemyComparison
            else -> tourComparison
        }
    }

    private fun compareEnemies(first: MovementOption,
                               second: MovementOption) : Int {
        val firstAttackValue = first.unit.getAttackValue(first.enemy.unit)
        val secondAttackValue = second.unit.getAttackValue(second.enemy.unit)

        val firstEnemyThreats = first.enemy.threats().size
        val secondEnemyThreats = second.enemy.threats().size

        return when {
            firstAttackValue != secondAttackValue -> secondAttackValue - firstAttackValue
            firstEnemyThreats != secondEnemyThreats -> secondEnemyThreats - firstEnemyThreats
            first.unit.hp != second.unit.hp -> first.unit.hp - second.unit.hp
            else -> 0
        }
    }

    private fun compareTours(first: MovementOption,
                             second: MovementOption) : Int {
        return when {
            first.distanceToEnemy != second.distanceToEnemy -> (first.distanceToEnemy - second.distanceToEnemy).toInt()
            else -> compareDestinations(first, second)
        }
    }

    private fun compareDestinations(first: MovementOption,
                                    second: MovementOption) : Int {
        val firstTargets = first.destination.attackableNeighbors(first.unit).size
        val secondTargets = second.destination.attackableNeighbors(second.unit).size

        val firstThreats = first.destination.threateningNeighbors(first.unit).size
        val secondThreats = second.destination.threateningNeighbors(second.unit).size

        return when {
            firstTargets != secondTargets -> secondTargets - firstTargets
            firstThreats != secondThreats -> firstThreats - secondThreats
            else -> 0
        }
    }

    private fun Cell.attackableNeighbors(unit : Unit) : List<Unit> {
        return this
                .neighbors
                .mapNotNull { it.unit }
                .filter { unit.canAttack(it) }
    }

    private fun Cell.threateningNeighbors(unit : Unit): List<Unit> {
        return this
                .neighbors
                .mapNotNull { it.unit }
                .filter { it.canAttack(unit) }
    }

    private fun Enemy.threats() : List<Unit> {
        return this
                .position
                .threateningNeighbors(this.unit)
    }
}
