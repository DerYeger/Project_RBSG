package de.uniks.se19.team_g.project_rbsg.ingame.model.util

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell
import java.util.*

class Node(val cell : Cell) :Comparable<Node> {
    var visited = false
    var distance = Int.MAX_VALUE

    override fun compareTo(other: Node): Int = distance - other.distance
}

fun distance(start : Cell, end : Cell) : Int {
    val nodes = start.game.cells.associateWith(::Node)

    val initial = nodes.getValue(start)
    initial.distance = 0

    val uncheckedNodes = PriorityQueue<Node>()

    uncheckedNodes.offer(initial)

    while (uncheckedNodes.isNotEmpty()) {
        val currentNode = uncheckedNodes.poll()
        currentNode.visited = true

        for (neighbor in currentNode.cell.neighbors) {
            if (!neighbor.isPassable) continue

            val neighborNode = nodes.getValue(neighbor)
            if (neighborNode.visited || neighborNode.distance <= currentNode.distance + 1) continue

            neighborNode.distance = currentNode.distance + 1

            if (neighbor === end)
                return neighborNode.distance

            uncheckedNodes.offer(neighborNode)
        }
    }

    return nodes.getValue(end).distance
}
