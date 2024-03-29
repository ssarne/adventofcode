package aoc.aoc2021

import aoc.ktutils.*

fun main() {
    check(execute(readTestLines(), 100).count, 1656)
    execute(readLines(), 100).count.let { println(it); check(it, readAnswerAsInt(1)) }
    check(execute(readTestLines(), Int.MAX_VALUE).step, 195)
    execute(readLines(), Int.MAX_VALUE).step.let { println(it); check(it, readAnswerAsInt(2)) }
}

private data class Res11(val count: Int, val step: Int)

private fun execute(input: List<String>, loops: Int): Res11 {

    val grid = IntMatrix.create(input)
    var count = 0
    var step = 0

    for (i in 1..loops) {

        for (y in 0 until grid.height) {  // increase
            for (x in 0 until grid.width) {
                grid.inc(x, y)
            }
        }

        var flashSize = 0
        for (y in 0 until grid.height) { // flash
            for (x in 0 until grid.width) {
                if (grid.get(x, y) > 9) {
                    val size = flash(grid, x, y)
                    count += size
                    flashSize += size
                }
            }
        }

        if (flashSize == 100 && step == 0) {
            step = i
            break
        }
    }

    return Res11(count, step)
}

fun flash(grid: IntMatrix, x: Int, y: Int): Int {

    if (grid.get(x, y) == 0) return 0

    var count = 1
    grid.set(x, y, 0)
    count += inc(grid, x - 1, y - 1)
    count += inc(grid, x - 1, y)
    count += inc(grid, x - 1, y + 1)
    count += inc(grid, x, y - 1)
    count += inc(grid, x, y + 1)
    count += inc(grid, x + 1, y - 1)
    count += inc(grid, x + 1, y)
    count += inc(grid, x + 1, y + 1)

    return count
}

fun inc(grid: IntMatrix, x: Int, y: Int): Int {
    if (x < 0 || x >= grid.width) return 0
    if (y < 0 || y >= grid.height) return 0
    if (grid.get(x, y) == 0) return 0
    grid.inc(x, y)
    return if (grid.get(x, y) > 9) flash(grid, x, y) else 0
}