package aoc.aoc2022.dec08

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 21)
    execute1(readLines()).let { println(it); check(it, readAnswer(1).toInt()) }

    check(execute2(readTestLines()), 8)
    execute2(readLines()).let { println(it); check(it, readAnswer(2).toInt()) }
}

private fun execute1(input: List<String>): Int {

    var num = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (isVisible(input, 0 until x, y..y, input[y][x])
                || isVisible(input, x + 1 until input[y].length, y..y, input[y][x])
                || isVisible(input, x..x, 0 until y, input[y][x])
                || isVisible(input, x..x, y + 1 until input.size, input[y][x])
            )
                num++
        }
    }
    return num
}

fun isVisible(input: List<String>, xRange: IntRange, yRange: IntRange, height: Char): Boolean {
    for (x in xRange)
        for (y in yRange)
            if (input[y][x] >= height)
                return false
    return true
}

private fun execute2(input: List<String>): Int {

    var max = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            var num = sight(input, x, y, -1, 0, 0 until x, y..y) *
                    sight(input, x, y, 1, 0, x + 1 until input[y].length, y..y) *
                    sight(input, x, y, 0, -1, x..x, 0 until y) *
                    sight(input, x, y, 0, 1, x..x, y + 1 until input.size)
            if (num > max)
                max = num

        }
    }
    return max
}

fun sight(input: List<String>, x: Int, y: Int, dx: Int, dy: Int, xRange: IntRange, yRange: IntRange): Int {
    var sight = 0
    var nx = x + dx
    var ny = y + dy
    while (nx in xRange && ny in yRange) {
        sight++
        if (input[ny][nx] >= input[y][x])
            return sight
        nx += dx
        ny += dy
    }
    return sight
}


