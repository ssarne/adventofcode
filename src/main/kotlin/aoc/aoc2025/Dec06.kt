package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 4277556L) ; println("Test: $it") }
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 3263827L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val matrix = ArrayList<List<String>>()
    for (line in input)
        matrix.add(line.split(" ").filter { it != "" }.toList())

    var sum = 0L
    for (x in 0 until matrix[0].size) {
        val op = matrix.last()[x]
        var result = matrix.first()[x].toLong()
        for (y in 1 until matrix.size - 1) {
            when (op) {
                "*" -> result *= matrix[y][x].toLong()
                "+" -> result += matrix[y][x].toLong()
            }
        }
        sum += result
    }
    return sum
}

private fun execute2(input: List<String>): Long {

    val matrix = padded(input) // align length of lines and add 1

    var sum = 0L
    var op = ' '
    var result = -1L

    for (x in 0 until matrix[0].length) {

        if (matrix.last()[x] != ' ') { // start new column
            op = matrix.last()[x]
            result = if (op == '*') 1L else 0L
        }

        var num = ""
        for (y in 0 until matrix.size - 1) {
            if (matrix[y][x] != ' ') {
                num += matrix[y][x]
            }
        }

        if (num != "") {
            when (op) {
                '*' -> result *= num.toLong()
                '+' -> result += num.toLong()
            }
        } else {
            sum += result
        }

    }
    return sum
}

private fun padded(input: List<String>): MutableList<String> {
    val matrix = ArrayList<String>()
    val len = input.maxOf { it.length + 1 }
    for (line in input)
        matrix.add(line + " ".repeat(len - line.length))
    return matrix
}