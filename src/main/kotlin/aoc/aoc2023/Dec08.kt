package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap


fun main() {
    check(execute1(readTestLines(1)), 2)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 6L)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class Node(val name: String, val left: String, val right: String, var steps: Int)

private fun execute1(input: List<String>): Int {
    val (path, map) = parse(input)
    var pos = map["AAA"]!!
    var steps = 0
    while (pos.name != "ZZZ") {
        val c = path[steps % path.length]
        if (c == 'L') pos = map[pos.left]!!
        if (c == 'R') pos = map[pos.right]!!
        steps++
    }

    return steps
}

private fun execute2(input: List<String>): Long {

    val (path, map) = parse(input)
    val positions = LinkedList<String>()

    for (node in map.values)
        if (node.name[2] == 'A')
            positions.add(node.name)

    val zlength = LongArray(positions.size)
    for (i in positions.indices) {
        var steps = 0
        var pos = positions[i]
        while (pos[2] != 'Z') {
            val node = map[pos]!!
            val c = path[steps % path.length]
            if (c == 'L') pos = node.left
            if (c == 'R') pos = node.right
            steps++
        }
        zlength[i] = steps.toLong()
    }
    
    return lcm(*zlength)
}

private fun parse(input: List<String>): Pair<String, Map<String, Node>> {
    val map = HashMap<String, Node>()
    var path = ""
    for (line in input) {
        if (line.isEmpty()) continue
        if (line.contains("=")) {
            val name = line.split(" = ")[0]
            val ways = line.split(" = ")[1]
                .replace("(", "")
                .replace(")", "")
                .split((", "))
            map[name] = Node(name, ways[0], ways[1], -1)
            continue
        }
        path = line
    }
    return path to map
}
