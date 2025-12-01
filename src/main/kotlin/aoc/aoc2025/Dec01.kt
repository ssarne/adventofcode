package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    execute(readTestLines(1)).first.let { check(it, 3L); println("Test: $it") }
    execute(readLines()).first.let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute(readTestLines(1)).second.let { check(it, 6L) ; println("Test: $it") }
    execute(readLines()).second.let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>): Pair<Long, Long> {

    var dial = 50
    var stops = 0L
    var passes = 0L

    for (line in input) {
        val dir = if (line.startsWith('L')) -1 else 1
        val rotation = line.substring(1).toInt()
        for (i in 0 until rotation) {
            dial += dir
            if (dial == 100) dial = 0
            if (dial == -1) dial = 99
            if (dial == 0) passes++
        }
        if (dial == 0) stops++
    }
    return stops to passes
}
