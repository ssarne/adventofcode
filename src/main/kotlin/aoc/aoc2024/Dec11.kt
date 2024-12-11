package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute(readTestLines(1), 25), 55312)

    execute(readLines(), 25).let { println(it) ; check(it, readAnswerAsLong(1)) }
    execute(readLines(), 75).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>, iter: Int): Long {

    val numbers = input.first().split(" ").map { it.toLong() }.toList()

    var stones = mutableMapOf<Long, Long>()
    for (n in numbers) sumOrAdd(stones, n, 1L)

    for (i in 0 until iter) {
        val next = mutableMapOf<Long, Long>()  // number, occurrences
        for (e in stones) {
            val text = e.key.toString()

            if (e.key == 0L) {
                sumOrAdd(next, 1L, e.value)
            } else if (text.length % 2 == 0) {
                sumOrAdd(next, text.substring(0, text.length / 2).toLong(), e.value)
                sumOrAdd(next, text.substring(text.length / 2).toLong(), e.value)
            } else {
                sumOrAdd(next, e.key * 2024, e.value)
            }
            stones = next
        }
    }

    return stones.values.sum()
}

private fun sumOrAdd(map: MutableMap<Long, Long>, key: Long, inc: Long) {
    map[key] = if (map.containsKey(key)) map[key]!! + inc else inc
}