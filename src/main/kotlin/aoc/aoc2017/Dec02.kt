package aoc.aoc2017

import aoc.ktutils.check
import aoc.utils.Utils

fun main() {
    check(checksum("aoc2017/dec02_test.txt"), 18)
    println(checksum(null))
    check(divisesum("aoc2017/dec02_test2.txt"), 9)
    println(divisesum(null))
}

private fun checksum(fileName: String?): Int {
    var sum = 0
    for (line in Utils.getLines(fileName)) {
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

private fun divisesum(fileName: String?): Int {
    var sum = 0
    for (line in Utils.getLines(fileName)) {
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
