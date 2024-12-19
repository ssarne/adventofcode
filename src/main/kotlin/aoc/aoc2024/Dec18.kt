package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(readTestLines(1), 6, 12).let { check(it, 22) }
    execute1(readLines(), 70, 1024).let { println(it); check(it, readAnswerAsInt(1)) }

    execute2(readTestLines(1), 6, 12).let { check(it, "6,1") }
    execute2(readLines(), 70, 1024).let { println(it); check(it, readAnswer(2)) }
}

private fun execute1(input: List<String>, size: Int, count: Int): Int {

    val (_, map) = parse(input, count)
    return solve(size, map).size
}

private fun execute2(input: List<String>, size: Int, count: Int): String {

    val (bytes, map) = parse(input, count)

    var path = solve(size, gridClone(map))
    for (i in count until input.size) {
        map[bytes[i]] = '#'
        if (!path.contains(bytes[i])) continue
        path = solve(size, gridClone(map))
        if (path.isEmpty())
            return "" + bytes[i].x + "," + bytes[i].y
    }

    return ""
}

private fun parse(input: List<String>, count: Int): Pair<MutableList<Point>, HashMap<Point, Char>> {

    val bytes = mutableListOf<Point>()
    for (line in input) {
        bytes.add(Point.create(line))
    }

    val map = HashMap<Point, Char>()
    for (i in 0 until count) {
        map[bytes[i]] = '#'
    }
    return Pair(bytes, map)
}

private fun solve(size: Int, map: MutableMap<Point, Char>): List<Point> {

    val start = Point(0, 0)
    val end = Point(size, size)
    val visited = mutableMapOf<Point, Point>()
    val queue = mutableListOf(start to start)

    while (queue.isNotEmpty()) {

        val pos = queue.removeAt(0)

        if (visited.contains(pos.first)) continue
        visited[pos.first] = pos.second // store path

        if (pos.first == end)
            break // success

        if (map[pos.first] != '#')
            map[pos.first] = 'O' // mark path

        for (next in pos.first.adjacent()) {
            if (next.x < 0 || next.x > size) continue
            if (next.y < 0 || next.y > size) continue
            if (map.contains(next)) continue
            queue.add(next to pos.first)
        }
    }

    val path = mutableListOf<Point>()
    if (visited[end] == null)
        return path

    var pos = end
    do {
        pos = visited[pos]!!
        path.add(pos)
    } while(pos != start)

    return path
}