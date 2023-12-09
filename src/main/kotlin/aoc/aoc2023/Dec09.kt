package aoc.aoc2023

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 114)
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 2)
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val values = asLongArray(line)
        sum += diffAndPad(values).second
    }
    return sum
}

private fun execute2(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val values = asLongArray(line)
        sum += diffAndPad(values).first
    }
    return sum
}

fun diffAndPad(values: LongArray): Pair<Long, Long> {

    if (all(values, 0))
        return 0L to 0L

    val diff = LongArray(values.size - 1)
    for (i in diff.indices)
        diff[i] = values[i + 1] - values[i]

    val next = diffAndPad(diff)
    val before = values.first() - next.first
    val after = values.last() + next.second
    return before to after
}

