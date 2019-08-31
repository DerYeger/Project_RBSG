package de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attack

import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.attackableNeighbors
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.preferBigger
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.preferSmaller
import de.uniks.se19.team_g.project_rbsg.skynet.behaviour.threateningNeighbors

class DefaultAttackOptionEvaluator : AttackOptionEvaluator {

    override fun compare(first: AttackOption, second: AttackOption): Int = when {
        first.defender.hp != second.defender.hp -> preferSmaller(first.defender.hp, second.defender.hp)
        else -> compareDefenderNeighbors(first, second)
    }

    private fun compareDefenderNeighbors(first: AttackOption, second: AttackOption) : Int {
        val firstThreatenedNeighbors = first.defender.attackableNeighbors().size
        val secondThreatenedNeighbors = second.defender.attackableNeighbors().size

        val firstThreateningNeighbors = first.defender.threateningNeighbors().size
        val secondThreateningNeighbors = second.defender.threateningNeighbors().size

        return when {
            firstThreatenedNeighbors != secondThreatenedNeighbors -> preferBigger(firstThreatenedNeighbors, secondThreatenedNeighbors)
            else -> preferSmaller(firstThreateningNeighbors, secondThreateningNeighbors)
        }
    }
}