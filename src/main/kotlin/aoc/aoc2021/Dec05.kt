package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.abs
import kotlin.math.max

fun main() {
    check(execute(readTestLines()), 5)
    execute(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute(readTestLines(), true), 12)
    execute(readLines(), true).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute(input: List<String>, diagonals: Boolean = false ): Int {
    val points = HashMap<Point, Int>()
    for (line in input) { // 0,9 -> 5,9
        val p0 = Point.create(line.split(" -> ")[0])
        val p1 = Point.create(line.split(" -> ")[1])
        val pd = p1 - p0
        val dx = if (pd.x > 0) 1 else if (pd.x < 0) - 1 else 0  // x-direction
        val dy = if (pd.y > 0) 1 else if (pd.y < 0) - 1 else 0  // y-direction
        val distance = max(abs(pd.x), abs(pd.y))

        if (!diagonals && (pd.x != 0) && (pd.y != 0)) continue // only consider horizontal and vertical lines

        for (i in 0..distance) {
            val p = Point(p0.x + i * dx, p0.y + i * dy)
            val c = if (points.containsKey(p)) points[p]!! + 1 else 1
            points[p] = c
            // println("$p $c")
        }
    }

    return points.values.count { it > 1 }
}
