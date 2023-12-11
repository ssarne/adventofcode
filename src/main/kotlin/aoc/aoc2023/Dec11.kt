package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.math.*

fun main() {

    check(execute(readTestLines(1), 1), 374)
    execute(readLines(), 1).let { println(it) ; check(it, readAnswerAsLong(1)) }
    check(execute(readTestLines(1), 10 - 1), 1030)
    check(execute(readTestLines(1), 100 - 1), 8410)
    execute(readLines(), 1000000L - 1).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class PointL(val x: Long, val y: Long) {
    fun manhattan(other: PointL) = abs(this.x - other.x) + abs(this.y - other.y)
}

private fun execute(input: List<String>, addon: Long): Long {

    val map = parseSparseBinaryMatrix(input)
    val map2 = expand(map, addon)

    var sum = 0L
    val used = HashSet<PointL>()

    for (p1 in map2.iterator()) {
        if (used.contains(p1)) continue
        used.add(p1)
        for (p2 in map2.iterator()) {
            if (used.contains(p2)) continue
            sum += p1.manhattan(p2)
        }
    }
    return sum
}

private fun expand(map: HashSet<Point>, delta: Long): HashSet<PointL> {

    // Find rows and columns to expand, mark the ones that have galaxies
    val size = mapSize(map)
    val emptyYs = BooleanArray(size.second.y + 1) { true }
    val emptyXs = BooleanArray(size.second.x + 1) { true }
    for (p in map.iterator()) {
        emptyXs[p.x] = false
        emptyYs[p.y] = false
    }

    // Transform to new map (with long coordinates as of part 2)
    val map2 = HashSet<PointL>()
    for (p in map.iterator()) {
        val dx = delta * emptyXs.withIndex().count { (i, v) -> i < p.x && v }
        val dy = delta * emptyYs.withIndex().count { (i, v) -> i < p.y && v }
        map2.add(PointL(p.x + dx, p.y + dy))
    }
    return map2
}




