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
