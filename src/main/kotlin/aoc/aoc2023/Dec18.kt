package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

fun main() {
    check(execute1(readTestLines(1)), 62)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 952408144115L)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = HashMap<Point, Char>()
    var pos = Pos(Point(0, 0), 'v')
    map[pos.p] = '#' // "#FFFFFF"
    for (line in input) {
        val parts = line.split(" ")
        val dir = parts[0].first()
        val len = parts[1].toInt()
        // val color = parts[2].replace("(", " ").replace("(", "")
        when (dir) {
            'U' -> pos = Pos(pos.p, '^')
            'R' -> pos = Pos(pos.p, '>')
            'D' -> pos = Pos(pos.p, 'v')
            'L' -> pos = Pos(pos.p, '<')
        }
        for (i in 0 until len) {
            pos = pos.move()
            map[pos.p] = '#' // color
        }
    }

    // Fill from sides to mark the outside with '.'
    val size = mapSize(map)
    for (x in size.first.x .. size.second.x) {
        if (!map.containsKey(Point(x, size.first.y)))
            floodFill(Point(x, size.first.y), map, size, '.')
        if (!map.containsKey(Point(x, size.second.y)))
            floodFill(Point(x, size.second.y), map, size, '.')
    }
    for (y in size.first.y .. size.second.y) {
        if (!map.containsKey(Point(size.first.x, y)))
            floodFill(Point(size.first.x, y), map, size, '.')
        if (!map.containsKey(Point(size.second.x, y)))
            floodFill(Point(size.second.x, y), map, size, '.')
    }

    // Fill up all empty positions on map (inside)
    for (x in size.first.x + 1 until size.second.x) {
        for (y in size.first.y + 1 until size.second.y) {
            if (!map.containsKey(Point(x, y))) {
                floodFill(Point(x, y), map, size, '#')
            }
        }
    }

    // printSparseMatrix(map, true, "After fill " + map.size)
    return map.values.count { (it == '#') }
}

fun floodFill(start: Point, map: HashMap<Point, Char>, size: Pair<Point, Point>, token: Char): Boolean {
    val queue = LinkedList<Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val pos = queue.pop()
        if (pos.x < size.first.x || pos.x > size.second.x) continue
        if (pos.y < size.first.y || pos.y > size.second.y) continue
        if (map[pos] != null) continue

        map[pos] = token
        for (n in pos.adjacent()) queue.add(n)
    }
    return true
}

private fun execute2(input: List<String>): Long {

    val polygon = parse(Point(0, 0), input)

    // Go through all lines
    // Create all points where a line starts/ends
    val points = ArrayList<Point>()
    for (p1 in polygon.points) {
        for (p2 in polygon.points) {
            val p = Point(p1.x, p2.y)
            if (points.contains(p)) continue
            else points.add(p)
        }
    }

    // Go by x, then y coordinates of points in order
    // Add up the sum of all that are inside the polygon
    points.sortWith(compareBy({ it.x }, { it.y }))
    var sum = 0L
    for (i in points.indices) {
        val pi = points[i]
        var next: Point? = null
        var below: Point? = null
        var right: Point? = null
        for (j in i + 1 until points.size) {
            val pj = points[j]
            if (pi.x == pj.x) {
                if (below == null) below = pj
                continue
            }
            if (pi.y > pj.y) continue
            if (pi.y == pj.y) {
                if (right == null) right = pj
                continue
            }
            next = pj
            break
        }

        // Detect if the area is inside the polygon
        // Or if the edges are inside (or corner for q3)
        val q1 = polygon.inside(Point(pi.x + 1, pi.y + 1))    //   q3 | q4
        val q2 = polygon.inside(Point(pi.x - 1, pi.y + 1))    //   ---+---
        val q3 = polygon.inside(Point(pi.x - 1, pi.y - 1))    //   q2 | q1
        val q4 = polygon.inside(Point(pi.x + 1, pi.y - 1))

        val area = if (q1) {
            1L * (next!!.x - pi.x) * (next.y - pi.y)
        } else if (q2 && q4) {
            1L * (right!!.x - pi.x) + 1L * (below!!.y - pi.y) - 1L
        } else if (q2) {
            1L * (below!!.y - pi.y)
        } else if (q4) {
            1L * (right!!.x - pi.x)
        } else if (q3) {
            1L
        } else {
            0L // Ignore points not inside polygon
        }

        sum += area
    }
    return sum
}

private fun parse(start: Point, input: List<String>): Polygon {
    val polygon = Polygon.create(start)
    var p = start

    for (line in input) {
        // (#70c710)
        val color = line.split(" ")[2]
        val len = color.substring(2, 7).toInt(radix = 16)
        val dir = color.substring(7).first()
        when (dir) {
            '0' -> p = Point(p.x + len, p.y) // >
            '1' -> p = Point(p.x, p.y + len) // v
            '2' -> p = Point(p.x - len, p.y) // <
            '3' -> p = Point(p.x, p.y - len) // ^
        }
        polygon.add(p)
    }
    return polygon
}