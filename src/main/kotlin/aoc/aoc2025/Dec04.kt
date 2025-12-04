package aoc.aoc2025


import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(readTestLines(1)).let { check(it, -1L); println("Test: $it") }
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, -1L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val grid = parseCharacterGridToMap(input)
    val size = mapSize(grid)
    var count = 0L
    for (x in size.first.x..size.second.x)
        for (y in size.first.y..size.second.y) {
            if (grid[Point(x, y)] == '@') {
                val around = Point(x, y).surrounding()
                var n = 0
                for (p in around)
                    if (grid.get(p) == '@')
                        n++
                if (n < 4)
                    count++
            }
        }
    return count
}

private fun execute2(input: List<String>): Long {
    val grid = parseCharacterGridToMap(input)
    val size = mapSize(grid)
    var count = 0L
    var changed = true
    while (changed) {

        changed = false

        for (x in size.first.x..size.second.x)
            for (y in size.first.y..size.second.y) {
                if (grid[Point(x, y)] == '@') {
                    val around = Point(x, y).surrounding()
                    var n = 0
                    for (p in around)
                        if (grid.get(p) == '@')
                            n++
                    if (n < 4) {
                        grid.remove(Point(x, y))
                        count++
                        changed = true
                        println("Remove " + Point(x, y))
                    }
                }
            }
    }
    return count
}