package aoc.aoc2017

import aoc.ktutils.check

fun main() {
    check(match1(65, 8921, 5), 1)
    check(match1(65, 8921, 40000000L), 588)
    println(match1(618, 814, 40000000L)) // 577

    check(match2(65, 8921, 5), 0)
    check(match2(65, 8921, 1057), 1)
    check(match2(65, 8921, 5000000L), 309)
    println(match2(618, 814, 5000000L)) // 316

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

private fun match2(a: Long, b: Long, c: Long): Int {
    var ga = a
    var gb = b
    var matches = 0
    var mask = "FFFF".toLong(radix = 16)
    for (i in 1..c) {
        do { ga = ga * 16807L % 2147483647L } while (ga % 4 != 0L)
        do { gb = gb * 48271L % 2147483647L } while (gb % 8 != 0L)

        if (ga and mask == gb and mask) matches++
    }
    return matches
}