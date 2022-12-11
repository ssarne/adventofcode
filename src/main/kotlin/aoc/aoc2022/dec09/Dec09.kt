package aoc.aoc2022.dec09

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute(readTestLines(), 2), 13)
    execute(readLines(), 2).let { println(it); check(it, readAnswer(1).toInt()) }
    execute(readLines(), 10).let { println(it); check(it, readAnswer(2).toInt()) }
}

private fun execute(input: List<String>, length: Int): Int {

    var hist = HashSet<Point>()
    var knots = ArrayList<Point>()
    for (i in 0 until length) knots.add(Point(0,0))

    for (line in input) {
        val dist = line.split(" ")[1].toInt()
        val dir =  line.split(" ")[0]
        for (i in 0 until dist) {
            when (dir) {
                "L" -> knots[0] = Point(knots[0].x - 1, knots[0].y)
                "R" -> knots[0] = Point(knots[0].x + 1, knots[0].y)
                "U" -> knots[0] = Point(knots[0].x, knots[0].y + 1)
                "D" -> knots[0] = Point(knots[0].x, knots[0].y - 1)
            }
            for (i in 1 until knots.size) {
                val delta = Point((knots[i-1].x - knots[i].x), (knots[i-1].y - knots[i].y))
                if (Math.abs(delta.x) > 1 && Math.abs(delta.y) > 1) knots[i] = Point(knots[i].x + delta.x / 2, knots[i].y + delta.y / 2)
                else if (Math.abs(delta.x) > 1) knots[i] = Point(knots[i].x + delta.x / 2, knots[i-1].y)
                else if (Math.abs(delta.y) > 1) knots[i] = Point(knots[i-1].x, knots[i].y + delta.y / 2)
                else knots[i] = Point(knots[i].x + delta.x / 2, knots[i].y + delta.y / 2)
            }
            hist.add(knots.last())
        }
    }

    return hist.size
}