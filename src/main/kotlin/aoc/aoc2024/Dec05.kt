package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 143)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 123)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val (rules, updates) = parse(input)
    var sum = 0L

    for (update in updates) {
        if (safeToPrint(rules, update))
            sum += update[update.size / 2]
    }

    return sum
}

private fun execute2(input: List<String>): Long {

    val (rules, updates) = parse(input)
    var sum = 0L

    for (update in updates) {
        if (safeToPrint(rules, update)) continue
        val fixed = sort(rules, update)
        sum += fixed[fixed.size / 2]
    }

    return sum
}

private fun safeToPrint(rules: MutableList<Pair<Int, Int>>, update: IntArray): Boolean {
    val printed = HashSet<Int>()
    for (i in update.indices) {
        val page = update[update.size - 1 - i]
        for (pair in rules)
            if (pair.second == page)
                if (printed.contains(pair.first))
                    return false
        printed.add(page)
    }
    return true
}

private fun sort(rules: MutableList<Pair<Int, Int>>, update: IntArray): IntArray {
    val sorted = mutableListOf<Int>()
    for (i in update.indices) {
        val p = update[update.size - 1 - i]
        var after = -1
        for (pair in rules)
            if (pair.second == p)
                for (j in sorted.indices)
                    if (sorted[j] == pair.first)
                        after = max(j, after)

        when (after) {
            -1 -> sorted.add(0, p)
            sorted.lastIndex -> sorted.add(p)
            else -> sorted.add(after + 1, p)
        }
    }
    return sorted.toIntArray()
}

private fun parse(input: List<String>): Pair<MutableList<Pair<Int, Int>>, MutableList<IntArray>> {
    val rules = mutableListOf<Pair<Int, Int>>()
    val updates = mutableListOf<IntArray>()
    for (line in input) {
        if (line.contains("|")) {
            val rule = line.trim().split("|")
            rules.add(rule[0].toInt() to rule[1].toInt())
        }

        if (line.contains(",")) {
            updates.add(asIntArray(line))
        }
    }
    return Pair(rules, updates)
}
