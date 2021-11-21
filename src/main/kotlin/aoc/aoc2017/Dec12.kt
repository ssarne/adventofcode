package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readTestLines
import aoc.ktutils.readLines
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    check(groupSize(parse(readTestLines()), "0"),6)
    println(groupSize(parse(readLines()), "0")) // 130
    check(countGroups(parse(readTestLines())),2)
    println(countGroups(parse(readLines()))) // 189
}

private data class Node(
    val id: String,
    val edges: MutableMap<String, Node> = HashMap(),
    var visited: Boolean = false
)

private fun parse(lines: List<String>): MutableMap<String, Node> {
    val edgeMap: MutableMap<String, MutableList<String>> = HashMap()
    for (line in lines) {
        var tokens = line.split(" <-> ")
        var from = tokens.first()
        if (edgeMap[from] == null) edgeMap[from] = ArrayList()
        edgeMap[from]?.addAll(tokens.last().split(", "))
    }

    val nodes: MutableMap<String, Node> = HashMap()
    for (id in edgeMap.keys) nodes[id] = Node(id)
    for (id in edgeMap.keys) {
        for (pid in edgeMap[id]!!) {
            nodes[id]?.edges?.put(pid, nodes[pid]!!)
        }
    }

    return nodes
}

private fun groupSize(graph: MutableMap<String, Node>, id: String): Int {
    var queue: Queue<Node> = LinkedList()
    queue.add(graph[id])
    var size = 0
    while (queue.isNotEmpty()) {
        var node = queue.remove()
        if (node.visited) continue

        node.visited = true
        size++
        queue.addAll(node.edges.values)
    }
    return size
}

private fun countGroups(graph: MutableMap<String, Node>): Int {

    var groups = 0
    for (id in graph.keys) {

        if (graph[id]?.visited == true) continue

        var queue: Queue<Node> = LinkedList()
        queue.add(graph[id])
        groups++;
        while (queue.isNotEmpty()) {
            var node = queue.remove()
            if (node.visited) continue
            node.visited = true
            queue.addAll(node.edges.values)
        }
    }
    return groups
}
