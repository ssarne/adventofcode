package aoc.aoc2021

import aoc.ktutils.*

fun main() {
    check(execute(readTestLines(1), 70), 58)
    execute(readLines(), 10000).let { println(it); check(it, readAnswerAsInt(1)) }
}

private fun execute(input: List<String>, max: Int = 100, print: Boolean = false): Int {
    var grid = CharMatrix.create(input)
    var steps = 0
    do {
        if (print) grid.print(true, " i=$steps")
        if (steps > max) break
        var changed = false
        val next = CharMatrix.create(grid.width, grid.height, '.')
        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (grid.get(x, y) == '>') {
                    if (grid.get((x + 1) % grid.width, y) == '.') {
                        next.set((x + 1) % next.width, y, grid.get(x, y))
                        changed = true
                    } else {
                        next.set(x, y, grid.get(x, y))
                    }
                }
            }
        }

        for (y in 0 until grid.height) {
            for (x in 0 until grid.width) {
                if (grid.get(x, y) == 'v') {
                    if (grid.get(x, (y + 1) % grid.height) != 'v' && next.get(x, (y + 1) % next.height) == '.') {
                        next.set(x, (y + 1) % next.height, grid.get(x, y))
                        changed = true
                    } else {
                        next.set(x, y, grid.get(x, y))
                    }
                }
            }
        }
        grid = next
        steps++
    } while (changed)

    return steps
}