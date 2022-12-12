package aoc.aoc2022.dec12

import aoc.ktutils.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    check(execute1(readTestLines()), 31)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 29)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

data class Step(val pos: Point, val dist: Int, val height: Char)

private fun execute1(input: List<String>): Int {
    var (map, start, end) = parse(input)
    var dist = bfs(start, end, map)
    return dist[end]!!
}

private fun execute2(input: List<String>): Int {

    var (map, _, end) = parse(input)
    var min = Int.MAX_VALUE

    for (pp in map.keys) {
        if (map[pp] == 'a') {
            var dist = bfs(pp, end, map)
            if (dist.containsKey(end) && dist[end]!! < min) min = dist[end]!!
        }
    }

    return min
}

private fun parse(input: List<String>): Triple<HashMap<Point, Char>, Point, Point> {
    var map = HashMap<Point, Char>()
    var start = Point(-1, -1)
    var end = Point(-1, -1)
    var y = 0
    for (line in input) {
        var x = 0
        for (c in line) {
            var d = c
            if (c == 'S') {
                d = 'a'
                start = Point(x, y)
            }
            if (c == 'E') {
                d = 'z'
                end = Point(x, y)
            }
            map.put(Point(x, y), d)
            x++
        }
        y++
    }
    return Triple(map, start, end)
}

private fun enqueue(
    map: HashMap<Point, Char>,
    next: Point,
    step: Step,
    queue: ArrayList<Step>
) {
    if (map.containsKey(next))
        if (map[next]!! <= step.height + 1)
            queue.add(Step(next, step.dist + 1, map[next]!!))

}

private fun bfs(
    start: Point,
    end: Point,
    map: HashMap<Point, Char>
): HashMap<Point, Int> {
    var dist = HashMap<Point, Int>()
    var queue = ArrayList<Step>()
    queue.add(Step(start, 0, 'a'))
    while (!queue.isEmpty()) {
        val step = queue.removeAt(0)
        if (dist.containsKey(step.pos)) continue
        dist.put(step.pos, step.dist)

        if (step.pos == end) break

        enqueue(map, Point(step.pos.x - 1, step.pos.y), step, queue)
        enqueue(map, Point(step.pos.x + 1, step.pos.y), step, queue)
        enqueue(map, Point(step.pos.x, step.pos.y - 1), step, queue)
        enqueue(map, Point(step.pos.x, step.pos.y + 1), step, queue)
    }
    return dist
}