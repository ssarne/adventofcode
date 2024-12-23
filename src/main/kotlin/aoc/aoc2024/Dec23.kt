package aoc.aoc2024


import aoc.ktutils.*
import java.util.BitSet
import kotlin.math.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 7L) } // ; println("Test: $it") }
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, "co,de,ka,ta") } // ; println("Test: $it") }
    execute2(readLines()).let { println(it); check(it, readAnswer(2)) }
}

private fun execute1(input: List<String>): Long {
    val connections = parse(input)

    val threes = mutableSetOf<Triple<String, String, String>>()
    for (c1 in connections.keys) {
        for (c2 in connections[c1]!!) {
            for (c3 in connections[c2]!!) {
                if (c1 == c3) continue
                for (c4 in connections[c3]!!) {
                    if (c1 == c4) {
                        if (c1.startsWith('t') || c2.startsWith('t') || c3.startsWith('t')) {
                            threes.add(Triple(c1, c2, c3))
                        }
                    }
                }
            }
        }
    }

    return threes.size.toLong() / 6
}

private fun execute2(input: List<String>): String {
    val connections = parse(input)
    val sorted = connections.keys.toList().sorted()
    val network = findLargest(0, sorted, connections, BitSet())
    return sorted.withIndex().filter { (i, c) -> network[i] }.joinToString(",") { it.value }
}

fun findLargest(i: Int, sorted: List<String>, connections: Map<String, Set<String>>, network: BitSet): BitSet {

    if (i == sorted.size)
        return network

    // test to not add this computer to the network
    val excluded = findLargest(i + 1, sorted, connections, network)

    // then test to add it, if possible
    if (isConnected(connections, i, network, sorted)) {
        var included = network.clone() as BitSet
        included[i] = true
        included = findLargest(i + 1, sorted, connections, included)
        if (included.cardinality() > excluded.cardinality())
            return included
    }

    return excluded
}

private fun isConnected(connections: Map<String, Set<String>>, i: Int, network: BitSet, sorted: List<String>): Boolean {
    if (network.length() == 0) return true
    val cons = connections[sorted[i]] ?: return false
    for (b in 0 until i) {
        if (network[b] && !cons.contains(sorted[b])) {
            return false
        }
    }
    return true
}

private fun parse(input: List<String>): MutableMap<String, MutableSet<String>> {
    val connections = mutableMapOf<String, MutableSet<String>>()
    for (line in input) {
        line.split("-").let {
            val cons0 = connections.getOrDefault(it[0], mutableSetOf())
            cons0.add(it[1])
            connections[it[0]] = cons0

            val cons1 = connections.getOrDefault(it[1], mutableSetOf())
            cons1.add(it[0])
            connections[it[1]] = cons1
        }
    }
    return connections
}
