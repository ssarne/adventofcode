package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute(readTestLines(1), 25), 55312)

    execute(readLines(), 25).let { println(it) ; check(it, readAnswerAsLong(1)) }
    execute(readLines(), 75).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>, iter: Int): Long {

    var stones = mutableMapOf<Long, Long>() // number, occurrences
    val numbers = input.first().split(" ").map { it.toLong() }.toList()
    for (n in numbers) stones.addOrPut(n, 1L)

    for (i in 0 until iter) {
        val next = mutableMapOf<Long, Long>()
        for (e in stones) {
            val text = e.key.toString()

            if (e.key == 0L) {
                next.addOrPut(1L, e.value)
            } else if (text.length % 2 == 0) {
                next.addOrPut(text.substring(0, text.length / 2).toLong(), e.value)
                next.addOrPut(text.substring(text.length / 2).toLong(), e.value)
            } else {
                next.addOrPut(e.key * 2024, e.value)
            }
            stones = next
        }
    }

    return stones.values.sum()
}