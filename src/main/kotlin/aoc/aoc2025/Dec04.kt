package aoc.aoc2025

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 13L); println("Test: $it") }
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 43L); println("Test: $it") }
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val grid = parseCharacterGridToMap(input)
    var count = 0L

    for (p in grid) {
        if (p.value == '@') {
            val n = p.key.surrounding().count { grid[it] == '@' }
            if (n < 4) count++
        }
    }
    return count
}

private fun execute2(input: List<String>): Long {
    val grid = parseCharacterGridToMap(input)
    val size = mapSize(grid)
    var count = 0L

    do {
        var changed = false
        for (x in size.first.x..size.second.x) {
            for (y in size.first.y..size.second.y) {
                val p = Point(x, y)
                if (grid[p] == '@') {
                    val n = p.surrounding().count { grid[it] == '@' }
                    if (n < 4) {
                        grid.remove(p)
                        count++
                        changed = true
                    }
                }
            }
        }
    } while (changed)

    return count
}