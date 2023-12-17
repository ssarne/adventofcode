package aoc.aoc2023

import aoc.ktutils.*
import java.util.LinkedList
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 102)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 94)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

data class DPos(val p: Point, val dir: Char, val dist: Int) {

    public val directions = arrayOf('^', '>', 'v', '<')
    fun right() = DPos(p, directions[Math.floorMod((directions.indexOf(dir) + 1), 4)], 0)
    fun left() = DPos(p, directions[Math.floorMod((directions.indexOf(dir) - 1), 4)], 0)

    fun move() = move(1)
    fun move(distance: Int): DPos {
        return when (dir) {
            '^' -> DPos(Point(p.x, p.y - distance), dir, dist + 1)
            '>' -> DPos(Point(p.x + distance, p.y), dir, dist + 1)
            'v' -> DPos(Point(p.x, p.y + distance), dir, dist + 1)
            '<' -> DPos(Point(p.x - distance, p.y), dir, dist + 1)
            else -> throw RuntimeException("CMH $dir")
        }
    }
}

private fun execute1(input: List<String>): Int {
    return execute(input, 0, 3)
}

private fun execute2(input: List<String>): Int {
    return execute(input, 4, 10)
}

private fun execute(input: List<String>, min: Int, max: Int): Int {
    val map = parseCharacterGridToMap(input)
    val size = mapSize(map)
    val start = DPos(size.first, '>', 0)
    val end = size.second
    val visited = HashMap<DPos, BooleanArray>() // IntArray>()
    val runners = HashMap<DPos, Int>()
    visited.put(start, BooleanArray(max + 1){true})
    runners.put(start, 0)

    while (true) {
        var bestPos: DPos? = null
        var bestHeat = 0

        val iterator = runners.keys.iterator()
        while (iterator.hasNext()) {
            val pos = iterator.next()
            val nexts = if (pos.dist < min) arrayOf(pos.move())
            else if (pos.dist < max) arrayOf(pos.left().move(), pos.right().move(), pos.move())
            else arrayOf(pos.left().move(), pos.right().move())

            var candidate = false
            for (next in nexts) {
                // if (next.dist > 3) continue
                if (!map.containsKey(next.p)) continue
                if (visited.containsKey(next) && visited[next]!![next.dist]) continue
                candidate = true
                val nextHeat = runners[pos]!! + asInt(map[next.p]!!)
                if (bestPos == null || bestHeat > nextHeat) {
                    bestPos = next
                    bestHeat = nextHeat
                }
            }
            if (!candidate) iterator.remove()
        }
        if (bestPos == null) throw RuntimeException("CMH")

        println("best=$bestPos heat=$bestHeat")
        val visiteds = visited[bestPos] ?: BooleanArray(max + 1){false}
        for (i in bestPos.dist .. max) visiteds[i] = true
        visited[bestPos] = visiteds
        runners[bestPos] = bestHeat
        if (bestPos.p == end) return bestHeat
    }
}