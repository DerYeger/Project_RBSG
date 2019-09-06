package de.uniks.se19.team_g.project_rbsg.skynet.behaviour

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell
import de.uniks.se19.team_g.project_rbsg.ingame.model.Unit

fun preferSmaller(first : Double, second: Double) : Int = when {
    first < second -> -1
    first > second -> 1
    else -> 0
}

fun preferSmaller(first : Int, second : Int) : Int = first - second

fun preferBigger(first : Double, second: Double) : Int = when {
    first < second -> 1
    first > second -> -1
    else -> 0
}

fun preferBigger(first : Int, second : Int) : Int = second - first

fun Cell.attackableNeighbors(unit : Unit) : List<Unit> =
        this.neighbors.mapNotNull { it.unit }.filter { unit.canAttack(it) }

fun Cell.threateningNeighbors(unit : Unit): List<Unit> =
        this.neighbors.mapNotNull { it.unit }.filter { it.canAttack(unit) }

fun Unit.attackableNeighbors() : List<Unit> =
        this.neighbors.filter { this.canAttack(it) }

fun Unit.threateningNeighbors() : List<Unit> =
        this.neighbors.filter { it.canAttack(this) }