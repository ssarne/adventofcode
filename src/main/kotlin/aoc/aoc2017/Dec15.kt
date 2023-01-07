package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readAnswerAsInt
import aoc.ktutils.readLines

fun main() {
    check(match1(65, 8921, 5), 1)
    check(match1(65, 8921, 40000000L), 588)
    execute1(readLines(), 40000000L).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(match2(65, 8921, 5), 0)
    check(match2(65, 8921, 1057), 1)
    check(match2(65, 8921, 5000000L), 309)
    execute2(readLines(), 5000000L).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>, c: Long): Int {
    val (a, b) = parse(input)
    return match1(a, b, c)
}

private fun match1(a: Long, b: Long, c: Long): Int {
    var ga = a
    var gb = b
    var matches = 0
    var mask = "FFFF".toLong(radix = 16)
    for (i in 1..c) {
        ga = ga * 16807L % 2147483647L
        gb = gb * 48271L % 2147483647L
        if (ga and mask == gb and mask) matches++
    }
    return matches
}

private fun execute2(input: List<String>, c: Long): Int {
    val (a, b) = parse(input)
    return match2(a, b, c)
}

private fun match2(a: Long, b: Long, c: Long): Int {
    var ga = a
    var gb = b
    var matches = 0
    var mask = "FFFF".toLong(radix = 16)
    for (i in 1..c) {
        do {
            ga = ga * 16807L % 2147483647L
        } while (ga % 4 != 0L)
        do {
            gb = gb * 48271L % 2147483647L
        } while (gb % 8 != 0L)

        if (ga and mask == gb and mask) matches++
    }
    return matches
}

private fun parse(input: List<String>): Pair<Long, Long> {
    return Pair(
        input[0].trim().split(" ")[4].toLong(),
        input[1].trim().split(" ")[4].toLong()
    )
}