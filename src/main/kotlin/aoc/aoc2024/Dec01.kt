package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 11)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 31)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val firsts = mutableListOf<Int>()
    val seconds = mutableListOf<Int>()
    for (line in input) {
        val pair = line.trim().split("   ")
        firsts.add(pair[0].toInt())
        seconds.add(pair[1].toInt())
    }
    firsts.sort()
    seconds.sort()
    var sum = 0L
    for (i in firsts.indices) {
        sum += (abs(firsts[i] - seconds[i]))
    }
    return sum
}

private fun execute2(input: List<String>): Long {
    val firsts = mutableListOf<Int>()
    val seconds = mutableListOf<Int>()
    for (line in input) {
        val pair = line.trim().split("   ")
        firsts.add(pair[0].toInt())
        seconds.add(pair[1].toInt())
    }

    var sum = 0L
    for (i in firsts.indices) {
        var count = 0
        for (j in seconds.indices)
            if (firsts[i] == seconds[j])
                count++
        sum += firsts[i] * count
    }
    return sum
}
