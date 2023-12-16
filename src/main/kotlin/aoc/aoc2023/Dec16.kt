package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 46)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 51)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val start = Pos(Point(0, 0), '>')
    return energize(start, map)
}

private fun execute2(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val size = mapSize(map)
    var max = 0
    for (x in 0 .. size.second.x) {
        val e1 = energize(Pos(Point(x, 0), 'v'), map)
        if (e1 > max) max = e1
        val e2 = energize(Pos(Point(x, size.second.y), '^'), map)
        if (e2 > max) max = e2
    }
    for (y in 0 .. size.second.y) {
        val e1 = energize(Pos(Point(0, y), '>'), map)
        if (e1 > max) max = e1
        val e2 = energize(Pos(Point(size.second.x, y), '<'), map)
        if (e2 > max) max = e2
    }
    return max
}

private fun energize(start: Pos, map: HashMap<Point, Char>): Int {
    val visited = HashSet<Pos>()
    val queue = LinkedList<Pos>()
    queue.add(start)
    while (queue.isNotEmpty()) {
        val pos = queue.pop()
        if (!map.containsKey(pos.p)) continue
        if (visited.contains(pos)) continue
        visited.add(pos)
        when (map[pos.p]) {
            '.' -> queue.add(pos.move(1))
            '/' -> when (pos.dir) {
                '>' -> queue.add(pos.left().move(1))
                '<' -> queue.add(pos.left().move(1))
                '^' -> queue.add(pos.right().move(1))
                'v' -> queue.add(pos.right().move(1))
            }
            '\\' -> when (pos.dir) {
                '>' -> queue.add(pos.right().move(1))
                '<' -> queue.add(pos.right().move(1))
                '^' -> queue.add(pos.left().move(1))
                'v' -> queue.add(pos.left().move(1))
            }
            '|' -> when (pos.dir) {
                '>' -> {
                    queue.add(pos.right().move(1))
                    queue.add(pos.left().move(1))
                }
                '<' -> {
                    queue.add(pos.right().move(1))
                    queue.add(pos.left().move(1))
                }
                '^' -> queue.add(pos.move(1))
                'v' -> queue.add(pos.move(1))
            }
            '-' -> when (pos.dir) {
                '>' -> queue.add(pos.move(1))
                '<' -> queue.add(pos.move(1))
                '^' -> {
                    queue.add(pos.right().move(1))
                    queue.add(pos.left().move(1))
                }
                'v' -> {
                    queue.add(pos.right().move(1))
                    queue.add(pos.left().move(1))
                }
            }
        }
    }
    val energized = HashSet<Point>()
    for (pos in visited.iterator()) energized.add(pos.p)
    return energized.size
}
