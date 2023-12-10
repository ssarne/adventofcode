package aoc.aoc2023


import aoc.ktutils.*
import java.util.*

fun main() {
    check(execute1(readTestLines(1)), 4)
    check(execute1(readTestLines(2)), 8)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(3)), 4)
    check(execute2(readTestLines(4)), 4)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val path = findPath(map)
    return path.size / 2
}

private fun execute2(input: List<String>): Int {

    val map = parseCharacterGridToMap(input)
    val path = findPath(map).toMutableSet()
    // printSparseMatrix(map)

    val (map2, path2) = expand(map, path)
    patchPath(map2, path2)
    // printSparseMatrix(map2)

    val outside2 = fillUp(map2, path2)
    // printSparseMatrix(map2)

    val nest = findNest(map, path, outside2)
    // printSparseMatrix(map)

    return nest.size
}

private fun findPath(map: Map<Point, Char>): List<Point> {
    var start = map.keys.first()
    for (pos in map.keys)
        if (map[pos]!! == 'S')
            start = pos

    val path = mutableListOf(start)

    // Only two directions possible from start, pick any of these as next position
    var pos = Pos(start, '.')
    Point(start.x, start.y - 1).let { if (map[it] == '7' || map[it] == 'F' || map[it] == '|') pos = Pos(it, '^') }
    Point(start.x, start.y + 1).let { if (map[it] == 'J' || map[it] == 'L' || map[it] == '|') pos = Pos(it, 'v') }
    Point(start.x - 1, start.y).let { if (map[it] == 'L' || map[it] == 'F' || map[it] == '-') pos = Pos(it, '<') }
    Point(start.x + 1, start.y).let { if (map[it] == 'J' || map[it] == '7' || map[it] == '-') pos = Pos(it, '>') }

    while (map[pos.p] != 'S') {
        path.add(pos.p)
        when (map[pos.p]) {
            '|' -> when (pos.dir) {
                'v' -> pos = Pos(Point(pos.p.x, pos.p.y + 1), 'v')
                '^' -> pos = Pos(Point(pos.p.x, pos.p.y - 1), '^')
            }

            '-' -> when (pos.dir) {
                '>' -> pos = Pos(Point(pos.p.x + 1, pos.p.y), '>')
                '<' -> pos = Pos(Point(pos.p.x - 1, pos.p.y), '<')
            }

            'L' -> when (pos.dir) {
                'v' -> pos = Pos(Point(pos.p.x + 1, pos.p.y), '>')
                '<' -> pos = Pos(Point(pos.p.x, pos.p.y - 1), '^')
            }

            'J' -> when (pos.dir) {
                'v' -> pos = Pos(Point(pos.p.x - 1, pos.p.y), '<')
                '>' -> pos = Pos(Point(pos.p.x, pos.p.y - 1), '^')
            }

            '7' -> when (pos.dir) {
                '^' -> pos = Pos(Point(pos.p.x - 1, pos.p.y), '<')
                '>' -> pos = Pos(Point(pos.p.x, pos.p.y + 1), 'v')
            }

            'F' -> when (pos.dir) {
                '^' -> pos = Pos(Point(pos.p.x + 1, pos.p.y), '>')
                '<' -> pos = Pos(Point(pos.p.x, pos.p.y + 1), 'v')
            }
        }
    }
    return path
}

private fun expand(map: Map<Point, Char>, path: Set<Point>): Pair<MutableMap<Point, Char>, MutableSet<Point>> {
    val map2 = HashMap<Point, Char>()
    val path2 = HashSet<Point>()
    for (point in map.keys) {
        Point(point.x * 2, point.y * 2).let {
            if (path.contains(point)) {
                map2[it] = map[point]!!
                path2.add(it)
            } else {
                map2[it] = '.'
            }
        }
    }
    return map2 to path2
}

private fun patchPath(map2: MutableMap<Point, Char>, path2: MutableSet<Point>) {
    val size2 = mapSize(map2)
    for (y in size2.first.y..size2.second.y) {
        for (x in size2.first.x..size2.second.x) {
            if (map2.contains(Point(x, y))) continue
            val l = map2[Point(x - 1, y)]
            val r = map2[Point(x + 1, y)]
            val u = map2[Point(x, y - 1)]
            val d = map2[Point(x, y + 1)]
            if ((l == 'L' || l == 'F' || l == '-' || l == 'S') && (r == 'J' || r == '7' || r == '-' || r == 'S')) {
                map2[Point(x, y)] = '-'
                path2.add(Point(x, y))
                continue
            }
            if ((u == '7' || u == 'F' || u == '|' || u == 'S') && (d == 'J' || d == 'L' || d == '|' || d == 'S')) {
                map2[Point(x, y)] = '|'
                path2.add(Point(x, y))
                continue
            }
            map2[Point(x, y)] = '.'
        }
    }
}

private fun fillUp(map: MutableMap<Point, Char>, path: Set<Point>): MutableSet<Point> {

    val outside = HashSet<Point>()
    val size = mapSize(map)

    // go around the edge and fill up from all free spots
    for (y in size.first.y..size.second.y) {
        fillUp(Point(size.first.x, y), map, path, outside)
        fillUp(Point(size.second.x, y), map, path, outside)
    }
    for (x in size.first.x..size.second.x) {
        fillUp(Point(x, size.first.y), map, path, outside)
        fillUp(Point(x, size.second.y), map, path, outside)
    }
    return outside
}

fun fillUp(start: Point, map: MutableMap<Point, Char>, path: Set<Point>, outside: MutableSet<Point>) {

    val queue = LinkedList<Point>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val point = queue.pop()
        if (!map.contains(point)) continue
        if (path.contains(point)) continue
        if (outside.contains(point)) continue
        outside.add(point)
        map[point] = '0'
        for (next in point.adjacent())
            queue.add(next)
    }
}

private fun findNest(map: Map<Point, Char>, path: Set<Point>, outside2: Set<Point>): Set<Point> {
    val nest = HashSet<Point>()
    for (point in map.keys) {
        if (path.contains(point))
            continue
        if (outside2.contains(Point(2 * point.x, 2 * point.y)))
            continue
        nest.add(point)
    }
    return nest
}
