package aoc.ktutils

import java.util.BitSet
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun parseSparseBinaryMatrix(input: List<String>): HashSet<Point> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    var grid = HashSet<Point>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            val c = input[y][x]
            if (c == '#') {
                grid.add(Point(x, y))
            }
        }
    }
    return grid
}

fun parseCharacterGridToMap(input: List<String>): HashMap<Point, Char> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    var grid = HashMap<Point, Char>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            grid.put(Point(x, y), input[y][x])
        }
    }
    return grid
}

fun printSparseMatrix(dots: Map<Point, Char>, header: Boolean = false, msg: String = "") {
    val minX = dots.keys.stream().mapToInt { it.x }.min().orElse(0)
    val minY = dots.keys.stream().mapToInt { it.y }.min().orElse(0)
    val maxX = dots.keys.stream().mapToInt { it.x }.max().orElse(1)
    val maxY = dots.keys.stream().mapToInt { it.y }.max().orElse(1)
    if (header) for (x in minX..maxX) print("-"); println("  $msg")
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (dots.contains(Point(x, y))) {
                print(dots[Point(x, y)])
            } else {
                print(' ')
            }
        }
        println()
    }
}

fun printSparseMatrixReversed(dots: Map<Point, Char>, header: Boolean = false) {
    val minX = dots.keys.stream().mapToInt { it.x }.min().orElse(0)
    val minY = dots.keys.stream().mapToInt { it.y }.min().orElse(0)
    val maxX = dots.keys.stream().mapToInt { it.x }.max().orElse(1)
    val maxY = dots.keys.stream().mapToInt { it.y }.max().orElse(1)
    if (header) for (x in minX..maxX) print("-"); println("")
    for (y1 in minY..maxY) {
        var y = maxY - y1
        for (x in minX..maxX) {
            if (dots.contains(Point(x, y))) {
                print(dots[Point(x, y)])
            } else {
                print(' ')
            }
        }
        println()
    }
}

fun printBitSetMatrixReversed(dots: BitSet, width: Int, header: Boolean = false) {
    val minX = 0
    val minY = 0
    val maxX = width
    val maxY = 1 + dots.length() / width
    if (header) for (x in minX..maxX) print("-"); println("")
    for (y1 in minY..maxY) {
        var y = maxY - y1
        for (x in minX until maxX) {
            if (dots[y * width + x]) {
                print('#')
            } else {
                print(' ')
            }
        }
        println()
    }
}

fun printSparseSet(dots: Set<Point>, header: Boolean = false) {
    val minX = dots.stream().mapToInt { it.x }.min().orElse(0)
    val minY = dots.stream().mapToInt { it.y }.min().orElse(0)
    val maxX = dots.stream().mapToInt { it.x }.max().orElse(1)
    val maxY = dots.stream().mapToInt { it.y }.max().orElse(1)
    if (header) for (x in minX..maxX) print("-"); println("")
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
