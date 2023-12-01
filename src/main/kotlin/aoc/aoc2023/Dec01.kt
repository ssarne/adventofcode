package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 142)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 281)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        var first = -1
        var last = -1
        for (c in line) {
            if (c.isDigit()) {
                val n = Character.getNumericValue(c)
                if (first == -1) first = n
                last = n
            }
        }
        sum += 10 * first + last
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    var sum = 0
    val map = mapOf(
        "zero" to 0,
        "one" to 1,
        "two" to 2,
        "three" to 3,
        "four" to 4,
        "five" to 5,
        "six" to 6,
        "seven" to 7,
        "eight" to 8,
        "nine" to 9)

    for (line in input) {
        var first = -1
        var last = -1
        for (i in line.indices) {
            var n = -1
            if (line[i].isDigit()) n = Character.getNumericValue(line[i])
            for (digit in map.keys)
                if (line.length >= i + digit.length)
                    if (line.substring(i, i + digit.length) == digit)
                        n = map[digit]!!
            if (n != -1) {
                if (first == -1) first = n
                last = n
            }
        }
        sum += 10 * first + last
    }
    return sum
}