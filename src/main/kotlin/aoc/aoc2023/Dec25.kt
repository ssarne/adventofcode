package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.*

fun main() {
    // check(execute1(readTestLines(1)), 54)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }
    execute2(readLines()).let { println(it) }
}

// This is a global-min-cut problem, could be solved by algorithm Stoer-Wagner

private fun execute1(input: List<String>): Int {
    val (components, connections) = parse(input)

    // dot -Tsvg -Kneato target/aoc2023_dec25.dot > target/aoc2023_dec25.svg
    dotty(connections)
    val excluded = HashSet<Pair<String,String>>()
    excluded.add("nvg" to "vfj") ; excluded.add("vfj" to "nvg") // nvg: vfj
    excluded.add("jbz" to "sqh") ; excluded.add("sqh" to "jbz") // sqh: jbz
    excluded.add("fch" to "fvh") ; excluded.add("fvh" to "fch") // fch: fvh

    val count = countConnected(components, excluded)
    return count * (components.size - count)
}

private fun execute2(input: List<String>): String {
    return "God Jul 2023"
}

fun countConnected(components: HashMap<String, HashSet<String>>, excluded: HashSet<Pair<String, String>>): Int {
    val queue = LinkedList<String>()
    queue.add(components.keys.first())
    val visited = HashSet<String>()
    while (queue.isNotEmpty()) {
        val c = queue.pop()
        visited.add(c)
        for (to in components[c]!!) {
            if (excluded.contains(c to to)) continue
            if (visited.contains(to)) continue
            queue.add(to)
        }
    }
    return visited.size
}

private fun parse(input: List<String>): Pair<HashMap<String, HashSet<String>>, HashSet<Pair<String, String>>> {
    val components = HashMap<String, HashSet<String>>()
    val connections = HashSet<Pair<String, String>>()
    for (line in input) {
        val from = line.split((": "))[0]
        components[from] = HashSet()
        val tos = line.split(": ")[1].split(" ")
        for (to in tos)
            components[to] = HashSet()
    }
    for (line in input) {
        val from = line.split((": "))[0]
        val tos = line.split(": ")[1].split(" ")
        for (to in tos) {
            components[from]!!.add(to)
            components[to]!!.add(from)
            connections.add(if (from < to) from to to else to to from)
        }
    }
    return components to connections
}
