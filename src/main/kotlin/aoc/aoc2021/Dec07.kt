package aoc.aoc2021


import aoc.ktutils.*
import kotlin.math.abs

fun main() {
    check(execute1("16,1,2,0,4,2,7,1,2,14"), 37)
    execute1(readText()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2("16,1,2,0,4,2,7,1,2,14"), 168)
    execute2(readText()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: String): Int {

    val positions = asIntArray(input)
    var best = Int.MAX_VALUE
    for (p in positions.min()!! .. positions.max()!!) {
        val fuel = positions.asSequence()
            .map { abs(it - p) }
            .sum()
        if (fuel < best) best = fuel
    }

    return best
}

private fun execute2(input: String): Int {

    val positions = asIntArray(input)
    var best = Int.MAX_VALUE
    for (p in positions.min()!! .. positions.max()!!) {
        val fuel = positions.asSequence()
            .map { fuel(abs(it - p)) }
            .sum()
        if (fuel < best) best = fuel
    }

    return best
}

fun fuel(dist: Int): Int {
    var fuel = 0
    for (d in 0..dist) {
        fuel += d
    }
    return fuel
}