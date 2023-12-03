package aoc.aoc2021

import aoc.ktutils.*
import java.lang.RuntimeException
import kotlin.math.max
import kotlin.math.min

fun main() {
    check(launch(6, 9, Area(Point(20, -10), Point(30,-5))).first, true)
    check(launch(6, 9, Area(Point(20, -10), Point(30,-5))).second, 45)
    check(execute("target area: x=20..30, y=-10..-5").first, 45)

    execute(readText()).let { println(it); check(it.first, readAnswerAsInt(1)); check(it.second, readAnswerAsInt(2)) }
}

private data class Area(val min: Point, val max: Point)

private fun execute(input: String): Pair<Int, Int> {

    val area = parseInput(input)
    var highest = 0
    var total = 0

    for (dx in initValueX(area.min.x)..area.max.x) {
        for (dy in area.min.y..1000) {
            val result = launch(dx, dy, area)
            if (result.first) {
                total += 1
                highest = max(result.second, highest)
            }
        }
    }
    return Pair(highest, total)
}

private fun parseInput(input: String): Area {
    val tokens = input.split(" ")
    val xrange = tokens[2].replace("x=", "").replace(",", "").split("..")
    val yrange = tokens[3].replace("y=", "").split("..")
    return Area(
        Point(xrange[0].toInt(), yrange[0].toInt()),
        Point(xrange[1].toInt(), yrange[1].toInt())
    )
}

private fun initValueX(xmin: Int): Int {
    for (x in 1..xmin) {
        var xsum = 0
        var dx = x
        while (dx > 0) xsum += dx--
        if (xsum >= xmin) return x
    }
    throw RuntimeException("Failed to find lowest x value")
}

private fun launch(lx: Int, ly: Int, target: Area): Pair<Boolean, Int> {
    var x = 0
    var y = 0
    var dx = lx
    var dy = ly
    var peak = 0
    while (x <= target.max.x && y >= min(target.min.y, 0)) {
        x += dx
        y += dy
        dx = max(dx-1, 0)
        dy -= 1
        if (y > peak) peak = y
        if (target.min.x <= x && x <= target.max.x && target.min.y <= y && y <= target.max.y)
            return Pair(true, peak)
    }
    return Pair(false, peak)
}
