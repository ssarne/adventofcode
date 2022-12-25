package aoc.aoc2022.dec24

import aoc.ktutils.*
import java.util.LinkedList

fun main() {

    check(execute1(readTestLines(2)), 10)
    check(execute1(readTestLines()), 18)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 54)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parse(input)
    return bfs(map, map.start, map.end, 0)
}

private fun execute2(input: List<String>): Int {
    val map = parse(input)
    val there = bfs(map, map.start, map.end, 0)
    val back = bfs(map, map.end, map.start, there)
    val again = bfs(map, map.start, map.end, back)
    return again
}

data class Blizzard(val id: Int, var pos: Point, val d: Char, val dir: Point) {
    fun location(time: Int, dx: IntRange, dy: IntRange): Point {
        val width = dx.last - 1
        val deltax = (time * dir.x) % width
        val nx = 1 + Math.floorMod(pos.x - 1 + deltax, width)
        val height = dy.last - 1
        val deltay = (time * dir.y) % height
        val ny = 1 + Math.floorMod(pos.y - 1 + deltay, height)
        return Point(nx, ny)
    }
}

data class Map(
    val grid: HashMap<Point, Char>,
    val dx: IntRange, val dy: IntRange,
    val blizzards: HashSet<Blizzard>,
    val start: Point, val end: Point
)

data class Position(val p: Point, val time: Int)


private fun bfs(map: Map, start: Point, end: Point, time: Int): Int {

    val queue = LinkedList<Position>()
    val memory = HashSet<Position>()
    queue.push(Position(start, time))
    while (queue.isNotEmpty()) {
        val pos = queue.pop()
        if (pos.p == end) return pos.time
        if (memory.contains(pos)) continue
        memory.add(pos)
        val bs = map.blizzards
            .map { it.location(pos.time, map.dx, map.dy) }
            .count { it == pos.p }
        if (bs > 0) continue
        for (n in pos.p.adjacent())
            if (n.x in map.dx && n.y in map.dy && !map.grid.containsKey(n))
                queue.addLast(Position(n, pos.time + 1))
        queue.addLast(Position(pos.p, pos.time + 1))
    }
    return -1
}

fun parse(input: List<String>): Map {
    var grid = HashMap<Point, Char>()
    var blizzards = HashSet<Blizzard>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            when (input[y][x]) {
                '#' -> grid.put(Point(x, y), input[y][x])
                '>' -> blizzards.add(Blizzard(1 + blizzards.size, Point(x, y), input[y][x], Point(1, 0)))
                '<' -> blizzards.add(Blizzard(1 + blizzards.size, Point(x, y), input[y][x], Point(-1, 0)))
                '^' -> blizzards.add(Blizzard(1 + blizzards.size, Point(x, y), input[y][x], Point(0, -1)))
                'v' -> blizzards.add(Blizzard(1 + blizzards.size, Point(x, y), input[y][x], Point(0, 1)))
            }
        }
    }
    val dx = grid.keys.map { it.x }.min()..grid.keys.map { it.x }.max()
    val dy = grid.keys.map { it.y }.min()..grid.keys.map { it.y }.max()
    return Map(grid, dx, dy, blizzards, Point(1, 0), Point(dx.last - 1, dy.last))
}
