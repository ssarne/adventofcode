package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 2)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 4)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

fun increases(numbers: IntArray): Boolean {
    for (i in 1 until numbers.size)
        if (numbers[i] <= numbers[i - 1])
            return false
    return true
}

fun decreases(numbers: IntArray): Boolean {
    for (i in 1 until numbers.size)
        if (numbers[i] >= numbers[i - 1])
            return false
    return true
}

private fun safe(numbers: IntArray): Boolean {
    if (!(increases(numbers) || decreases(numbers)))
        return false

    for (i in 1 until numbers.size) {
        val delta = abs(numbers[i] - numbers[i - 1])
        if (delta < 1 || delta > 3)
            return false
    }
    return true
}

private fun execute1(input: List<String>): Long {

    var sum = 0L
    for (line in input) {
        val numbers = line.trim().split(" ").map { t -> t.toInt() }.toIntArray()
        if (safe(numbers)) sum++
    }

    return sum
}


private fun execute2(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val numbers = line.trim().split(" ").map { t -> t.toInt() }.toIntArray()
        if (safe(numbers)) {
            sum++
            continue
        }
        for (ignore in numbers.indices) {

            val numbers2 = numbers.slice(0 until ignore) + numbers.slice(ignore + 1 until numbers.size)

            if (safe(numbers2.toIntArray())) {
                sum++
                break
            }
        }
    }

    return sum
}
