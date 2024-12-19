package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 6) }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 16L) }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val (towels, patterns) = parse(input)
    val memory = mutableMapOf("" to 1L)
    var count = 0L

    for (pattern in patterns) {
        val n = countCompositions(pattern, towels, memory)
        if (n > 0) count++
    }

    return count
}

private fun execute2(input: List<String>): Long {

    val (towels, patterns) = parse(input)

    val memory = mutableMapOf("" to 1L)
    var count = 0L
    for (pattern in patterns) {
        count += countCompositions(pattern, towels, memory)
    }

    return count
}

fun countCompositions(pattern: String, towels: List<String>, memory: MutableMap<String, Long>): Long {

    if (memory.containsKey(pattern))
        return memory[pattern]!!

    var count = 0L
    for (towel in towels)
        if (pattern.startsWith(towel))
            count += countCompositions(pattern.substring(towel.length), towels, memory)

    memory[pattern] = count
    return count
}

private fun parse(input: List<String>): Pair<List<String>, MutableSet<String>> {
    val towels = input.first().split(", ")
    val patterns = mutableSetOf<String>()
    for (line in input) {
        if (line == "") continue
        if (line.contains(", ")) continue
        patterns.add(line)
    }
    return Pair(towels, patterns)
}