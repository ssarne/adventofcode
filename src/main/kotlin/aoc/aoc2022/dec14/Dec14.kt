package aoc.aoc2022.dec14

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines()), 24)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 93)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val cave = parse(input)
    val limit = cave.keys.map { it.y }.max()!!
    var full = false
    // printSparseMatrix(cave)
    var sum = 0
    while (!full) {
        var sand = Point(500, 0)
        while (true) {
            if (sand.y > limit) {
                full = true
                break
            }
            if (!cave.containsKey(Point(sand.x, sand.y + 1))) {
                sand = Point(sand.x, sand.y + 1)
                continue
            } else if (!cave.containsKey(Point(sand.x - 1, sand.y + 1))) {
                sand = Point(sand.x - 1, sand.y + 1)
                continue
            } else if (!cave.containsKey(Point(sand.x + 1, sand.y + 1))) {
                sand = Point(sand.x + 1, sand.y + 1)
                continue
            } else {
                cave[sand] = 'o'
                sum++
                break
            }
        }
    }
    // printSparseMatrix(cave)
    return sum
}

private fun execute2(input: List<String>): Int {
    val cave = parse(input)
    val bottom = 2 + cave.keys.map { it.y }.max()!!
    var sum = 0
    val tap = Point(500, 0)

    while (cave[tap] == '+') {
        var sand = tap
        while (true) {
            if (sand.y + 1 == bottom) {
                break
            } else if (!cave.containsKey(Point(sand.x, sand.y + 1))) {
                sand = Point(sand.x, sand.y + 1)
            } else if (!cave.containsKey(Point(sand.x - 1, sand.y + 1))) {
                sand = Point(sand.x - 1, sand.y + 1)
            } else if (!cave.containsKey(Point(sand.x + 1, sand.y + 1))) {
                sand = Point(sand.x + 1, sand.y + 1)
            } else {
                break
            }
        }
        cave.put(sand, 'o')
        sum++
    }

    //printSparseMatrix(cave)
    return sum
}

private fun parse(input: List<String>): MutableMap<Point, Char> {
    val cave = HashMap<Point, Char>()
    for (line in input) {
        val points = line.split(" -> ")
        var start = Point.create(points[0])
        for (i in 1 until points.size) {
            val next = Point.create(points[i])
            val delta = next - start
            for (dx in min(0, delta.x)..max(0, delta.x))
                for (dy in min(0, delta.y)..max(0, delta.y))
                    Point(start.x + dx, start.y + dy).let { cave[it] = '#' }
            start = next
        }
    }
    cave[Point(500, 0)] = '+'
    return cave
}