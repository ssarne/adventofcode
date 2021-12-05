package aoc.aoc2021

import aoc.ktutils.Point
import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import kotlin.math.abs
import kotlin.math.max

fun main() {
    check(execute(readTestLines()), 5)
    println(execute(readLines())) // 5084

    check(execute(readTestLines(), true), 12)
    println(execute(readLines(), true)) // 17882
}

private fun execute(input: List<String>, diagonals: Boolean = false ): Int {
    var points = HashMap<Point, Int>()
    for (line in input) { // 0,9 -> 5,9
        var p0 = Point.create(line.split(" -> ")[0])
        var p1 = Point.create(line.split(" -> ")[1])
        var pd = p1 - p0
        var dx = if (pd.x > 0) 1 else if (pd.x < 0) - 1 else 0  // x-direction
        var dy = if (pd.y > 0) 1 else if (pd.y < 0) - 1 else 0  // y-direction
        var distance = max(abs(pd.x), abs(pd.y))

        if (!diagonals && (pd.x != 0) && (pd.y != 0)) continue // only consider horizontal and vertical lines

        for (i in 0..distance) {
            val p = Point(p0.x + i * dx, p0.y + i * dy)
            val c = if (points.containsKey(p)) points[p]!! + 1 else 1
            points.put(p, c)
            // println("$p $c")
        }
    }

    return points.values
        .filter{ it > 1 }
        .count()
}
