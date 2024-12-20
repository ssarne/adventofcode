package aoc.ktutils

import java.util.BitSet
import kotlin.collections.HashMap
import kotlin.collections.HashSet

fun parseSparseBinaryMatrix(input: List<String>): HashSet<Point> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    val grid = HashSet<Point>()
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

fun parseCharacterGridToMap(input: List<String>, filter: Char? = null): HashMap<Point, Char> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    val grid = HashMap<Point, Char>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            grid[Point(x, y)] = input[y][x]
        }
    }
    return grid
}

fun findFirst(grid: Map<Point, Char>, char: Char): Point {
    for ((p, c) in grid.entries)
        if (char == c)
            return p
    throw RuntimeException("Could not find '$char'")
}


fun transposeGrid(map: HashMap<Point, Char>): HashMap<Point, Char> {
    val transposed = HashMap<Point, Char>()
    for ((p, v) in map)
        transposed[Point(p.y, p.x)] = v
    return transposed
}

fun gridClone(map: HashMap<Point, Char>): HashMap<Point, Char> {
    val clone = HashMap<Point, Char>()
    for ((p, v) in map)
        clone[Point(p.x, p.y)] = v
    return clone
}

fun gridEquals(map1: HashMap<Point, Char>, map2: HashMap<Point, Char>): Boolean {
    if (map1.size != map2.size)
        return false
    for ((p, v) in map1)
        if (map2[p] != v)
            return false
    return true
}

fun rotateCounterClockwise(map: HashMap<Point, Char>): HashMap<Point, Char> {
    val size = mapSize(map)
    val rotated = HashMap<Point, Char>()
    for ((p, v) in map)
        rotated[Point(p.y, size.second.x - p.x)] = v
    return rotated
}

fun rotateClockwise(map: HashMap<Point, Char>): HashMap<Point, Char> {
    val size = mapSize(map)
    val rotated = HashMap<Point, Char>()
    for ((p, v) in map)
        rotated[Point(size.second.y - p.y, p.x)] = v
    return rotated
}

fun <T> centerPoint(grid: Map<Point, T>): Point {
    val maxX = grid.keys.map { it.x }.max()
    val maxY = grid.keys.map { it.y }.max()
    val minX = grid.keys.map { it.x }.min()
    val minY = grid.keys.map { it.y }.min()
    return Point((maxX - minX) / 2, (maxY - minY) / 2)
}

fun <T> mapSize(grid: Map<Point, T>): Pair<Point, Point> {
    val maxX = grid.keys.map { it.x }.max()
    val maxY = grid.keys.map { it.y }.max()
    val minX = grid.keys.map { it.x }.min()
    val minY = grid.keys.map { it.y }.min()
    return Point(minX, minY) to Point(maxX, maxY)
}

fun mapSize(grid: Set<Point>): Pair<Point, Point> {
    val maxX = grid.maxOf { it.x }
    val maxY = grid.maxOf { it.y }
    val minX = grid.minOf { it.x }
    val minY = grid.minOf { it.y }
    return Point(minX, minY) to Point(maxX, maxY)
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
