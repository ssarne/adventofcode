package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1), 11, 7), 12)
    execute1(readLines(), 101, 103).let { println(it) ; check(it, readAnswerAsInt(1)) }
    execute2(readLines(), 101, 103).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private data class Robot(var id: Int, var pos: Point, var speed: Point)

private fun execute1(input: List<String>, width: Int, height: Int, time: Int = 100): Int {

    val robots = parseRobots(input)
    // printMap(robots, width, height)

    for (t in 1..time) {
        for (robot in robots) {
            robot.pos = Point(
                (robot.pos.x + robot.speed.x + width) % width,
                (robot.pos.y + robot.speed.y + height) % height
            )
        }
    }

    // printMap(robots, width, height)
    val q1 = Point(0, 0) to Point(width / 2 - 1, height / 2 - 1)
    val q2 = Point(width / 2 + 1, 0) to Point(width - 1, height / 2 - 1)
    val q3 = Point(0, height / 2 + 1) to Point(width / 2 - 1, height - 1)
    val q4 = Point(width / 2 + 1, height / 2 + 1) to Point(width - 1, height - 1)

    val count = intArrayOf(0, 0, 0, 0)
    for (robot in robots) {
        when {
            robot.pos inside q1 -> count[0]++
            robot.pos inside q2 -> count[1]++
            robot.pos inside q3 -> count[2]++
            robot.pos inside q4 -> count[3]++
        }
    }

    return count.fold(1) { a, b -> a * b }
}

private fun execute2(input: List<String>, width: Int, height: Int): Int {

    val robots = parseRobots(input)
    var time = 0

    while (true) {
        for (robot in robots) {
            robot.pos = Point(
                (robot.pos.x + robot.speed.x + width) % width,
                (robot.pos.y + robot.speed.y + height) % height
            )
        }

        val pos = mutableSetOf<Point>()
        for (robot in robots) pos.add(robot.pos)
        time++

        if (robots.size == pos.size) { // when all robots are on unique spots
            printMap(robots, width, height)
            return time
        }
    }
}

private fun parseRobots(input: List<String>): MutableSet<Robot> {
    val robots = mutableSetOf<Robot>()
    for ((i, line) in input.withIndex()) {
        val robot = Robot(i,
            Point.create(line.substring(2, line.indexOf(" "))),
            Point.create(line.substring(line.indexOf("v=") + 2, line.length)))
        robots.add(robot)
    }
    return robots
}

private fun printMap(robots: MutableSet<Robot>, width: Int, height: Int) {
    val map = mutableMapOf<Point, Char>()
    for (x in 0 until width) for (y in 0 until height) map.put(Point(x, y), '.')
    for (robot in robots) map.put(robot.pos, '#')
    printSparseMatrix(map)
}
