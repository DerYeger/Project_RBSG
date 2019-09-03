package de.uniks.se19.team_g.project_rbsg.ingame.model.util

import de.uniks.se19.team_g.project_rbsg.ingame.model.Cell
import java.util.*
import kotlin.collections.HashMap

private class Node(val cell : Cell) :Comparable<Node> {
    var visited = false
    var distance = Int.MAX_VALUE

    override fun compareTo(other: Node): Int = distance - other.distance
}

fun distance(start : Cell, end : Cell) : Int {
    if (start === end) return 0

    val nodes = HashMap<Cell, Node>()

    val initial = nodes.fetch(start)
    initial.distance = 0

    val uncheckedNodes = PriorityQueue<Node>()

    uncheckedNodes.offer(initial)

    while (uncheckedNodes.isNotEmpty()) {
        val currentNode = uncheckedNodes.poll()
        currentNode.visited = true

        for (neighbor in currentNode.cell.neighbors) {
            if (!neighbor.isPassable) continue

            val neighborNode = nodes.fetch(neighbor)
            if (neighborNode.visited || neighborNode.distance <= currentNode.distance + 1) continue

            neighborNode.distance = currentNode.distance + 1

            if (neighbor === end)
                return neighborNode.distance

            uncheckedNodes.offer(neighborNode)
        }
    }

    return nodes.fetch(end).distance
}

private fun HashMap<Cell, Node>.fetch(cell : Cell) : Node = this.getOrPut(cell) { Node(cell) }
