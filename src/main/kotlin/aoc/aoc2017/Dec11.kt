package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readText
import aoc.ktutils.readAnswerAsInt

fun main() {
    check(execute("ne,ne,ne").first, 3)
    check(execute("ne,ne,sw,sw").first, 0)
    check(execute("ne,ne,s,s").first, 2)
    check(execute("se,sw,se,sw,sw").first, 3)
    check(execute("ne,nw").first, 1)
    check(execute("n,n,n,sw").first, 3)
    check(execute("s,s,s,sw").first, 4)
    check(execute("s,sw,sw,sw").first, 4)
    check(execute("s,s,s,nw").first, 3)
    check(execute("s,sw,sw,sw").first, 4)
    check(execute("s,s,s,nw").first, 3)
    execute(readText()).let { println(it.first) ; check(it.first, readAnswerAsInt(1)) }

    check(execute("ne,ne,sw,sw").second, 2)
    execute(readText()).let { println(it.second) ; check(it.second, readAnswerAsInt(2)) }
}

private fun execute(input: String): Pair<Int, Int> {
    val steps = input.split(",")
    var n = 0
    var e = 0
    var dist = 0
    var max = 0
    for (step in steps) {
        when (step) {
            "n" -> {n++}
            "s" -> {n--}
            "ne" -> {n++ ; e++}
            "nw" -> {e--}
            "se" -> {e++}
            "sw" -> {n--; e--}
            else -> throw RuntimeException("CMH: '$step'")
        }
        dist = hexDistance(e, n)
        max = kotlin.math.max(dist, max)
    }
    return Pair(dist, max)
}

fun hexDistance(e: Int, n: Int): Int {
    if (n >= 0 && e >= 0) return Math.max(n, e)
    if (n >= 0 && e < 0) return n + Math.abs(e)
    if (n < 0 && e >= 0) return Math.abs(n) + e
    if (n < 0 && e < 0) return Math.max(Math.abs(n), Math.abs(e))
    throw RuntimeException("CMH: ($e, $n)")
}
