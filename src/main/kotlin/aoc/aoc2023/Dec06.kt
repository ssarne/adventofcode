package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.abs

fun main() {
    check(execute1(readTestLines(1)), 288)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 71503)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {

    val times = asIntArray(input[0].split(":")[1])
    val dists = asIntArray(input[1].split(":")[1])
    var result = 1

    for (i in times.indices) {
        var wins = 0
        for (hold in 1 .. times[i]) {
            val dist = (times[i] - hold) * hold
            if (dist > dists[i]) wins++
        }
        result *= wins
    }
    return result
}

private fun execute2(input: List<String>): Long {

    val time = input[0].split(":")[1].replace(" ", "").toLong()
    val target = input[1].split(":")[1].replace(" ", "").toLong()

    // Find last loss lower half
    val low = binarySearch(0L, time / 2, {(time - it) * it}, {it < target})

    // Find last win, upper half
    val high = binarySearch(time / 2, time, {(time - it) * it}, {it > target})
    return high - low
}