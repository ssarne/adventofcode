package aoc.aoc2024

import aoc.ktutils.*
import java.util.LinkedList
import java.util.SortedMap
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 7036)
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 45)
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val map = parseCharacterGridToMap(input)
    val start = Pos(findPos(map, 'S'), '>')
    val visited = mutableMapOf<Point, Pair<Pos, Int>>()
    return solveMaze(start, map, visited)
}

private fun nextPos(queue: MutableMap<Pos, Int>): Pair<Pos, Int> {
    var next: Pair<Pos, Int>? = null
    for (e in queue.entries)
        if (next == null || e.value < next.second)
            next = e.key to e.value
    queue.remove(next!!.first)
    return next
}


private fun findPos(map: HashMap<Point, Char>, c: Char): Point {
    for (e in map) {
        if (e.value == c)
            return e.key
    }
    throw RuntimeException("CMH")
}

private fun execute2(input: List<String>): Long {
    val map = parseCharacterGridToMap(input)
    val start = Pos(findPos(map, 'S'), '>')
    val end = Pos(findPos(map, 'E'), '-')

    val visited = mutableMapOf<Point, Pair<Pos, Int>>()
    solveMaze(start, map, visited)

    // val queue2 = mutableMapOf(end to visited[end.p])
    printSparseMatrix(map)
    val trails = mutableSetOf<Point>()
    findPrevPosses(end.p, end.p, trails, visited)
    return trails.size.toLong() + 1
}

private fun solveMaze(
    start: Pos,
    map: HashMap<Point, Char>,
    visited: MutableMap<Point, Pair<Pos, Int>>
): Long {
    val queue = mutableMapOf(start to 0)
    while (queue.isNotEmpty()) {
        // printSparseMatrix(map)
        val pos = nextPos(queue)

        if (visited.contains(pos.first.p)) continue
        visited.put(pos.first.p, pos.first to pos.second)

        if (map[pos.first.p] == 'E') return pos.second.toLong()  // success

        visited.put(pos.first.p, pos.first to pos.second)
        map[pos.first.p] = pos.first.dir

        if (!visited.contains(pos.first.move().p) && map[pos.first.move().p]!! in ".E")
            queue.put(pos.first.move(), pos.second + 1)

        if (!visited.contains(pos.first.left().move().p) && map[pos.first.left().move().p]!! in ".E")
            queue.put(pos.first.left().move(), pos.second + 1000 + 1)

        if (!visited.contains(pos.first.right().move().p) && map[pos.first.right().move().p]!! in ".E")
            queue.put(pos.first.right().move(), pos.second + 1000 + 1)
    }

    return -1L
}

fun findPrevPosses(pos: Point, next: Point, trails: MutableSet<Point>, visited: MutableMap<Point, Pair<Pos, Int>>) {
    if (pos == Point(13, 13))
        println(pos)

    val curr = visited[pos]!!
    val dir = if (pos != next) Pos.direction(pos, next) else curr.first.dir // end->end
    val exitValue = if (curr.first.dir != dir) curr.second + 1000 else curr.second

    for (p in pos.adjacent()) {
        val prevs = mutableListOf<Point>()
        if (visited.contains(p)) {
            val prev = visited[p]!!
            if (prev.first.move().p == pos) {
                if (prev.first.move().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1 == exitValue)
                        prevs.add(prev.first.p)
                }
                if (prev.first.move().left().dir == dir || prev.first.move().right().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1000 + 1 == exitValue)
                        prevs.add(prev.first.p)
                }
            }


            if (prev.first.left().move().p == pos) {
                if (prev.first.left().move().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1 + 1000 == exitValue)
                        prevs.add(prev.first.p)
                }
                if (prev.first.left().move().left().dir == dir || prev.first.left().move().right().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1000 + 1 + 1000 == exitValue)
                        prevs.add(prev.first.p)
                }
            }
            if (prev.first.right().move().p == pos) {
                if (prev.first.right().move().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1 + 1000 == exitValue)
                        prevs.add(prev.first.p)
                }
                if (prev.first.right().move().left().dir == dir || prev.first.right().move().right().dir == dir) {
                    if (visited[prev.first.p]!!.second + 1000 + 1 + 1000 == exitValue)
                        prevs.add(prev.first.p)
                }
            }
        }
        trails.addAll(prevs)
        for (prev in prevs) {
            findPrevPosses(visited[prev]!!.first.p, pos, trails, visited)
        }
    }
}

