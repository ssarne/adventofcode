package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 405)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 400)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val chunks = asChunks(input)
    var sum = 0
    for (chunk in chunks) {
        val area = parseCharacterGridToMap(chunk)
        sum += findMirror(area)
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    val chunks = asChunks(input)
    var sum = 0
    for (chunk in chunks) {
        val area = parseCharacterGridToMap(chunk)
        val org = findMirror(area)
        for (p in area.keys) {
            val c = area[p]!!
            val n = if (c == '#') '.' else '#'
            area[p] = n
            val res = findMirror(area, org)
            area[p] = c
            if (res > 0) {
                sum += res
                break;
            }
        }
    }
    return sum
}

fun findMirror(area: HashMap<Point, Char>, ignore: Int = 0): Int {
    // Check for horizontal mirrors
    val size = mapSize(area)
    for (x in 1..size.second.x) {
        val mirror = isMirror(size, x, area)
        if (mirror && ignore != x) {
            return x
        }
    }

    // Check for vertical mirrors (using a transposed map)
    val tArea = transposeGrid(area)
    val tSize = mapSize(tArea)
    for (x in 1..tSize.second.x) {
        val mirror = isMirror(tSize, x, tArea)
        if (mirror && ignore != 100 * x) {
            return 100 * x
        }
    }

    return 0
}

private fun isMirror(size: Pair<Point, Point>, x: Int, area: HashMap<Point, Char>): Boolean {
    val dist = min(x - 1, size.second.x - x)
    for (y in 0..size.second.y) {
        for (xi in 0..dist) {
            val l = area[Point(x - xi - 1, y)]
            val r = area[Point(x + xi, y)]
            if (l != r) {
                return false
            }
        }
    }
    return true
}

