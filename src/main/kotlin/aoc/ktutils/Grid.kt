package aoc.ktutils

import java.lang.RuntimeException

fun createIntMatrixFromDigits(input: List<String>): Array<IntArray> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    val height = input.size
    val width = input[0].length
    val grid = Array(width) { IntArray(height) }
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            grid[x][y] = Character.getNumericValue(input[y][x])
        }
    }
    return grid
}

fun printSparseSet(dots: Set<Point>) {
    val minX = dots.stream().mapToInt { it.x }.min().orElse(0)
    val minY = dots.stream().mapToInt { it.y }.min().orElse(0)
    val maxX = dots.stream().mapToInt { it.x }.max().orElse(1)
    val maxY = dots.stream().mapToInt { it.y }.max().orElse(1)
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (dots.contains(Point(x, y))) {
                print('#')
            } else {
                print(' ')
            }
        }
        println()
    }
}
