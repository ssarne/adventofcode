package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 41)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 6)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    val map = parseCharacterGridToMap(input)
    val start = Pos(findFirst(map, '^'), '^')

    val (visited, _, _) = walk(start, map)

    return visited.size
}


private fun execute2(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val start = Pos(findFirst(map, '^'), '^')
    val (visited, _, _) = walk(start, map)
    var sum = 0
    for (p in visited) {
        val map2 = gridClone(map)
        map2[p] = '#'
        val (_, _, end) = walk(start, map2)
        if (map.contains(end.p))
            sum++
    }
    return sum
}

private fun walk(start: Pos, map: HashMap<Point, Char>): Triple<MutableSet<Point>, MutableSet<Pos>, Pos> {

    val visited = mutableSetOf<Point>()
    val track = mutableSetOf<Pos>()

    var pos = start
    visited.add(pos.p)
    track.add(pos)

    while (true) {
        val next = pos.move()

        if (!map.containsKey(next.p))
            return Triple(visited, track, next)

        pos = if (map.get(next.p) == '#') pos.right() else next

        if (track.contains(pos))
            return Triple(visited, track, pos)

        visited.add(pos.p)
        track.add(pos)
    }
}