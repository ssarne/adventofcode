package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    check(checksum(readTestLines()), 18)
    println(checksum(readLines())) // 34581
    check(divisesum(readTestLines(2)), 9)
    println(divisesum(readLines())) // 214
}

private fun checksum(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        var min: Int? = null
        var max: Int? = null
        for (num in line.split(" ", "\t")) {
            val n = num.toInt()
            min = kotlin.math.min(min ?: n, n)
            max = kotlin.math.max(max ?: n, n)
        }
        sum += (max ?: 0) - (min ?: 0)
    }
    return sum
}

private fun divisesum(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        for (num1 in line.split(" ", "\t")) {
            for (num2 in line.split(" ", "\t")) {
                val a = num1.toInt()
                val b = num2.toInt()
                val c = a / b
                if (a != b && c * b == a) // assume no duplicates in row
                    sum += c
            }
        }
    }
    return sum
}
