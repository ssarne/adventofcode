package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines()), 739785)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 444356092776315L)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private class IncrementingDie(var rolls:Int = 0) {
    fun next(): Int = ++rolls
}

private fun execute1(input: List<String>): Int {
    var p1 = input[0].split(" ")[4].toInt()
    var p2 = input[1].split(" ")[4].toInt()
    var s1 = 0
    var s2 = 0
    val die = IncrementingDie()
    while (s1 < 1000 && s2 < 1000) {
        p1 += die.next() + die.next() + die.next()
        while (p1 > 10) p1 -= 10
        s1 += p1
        if (s1 >= 1000) break
        p2 += die.next() + die.next() + die.next()
        while (p2 > 10) p2 -= 10
        s2 += p2
    }
    if (s1 >= 1000) return s2 * die.rolls
    else return s1 * die.rolls
}

private fun execute2(input: List<String>): Long {
    val cache = HashMap<String, Pair<Long, Long>>()
    val p1 = input[0].split(" ")[4].toInt()
    val p2 = input[1].split(" ")[4].toInt()
    val (w1, w2) = play(p1, p2, 0, 0, 1, 0, 0, cache)
    return max(w1, w2)
}

private fun play(p1: Int, p2: Int, s1: Int, s2: Int, player: Int, roll: Int, sum: Int, cache: HashMap<String, Pair<Long, Long>>): Pair<Long, Long> {

    val key = "$p1,$p2,$s1,$s2,$player,$roll,$sum"
    cache[key].let { if (it != null) return it }

    if (roll == 3) {
        if (player == 1) {
            var np1 = p1 + sum
            while (np1 > 10) np1 -= 10
            var ns1 = s1 + np1
            if (ns1 >= 21) return 1L to 0L
            return play(np1, p2, ns1, s2, 2, 0, 0, cache)
        } else { // player == 2
            var np2 = p2 + sum
            while (np2 > 10) np2 -= 10
            var ns2 = s2 + np2
            if (ns2 >= 21) return 0L to 1L
            return play(p1, np2, s1, ns2, 1, 0, 0, cache)
        }
    }

    if (player == 1) {
        var r1 = play(p1, p2, s1, s2, 1, roll+1, sum+1, cache)
        var r2 = play(p1, p2, s1, s2, 1, roll+1, sum+2, cache)
        var r3 = play(p1, p2, s1, s2, 1, roll+1, sum+3, cache)
        var res = Pair(r1.first + r2.first + r3.first, r1.second + r2.second + r3.second)
        cache.put(key, res)
        return res
    } else {
        var r1 = play(p1, p2, s1, s2, 2, roll+1, sum+1, cache)
        var r2 = play(p1, p2, s1, s2, 2, roll+1, sum+2, cache)
        var r3 = play(p1, p2, s1, s2, 2, roll+1, sum+3, cache)
        var res = Pair(r1.first + r2.first + r3.first, r1.second + r2.second + r3.second)
        cache.put(key, res)
        return res
    }
}