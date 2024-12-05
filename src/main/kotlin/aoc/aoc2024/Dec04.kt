package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 18)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 9)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    var matches = 0L
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (input[y][x] == 'X') {
                for (dx in -1..1) {
                    for (dy in -1..1) {
                        if (x + 3 * dx in input[y].indices && y + 3 * dy in input.indices) {
                            if (input[y + 1 * dy][x + 1 * dx] == 'M'
                                && input[y + 2 * dy][x + 2 * dx] == 'A'
                                && input[y + 3 * dy][x + 3 * dx] == 'S'
                            )
                                matches++
                        }
                    }
                }
            }
        }
    }
    return matches
}

private fun execute2(input: List<String>): Long {
    var matches = 0L
    for (y in 1 until input.size - 1) {
        for (x in 1 until input[0].length - 1) {
            if (input[y][x] == 'A') {
                if (input[y + 1][x + 1] == 'M' && input[y - 1][x - 1] == 'S' && input[y + 1][x - 1] == 'M' && input[y - 1][x + 1] == 'S') matches++
                if (input[y + 1][x + 1] == 'M' && input[y - 1][x - 1] == 'S' && input[y - 1][x + 1] == 'M' && input[y + 1][x - 1] == 'S') matches++

                if (input[y + 1][x - 1] == 'M' && input[y - 1][x + 1] == 'S' && input[y + 1][x + 1] == 'M' && input[y - 1][x - 1] == 'S') matches++
                if (input[y + 1][x - 1] == 'M' && input[y - 1][x + 1] == 'S' && input[y - 1][x - 1] == 'M' && input[y + 1][x + 1] == 'S') matches++

                if (input[y - 1][x + 1] == 'M' && input[y + 1][x - 1] == 'S' && input[y + 1][x + 1] == 'M' && input[y - 1][x - 1] == 'S') matches++
                if (input[y - 1][x + 1] == 'M' && input[y + 1][x - 1] == 'S' && input[y - 1][x - 1] == 'M' && input[y + 1][x + 1] == 'S') matches++

                if (input[y - 1][x - 1] == 'M' && input[y + 1][x + 1] == 'S' && input[y + 1][x - 1] == 'M' && input[y - 1][x + 1] == 'S') matches++
                if (input[y - 1][x - 1] == 'M' && input[y + 1][x + 1] == 'S' && input[y - 1][x + 1] == 'M' && input[y + 1][x - 1] == 'S') matches++
            }
        }
    }
    return matches / 2  // divide by two, each X is found twice, once per leg
}