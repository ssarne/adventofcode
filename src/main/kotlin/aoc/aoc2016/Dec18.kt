package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute(testLines(2), 3).let { println("Test:   $it"); check(it, 6L) }
    execute(testLines(3), 10).let { println("Test:   $it"); check(it, 38L) }
    execute(readLines(), 40).let { println("Result: $it"); check(it, answer(1)) }
    execute(readLines(), 400000).let { println("Result: $it"); check(it, answer(2)) }
}

private fun execute(input: List<String>, size: Int): Long {

    var map = input.first()
    var sum = map.count { it == '.' }.toLong()
    val width = map.length

    for (y in 1 until size) {
        val next = StringBuilder()
        for (x in 0 until width) {
            if (matchTrap(map, x, "^^.") || // Its left and center tiles are traps, but its right tile is not.
                matchTrap(map, x, ".^^") || // Its center and right tiles are traps, but its left tile is not.
                matchTrap(map, x, "^..") || // Only its left tile is a trap.
                matchTrap(map, x, "..^")) //  // Only its right tile is a trap.
                next.append('^')
            else
                next.append('.')
        }
        map = next.toString()
        sum += map.count { it == '.' }
    }
    return sum
}

private fun matchTrap(map: String, x: Int, pattern: String): Boolean {
    var trap = true
    for (dx in -1..1) {
        val c = if (x + dx < 0 || x + dx >= map.length || map[x + dx] == '.')
            '.' else '^'
        if (pattern[dx + 1] != c)
            trap = false
    }
    return trap
}
