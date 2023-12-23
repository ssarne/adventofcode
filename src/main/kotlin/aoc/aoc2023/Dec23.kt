package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 94)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 154)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val start = Pos(Point(1, 0), 'v')
    map[start.p] = 'O'
    return findLongestPath(start.move(), 1, map)
}

private fun execute2(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val start = Pos(Point(1, 0), 'v')
    map[start.p] = 'O'
    val size = mapSize(map)
    for (x in 1 until size.second.x)
        for (y in 1 until size.second.y)
            if (">v<^".contains(map[Point(x, y)]!!)) map[Point(x, y)] = '.'
    return findLongestPath(start.move(), 1, map)
}

fun findLongestPath(pos: Pos, steps: Int, map: HashMap<Point, Char>): Int {

    var best = 0
    for (next in setOf(pos.left().move(), pos.move(), pos.right().move())) {
        val c = map[next.p]
        map[pos.p] = 'O'
        // printSparseMatrix(map, true, "steps=$steps c=$c next=$next")
        if (c == null) best = steps
        if (c == '.' || c == next.dir) {
            val candidate = findLongestPath(next, steps + 1, map)
            if (candidate > best) best = candidate
            map[next.p] = c
        }
    }
    return best
}