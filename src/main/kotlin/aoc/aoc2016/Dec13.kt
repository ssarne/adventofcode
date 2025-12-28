package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute(testLines(), Point(7, 4), 1000).let { check(it, 11); println("Test: $it") }
    execute(readLines(), Point(31, 39), 1000).let { println(it) ; check(it, answerI(1)) }
    execute(readLines(), null, 50).let { println(it) ; check(it, answerI(2)) }
}

private fun execute(input: List<String>, target: Point?, steps: Int): Int {
    val number = input.first().toInt()
    val queue = mutableListOf(Point(1, 1))
    val visited = mutableMapOf(Point(1, 1) to 0)
    while (queue.isNotEmpty()) {
        val pos = queue.removeAt(0)
        if (pos == target) return visited[pos]!!
        for (next in pos.adjacent()) {
            if (next.x < 0 || next.y < 0) continue
            if (visited.contains(next)) continue
            if (visited[pos]!! > steps - 1) continue
            if (isFloor(next, number)) {
                visited.put(next, visited[pos]!! + 1)
                queue.add(next)
            }
        }
    }
    return visited.size
}

private fun isFloor(p: Point, n: Int): Boolean {
    val number = p.x * p.x + 3 * p.x + 2 * p.x * p.y + p.y + p.y * p.y + n
    val binary = number.toString(2)
    val bits = binary.count { it == '1' }
    return bits % 2 == 0
}