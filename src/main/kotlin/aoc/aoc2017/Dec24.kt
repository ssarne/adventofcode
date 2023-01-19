package aoc.aoc2017

import aoc.ktutils.*
import java.util.BitSet

fun main() {
    check(execute1(readTestLines()), 31)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 19)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(bridges: List<String>): Int {
    return build(bridges, BitSet(), 0, 0, 0)
    { a, b -> if (a.second > b.second) a else b }
        .second
}

private fun execute2(bridges: List<String>): Int {
    return build(bridges, BitSet(), 0, 0, 0)
    { a, b -> if (a.first > b.first || a.first == b.first && a.second > b.second) a else b }
        .second
}

fun build(
    bridges: List<String>, used: BitSet, connector: Int, strength: Int, length: Int,
    eval: (a: Pair<Int, Int>, b: Pair<Int, Int>) -> Pair<Int, Int>
): Pair<Int, Int> {
    var best = Pair(length, strength)
    for (i in bridges.indices) {
        if (used[i]) continue
        val (a, b) = bridges[i].trim().split("/").map { it.toInt() }
        if (a == connector) {
            used[i] = true
            val res = build(bridges, used, b, strength + a + b, length + 1, eval)
            best = eval(res, best)
            used[i] = false
        }
        if (b == connector) {
            used[i] = true
            val res = build(bridges, used, a, strength + a + b, length + 1, eval)
            best = eval(res, best)
            used[i] = false
        }
    }
    return best
}