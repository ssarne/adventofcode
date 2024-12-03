package aoc.aoc2024


import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 161)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(2)), 48)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        for (i in line.indices) {
            if (!line.startsWith("mul", i)) continue
            val open = line.indexOf('(', i)
            val close = line.indexOf(')', i)
            if (open != i + 3) continue
            if (close < i + 4 && close > i + 10) continue
            val numbers = line.substring(open + 1, close).split(",")
            if (numbers.size != 2) continue
            if (!numbers[0].isDigitsOnly()) continue
            if (!numbers[1].isDigitsOnly()) continue
            val v1 = numbers[0].toInt()
            val v2 = numbers[1].toInt()
            sum += v1 * v2
        }
    }
    return sum
}

private fun execute2(input: List<String>): Long {
    var sum = 0L
    var doit = true
    for (line in input) {
        for (i in line.indices) {
            if (line.length > i + "don't".length && line.substring(i, i + "don't".length) == "don't") {
                doit = false
            } else if (line.length > i + "do".length && line.substring(i, i + "do".length) == "do") {
                doit = true
            } else if (doit && line.startsWith("mul", i)) {
                val open = line.indexOf('(', i)
                val close = line.indexOf(')', i)
                if (open != i + 3) continue
                if (close < i + 4 && close > i + 10) continue
                val numbers = line.substring(open + 1, close).split(",")
                if (numbers.size != 2) continue
                if (!numbers[0].isDigitsOnly()) continue
                if (!numbers[1].isDigitsOnly()) continue
                val v1 = numbers[0].toInt()
                val v2 = numbers[1].toInt()
                sum += v1 * v2
            } else {
                continue
            }
        }
    }
    return sum
}