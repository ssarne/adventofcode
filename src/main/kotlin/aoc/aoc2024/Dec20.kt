package aoc.aoc2024


import aoc.ktutils.*
import kotlin.math.*

fun main() {
   //execute1(readTestLines(1), 20, 2).let { println("Test: ") ; check(it, 5) }
   //execute1(readLines(), 100, 2).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    execute(readTestLines(1), 76, 20).let { println("Test: $it") ; check(it, 3L) }
    execute(readTestLines(1), 70, 41).let { println("Test: $it") ; check(it, 41L) }
    execute(readLines(), 100, 20).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>, threshold: Int, duration: Int): Long {

    val map = parseCharacterGridToMap(input)
    val visited = solve(map)

    val cheats = cheats(map, visited, duration)

    var count = 0L
    for (c in cheats) {
        if (c.value.first >= threshold) {
            // println("$c")
            count++
        }
    }
    return count
}

fun cheats(map: HashMap<Point, Char>, visited: MutableMap<Point, Int>, duration: Int): MutableMap<Pair<Point, Point>, Pair<Int, Int>> {
    val cheats = mutableMapOf<Pair<Point, Point>, Pair<Int, Int>>()
    for (pos in visited.keys) {
        cheat(map, visited, cheats, duration, pos, pos, 0)
    }
    return cheats
}

fun cheat(map: HashMap<Point, Char>,
          visited: MutableMap<Point, Int>,
          cheats: MutableMap<Pair<Point, Point>, Pair<Int, Int>>, // from -> to, save -> steps
          duration: Int, start: Point, pos: Point, steps: Int) {

    if (steps > duration) return
    if (cheats.containsKey(start to pos) && cheats[start to pos]!!.second <= steps) return

    val save = if (map[pos]!! in ".SEO") visited[pos]!! - visited[start]!! - steps else -1
    cheats.put(start to pos, save to steps)

    for (next in pos.adjacent()) {
        if (!map.containsKey(next)) continue
        // if (map[next] == '#') // TODO can it re-cheat? Yes
        cheat(map, visited, cheats, duration, start, next, steps + 1)
    }
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