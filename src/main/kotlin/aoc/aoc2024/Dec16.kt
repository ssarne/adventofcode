package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 7036)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 45)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val map = parseCharacterGridToMap(input)
    val start = Pos(findFirst(map, 'S'), '>')
    return solveMaze(start, map, mutableMapOf())
}

private fun execute2(input: List<String>): Long {

    val map = parseCharacterGridToMap(input)
    val start = Pos(findFirst(map, 'S'), '>')

    val visited = mutableMapOf<Pos, Int>()
    solveMaze(start, map, visited)

    val trails = mutableSetOf<Point>()
    val end = visited.filter { map[it.key.p] == 'E' }.firstNotNullOf { it.key }
    buildTrails(end, trails, visited)

    return trails.size.toLong()
}

private fun nextPos(front: MutableMap<Pos, Int>): Pair<Pos, Int> {
    var next: Pair<Pos, Int>? = null
    for (e in front.entries)
        if (next == null || e.value < next.second)
            next = e.key to e.value
    front.remove(next!!.first)
    return next
}

// djikstra
private fun solveMaze(start: Pos, map: HashMap<Point, Char>, visited: MutableMap<Pos, Int>): Long {

    val front = mutableMapOf(start to 0)
    while (front.isNotEmpty()) {

        val pos = nextPos(front)
        // printSparseMatrix(map)

        if (visited.contains(pos.first)) continue
        visited[pos.first] = pos.second

        if (map[pos.first.p] == 'E') return pos.second.toLong()  // success

        if (map[pos.first.p] == '.') map[pos.first.p] = pos.first.dir // pretty print

        pos.first.move().let {
            if (!visited.contains(it) && map[it.p]!! in ".E")
                front[it] = pos.second + 1
        }

        for (it in arrayOf(pos.first.left(), pos.first.right())) {
            if (!visited.contains(it))
                front[it] = pos.second + 1000
        }
    }

    return -1L
}

fun buildTrails(pos: Pos, trails: MutableSet<Point>, visited: MutableMap<Pos, Int>) {

    trails.add(pos.p)

    if (visited.contains(pos.back()) && visited[pos.back()]!! + 1 == visited[pos]) {
        buildTrails(pos.back(), trails, visited)
    }

    for (turn in arrayOf(pos.left(), pos.right())) {
        if (visited.getOrDefault(turn, -10000) + 1000 == visited[pos])
            buildTrails(turn, trails, visited)
    }
}

