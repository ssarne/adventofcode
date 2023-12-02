package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 5)
    check(execute1(readTestLines(2)), 2)
    check(execute1(readTestLines(3)), 12)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(4)), 4)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val moves = input[0].split(", ")
    var pos = Pos(Point(0, 0), '^')
    for (m in moves) {
        pos = when (m[0]) {
            'L' -> pos.left().move(m.substring(1).toInt())
            'R' -> pos.right().move(m.substring(1).toInt())
            else -> throw RuntimeException("CMH $m")
        }

    }
    return pos.p.manhattan(Point(0, 0))
}

private fun execute2(input: List<String>): Int {
    val moves = input[0].split(", ")
    var pos = Pos(Point(0, 0), '^')
    val visited = HashSet<Point>()
    for (m in moves) {
        pos = when (m[0]) {
            'L' -> pos.left()
            'R' -> pos.right()
            else -> throw RuntimeException("CMH $m")
        }
        for (i in 0 until m.substring(1).toInt()) {
            visited.add(pos.p)
            pos = pos.move()
            if (visited.contains(pos.p)) {
                // printSparseSet(visited)
                return pos.p.manhattan(Point(0, 0))
            }
        }
    }
    return -1
}