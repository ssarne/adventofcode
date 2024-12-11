package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1), 25), -1)
    execute1(readLines(), 25).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    // check(execute2(readTestLines(1)), -1)
    execute2(readLines(), 75).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>, iter: Int): Long {
    var stones = input.first().split(" ").map { it.toLong() }.toMutableList()

    for (i in 0 until iter) {
        val next = mutableListOf<Long>()
        for (s in stones) {
            val ss = s.toString()

            if (s == 0L)
                next.add(1)
            else if (ss.length % 2 == 0) {
                next.add(ss.substring(0, ss.length / 2).toLong())
                next.add(ss.substring(ss.length / 2).toLong())
            } else
                next.add(s * 2024)
            stones = next
        }

        println("$i ${stones.size}")
    }

    return stones.size.toLong()
}

private fun execute2(input: List<String>, iter: Int): Long {

    var numbers = input.first().split(" ").map { it.toLong() }.toList()
    var stones = mutableMapOf<Long, Long>()
    for (n in numbers) {
        val n2 = if (stones.containsKey(n)) stones[n]!! + 1 else 1L
        stones[n] = n2
    }

    for (i in 0 until iter) {
        val next = mutableMapOf<Long, Long>()
        for (e in stones) {
            val s = e.key
            val ss = s.toString()

            if (s == 0L) {
                val n = 1L
                val n2 = if (next.containsKey(n)) next[n]!! + e.value else e.value
                next[n] = n2
            } else if (ss.length % 2 == 0) {
                var n = ss.substring(0, ss.length / 2).toLong()
                var n2 = if (next.containsKey(n)) next[n]!! + e.value else e.value
                next[n] = n2

                n = ss.substring(ss.length / 2).toLong()
                n2 = if (next.containsKey(n)) next[n]!! + e.value else e.value
                next[n] = n2
            } else {
                var n = s * 2024
                var n2 = if (next.containsKey(n)) next[n]!! + e.value else e.value
                next[n] = n2
            }
            stones = next
        }

        println("$i ${stones.size}")
    }
    
    var sum = stones.values.sum()
    return sum
}