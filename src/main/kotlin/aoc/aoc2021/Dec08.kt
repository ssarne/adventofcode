package aoc.aoc2021

import aoc.ktutils.*
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 26)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(readEntry("acedgfb cdfbe gcdfa fbcad dab cefabd cdfgeb eafb cagedb ab | cdfeb fcadb cdfeb cdbaf"), 5353)

    check(execute2(readTestLines()), 61229)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(lines: List<String>): Int {

    var count = 0
    for (line in lines) {
        val output = line.split(" | ")[1]
        for (token in output.split(" ")) {
            when (token.length) {
                2 -> count++  // 1
                3 -> count++  // 7
                4 -> count++  // 4
                7 -> count++  // 8
            }
        }
    }
    return count
}

private fun execute2(lines: List<String>): Int {

    return lines.stream()
        .mapToInt { readEntry(it) }
        .sum()
}

private fun readEntry(line: String): Int {

    val input = line.split(" | ")[0].split(" ")
    val output = line.split(" | ")[1].split(" ")
    check(input.size, 10)
    check(output.size, 4)

    val digits = deduce(input)

    return 1000 * resolve(output[0], digits) +
            100 * resolve(output[1], digits) +
            10 * resolve(output[2], digits) +
            1 * resolve(output[3], digits)
}

private fun deduce(input: List<String>): HashMap<Int, String> {

    val digits = HashMap<Int, String>()

    for (digit in input) {
        when (digit.length) { // 1, 4, 7, 8
            2 -> digits[1] = digit
            4 -> digits[4] = digit
            3 -> digits[7] = digit
            7 -> digits[8] = digit
        }
    }

    for (digit in input) {
        if (digit.length == 6) {  // 0, 6, 9
            if (isSuperset(digit, digits[4]!!)) {
                digits[9] = digit
            } else if (isSuperset(digit, digits[7]!!)) {
                digits[0] = digit
            } else {
                digits[6] = digit
            }
        }
    }

    for (digit in input) {
        if (digit.length == 5) {  // 2, 3, 5
            if (isSuperset(digit, digits[1]!!)) {
                digits[3] = digit
            } else if (isSuperset(digits[9]!!, digit)) {
                digits[5] = digit
            } else {
                digits[2] = digit
            }
        }
    }

    return digits
}

fun isSuperset(superset: String, subset: String): Boolean {
    for (c in subset) {
        if (!superset.contains(c)) {
            return false
        }
    }
    return true
}

fun resolve(input: String, map: Map<Int, String>): Int {
    for (i in map.keys) {
        if (map[i]!!.toSet() == input.toSet()) {
            return i
        }
    }
    throw RuntimeException("Cannot resolve $input")
}