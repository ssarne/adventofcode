package aoc.aoc2023

import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    check(execute1(readTestLines(1)), 35)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 46)
    //execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class Mapping(var from: Long, var to: Long, var range: Long)

private fun execute1(input: List<String>): Long {

    val (seeds, transitions) = parse(input)
    var min = Long.MAX_VALUE

    for (n in seeds) {
        val location = transition(n, transitions)
        if (min > location) min = location
    }
    return min
}


private fun execute2(input: List<String>): Long {

    val (seeds, transitions) = parse(input)
    var min = Long.MAX_VALUE

    for (j in seeds.indices step 2) {
        for (n in seeds[j]..seeds[j] + seeds[j + 1]) {

            val location = transition(n, transitions)
            if (min > location) min = location
        }
    }
    return min
}

private fun transition(
    n: Long,
    transitions: MutableList<List<Mapping>>
): Long {
    var current = n
    for (transition in transitions) {
        var next = current
        for (mapping in transition) {
            if (current >= mapping.from && current < mapping.from + mapping.range) {
                next = current - mapping.from + mapping.to
            }
        }
        current = next
    }
    return current
}

private fun parse(input: List<String>): Pair<LongArray, MutableList<List<Mapping>>> {
    val seeds = asLongArray(input.first().split(": ")[1])
    val mappings = HashMap<String, List<Mapping>>()

    var list: MutableList<Mapping>? = null
    for (line in input) {
        if (line.startsWith("seeds")) continue
        if (line.isEmpty()) continue
        if (line.contains("map:")) {
            list = LinkedList<Mapping>()
            mappings[line.split(" ")[0]] = list
            continue
        }
        var mapping = asLongArray(line)
        list!!.add(Mapping(mapping[1], mapping[0], mapping[2]))
    }

    val categories = listOf("seed", "soil", "fertilizer", "water", "light", "temperature", "humidity", "location")
    val transitions = mutableListOf<List<Mapping>>()
    for (i in categories.indices) {
        if (i + 1 == categories.size) break
        val label = categories[i] + "-to-" + categories[i + 1]
        transitions.add(mappings[label]!!)
    }
    return Pair(seeds, transitions)
}
