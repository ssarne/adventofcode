package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.max
import kotlin.math.min

fun main() {
    check(execute1(readTestLines(1)), -1)
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    testExecute3()
    check(execute2(readTestLines(1)), 952408144115L)
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

fun testExecute3() {
    check(execute3(Polygon.create(Point(0, 0), Point(4, 0), Point(4, 4),  // square, 25
        Point(0, 4), Point(0, 0))), 25)
    check(execute3(Polygon.create(Point(0, 4), Point(4, 4), Point(4, 0),  // cross, 105
        Point(8, 0), Point(8, 4), Point(12, 4), Point(12, 8),
        Point(8, 8), Point(8, 12), Point(4, 12),  Point(4, 8),
        Point(0, 8), Point(0, 4))), 105)
    check(execute3(Polygon.create(Point(0, 0), Point(8, 0), Point(8, 4),  // bow-shape,
        Point(6, 4), Point(6, 2), Point(2, 2), Point(2, 8),
        Point(0, 8), Point(0, 0))), 51)
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

    // printSparseMatrix(map, true, "After dig " + map.size)

    val size = mapSize(map)
    for (x in size.first.x..size.second.x) {
        for (y in size.first.y..size.second.y) {
            if (!map.containsKey(Point(x, y))) {
                // try fill
                val inner = floodFill(Point(x, y), gridClone(map), size)
                if (inner) floodFill(Point(x, y), map, size)
            }
        }
    }

    // printSparseMatrix(map, true, "After fill " + map.size)

    return map.size
}

fun floodFill(start: Point, map: HashMap<Point, Char>, size: Pair<Point, Point>): Boolean {
    val queue = LinkedList<Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val pos = queue.pop()
        if (pos.x < size.first.x || pos.x > size.second.x) return false
        if (pos.y < size.first.y || pos.y > size.second.y) return false
        if (map[pos] != null) continue

        map[pos] = '#'
        for (n in pos.adjacent()) queue.add(n)
    }
    return true
}

private fun execute2(input: List<String>): Long {
    val polygon = parse(Point(0, 0), input)
    return execute3(polygon)
}

private fun execute3(polygon: Polygon): Long {

    // Go through all squares
    // Create all points where a line starts/ends
    val points = ArrayList<Point>()
    for (p1 in polygon.points) {
        for (p2 in polygon.points) {
            val p = Point(p1.x, p2.y)
            if (points.contains(p)) continue
            else points.add(p)
            //  if (polygon.points.contains(p))

            //else if (polygon.inside(p))
            //    points.add(p)
        }
    }

    // Go by x, then y coordinates of points in order
    // Add up the sum of all that are inside the polygon
    points.sortWith(compareBy({ it.x }, { it.y }))
    var prev: Point? = null
    var sum = 0L
    for (i in points.indices) {
        val pi = points[i]
        if (prev == pi) continue
        prev = pi
        var next: Point? = null
        var below: Point? = null
        var right: Point? = null
        for (j in i + 1 until points.size) {
            val pj = points[j]
            if (pi == pj) continue // dup
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

        val q1 = polygon.inside(Point(pi.x + 1, pi.y + 1))    //   q3 | q4
        val q2 = polygon.inside(Point(pi.x - 1, pi.y + 1))    //   ---+---
        val q3 = polygon.inside(Point(pi.x - 1, pi.y - 1))    //   q2 | q1
        val q4 = polygon.inside(Point(pi.x + 1, pi.y - 1))

        var area = 0L
        if (q1) {
            area = 1L * (next!!.x - pi.x) * (next.y - pi.y)
        } else if (q2 && q4) {
            area = 1L * (right!!.x - pi.x) + 1L * (below!!.y - pi.y) - 1L
        } else if (q2) {
            area = 1L * (below!!.y - pi.y)
        } else if (q4) {
            area = 1L * (right!!.x - pi.x)
        } else if (q3) {
            area = 1L
        } else {
            // Ignore points not inside polygon
            // throw RuntimeException("CMH $pi $next $below $right")
        }

        sum += area
    }
    return sum
}

fun splitLines(polygon: Polygon): Polygon {

    val next = Polygon.create(polygon.points.first())
    for ((p1, p2) in polygon.points.zipWithNext()) {
        val ps = polygon.points.toMutableList()
        var prev = p1
        if (p1.x == p2.x) { // horizontal
            if (p1.y < p2.y) ps.sortBy { it.y } else ps.sortByDescending { it.y }
            for (p in ps) {
                if (min(p1.y, p2.y) < p.y && p.y < max(p1.y, p2.y)) {
                    if (p.y != prev.y) {
                        next.add(Point(p1.x, p.y))
                        prev = p
                    }
                }
            }
        }
        if (p1.y == p2.y) { // vertical
            if (p1.x < p2.x) ps.sortBy { it.x } else ps.sortByDescending { it.x }
            for (p in ps) {
                if (min(p1.x, p2.x) < p.x && p.x < max(p1.x, p2.x)) {
                    if (p.x != prev.x) {
                        next.add(Point(p.x, p1.y))
                        prev = p
                    }
                }
            }
        }
        next.add(p2)
    }
    return next
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