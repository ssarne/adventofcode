package aoc.aoc2025

import aoc.ktutils.*
import kotlin.math.abs

fun main() {
    execute1(readTestLines(1)).let { check(it, 50L) ; println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 24L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val grid = HashMap<Point, Char>()
    for (line in input) {
        val split = line.split(",")
        val point = Point(split[0].toInt(), split[1].toInt())
        grid.put(point, '#')
    }

    var max = 0L
    for (p1 in grid.keys) {
        for (p2 in grid.keys) {
            val size = (1L + abs(p2.x - p1.x)) * (1L + abs(p2.y - p1.y))
            if (size > max) max = size
        }
    }

    return max
}

private fun execute2(input: List<String>): Long {

    val points = ArrayList<Point>()
    for (line in input) {
        val split = line.split(",")
        val point = Point(split[0].toInt(), split[1].toInt())
        points.add(point)
    }

    val (compressed, mapping) = compressPoints(points)
    val grid = createGridWithPolygon(compressed)
    fillMap(grid)

    var max = 0L
    for (p1 in compressed) {
        for (p2 in compressed) {
            val p1u = mapping[p1]!! // original coordinates
            val p2u = mapping[p2]!!
            val size = (1L + abs(p2u.x - p1u.x)) * (1L + abs(p2u.y - p1u.y))
            if (size > max) {
                var filled = true
                for (x in Math.min(p1.x, p2.x) .. Math.max(p1.x, p2.x)) {
                    for (y in Math.min(p1.y, p2.y) .. Math.max(p1.y, p2.y)) {
                        if (!(grid[Point(x, y)] == '#' || grid[Point(x, y)] == 'X')) filled = false
                        if (!filled) break
                    }
                    if (!filled) break
                }
                if (filled) max = size
            }
        }
    }

    return max
}

private fun createGridWithPolygon(compressed: ArrayList<Point>): HashMap<Point, Char> {
    val grid = HashMap<Point, Char>()
    var prev = compressed.last()
    for (i in 0 until compressed.size) {
        val curr = compressed[i]
        grid.put(prev, '#')
        grid.put(curr, '#')
        if (prev.x == curr.x) {
            val dy = if (prev.y < curr.y) 1 else -1
            for (y in 1 until Math.abs(prev.y - curr.y)) {
                grid.put(Point(prev.x, prev.y + y * dy), 'X')
            }
        }
        if (prev.y == curr.y) {
            val dx = if (prev.x < curr.x) 1 else -1
            for (x in 1 until Math.abs(prev.x - curr.x)) {
                grid.put(Point(prev.x + x * dx, prev.y), 'X')
            }
        }
        prev = curr
    }
    return grid
}

// compress the points by "removing" the distance between the corners
private fun compressPoints(original: ArrayList<Point>): Pair<ArrayList<Point>, HashMap<Point, Point>> {

    val xhs = HashSet<Int>()
    for (p in original) xhs.add(p.x)
    val xls = xhs.toMutableList().sorted()
    val yhs = HashSet<Int>()
    for (p in original) yhs.add(p.y)
    val yls = yhs.toMutableList().sorted()

    val compressed = ArrayList<Point>()
    val mapping = HashMap<Point, Point>()
    for (p in original) {
        val c = Point(xls.indexOf(p.x), yls.indexOf(p.y))
        compressed.add(c)
        mapping.put(c, p)
    }
    return Pair(compressed, mapping)
}

private fun fillMap(grid: HashMap<Point, Char>) {

    // Fill from sides to mark the outside with '.'
    val size = mapSize(grid)
    for (x in size.first.x..size.second.x) {
        if (!grid.containsKey(Point(x, size.first.y)))
            floodFill(Point(x, size.first.y), grid, size, '.')
        if (!grid.containsKey(Point(x, size.second.y)))
            floodFill(Point(x, size.second.y), grid, size, '.')
    }
    for (y in size.first.y..size.second.y) {
        if (!grid.containsKey(Point(size.first.x, y)))
            floodFill(Point(size.first.x, y), grid, size, '.')
        if (!grid.containsKey(Point(size.second.x, y)))
            floodFill(Point(size.second.x, y), grid, size, '.')
    }

    // Fill up all empty positions on map (inside)
    for (x in size.first.x + 1 until size.second.x) {
        for (y in size.first.y + 1 until size.second.y) {
            if (!grid.containsKey(Point(x, y))) {
                floodFill(Point(x, y), grid, size, 'X')
            }
        }
    }
}

fun floodFill(start: Point, map: HashMap<Point, Char>, size: Pair<Point, Point>, token: Char): Boolean {
    val queue = HashSet<Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val pos = queue.first()
        queue.remove(pos)
        if (pos.x < size.first.x || pos.x > size.second.x) continue
        if (pos.y < size.first.y || pos.y > size.second.y) continue
        if (map[pos] != null) continue

        map[pos] = token
        for (n in pos.adjacent()) queue.add(n)
    }
    return true
}