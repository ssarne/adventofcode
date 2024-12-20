package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    execute(readTestLines(1), 20, 2).let { check(it, 5) }
    execute(readLines(), 100, 2).let { println(it); check(it, readAnswerAsLong(1)) }

    execute(readTestLines(1), 76, 20).let { check(it, 3L) }
    execute(readTestLines(1), 70, 41).let { check(it, 41L) }
    execute(readLines(), 100, 20).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>, threshold: Int, duration: Int): Long {

    val map = parseCharacterGridToMap(input)
    val visited = solve(map)
    val cheats = cheats(map, visited, duration)

    var count = 0L
    for (c in cheats) {
        if (c.value.first >= threshold) {
            count++
        }
    }
    return count
}

fun cheats(
    map: HashMap<Point, Char>,
    visited: MutableMap<Point, Int>,
    duration: Int
): MutableMap<Pair<Point, Point>, Pair<Int, Int>> {
    val cheats = mutableMapOf<Pair<Point, Point>, Pair<Int, Int>>() // from->to, save total
    for (pos in visited.keys) {
        val candidates = pos.withinManhattanDistance(duration)
        for (next in candidates) {
            val steps = next.manhattan(pos)
            if (steps > duration) continue
            if (!map.containsKey(next)) continue
            if (map[next] == '#') continue
            if (!visited.containsKey(next)) continue
            val save = visited[next]!! - visited[pos]!! - steps
            cheats[pos to next] = save to visited[pos]!! + steps
        }
    }
    return cheats
}

private fun solve(map: MutableMap<Point, Char>): MutableMap<Point, Int> {

    val start = findFirst(map, 'S')
    val end = findFirst(map, 'E')
    val visited = mutableMapOf<Point, Int>()
    val queue = mutableListOf(start to 0)

    while (queue.isNotEmpty()) {

        val pos = queue.removeAt(0)

        if (visited.contains(pos.first)) continue
        visited[pos.first] = pos.second // store path

        if (pos.first == end)
            break // success

        if (map[pos.first] != '#')
            map[pos.first] = 'O' // mark path

        for (next in pos.first.adjacent()) {
            if (!map.containsKey(next)) continue
            if (!(map[next]!! in ".E")) continue
            queue.add(next to pos.second + 1)
        }
    }

    return visited
}