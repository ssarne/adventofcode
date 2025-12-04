package aoc.aoc2025

import aoc.ktutils.*

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
    var count = 0L

    do {
        val size = grid.size
        val iterator = grid.iterator()
        while (iterator.hasNext()) {
            val p = iterator.next()
            if (p.value == '@') {
                val n = p.key.surrounding().count { grid[it] == '@' }
                if (n < 4) {
                    iterator.remove()
                    count++
                }
            }
        }
    } while (size != grid.size)

    return count
}