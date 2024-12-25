package aoc.aoc2024


import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 3L) } // ; println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
}

private fun execute1(input: List<String>): Long {

    val size = Point(5, 7)
    val (keys, locks) = parse(input, size)

    var matches = 0L
    for (key in keys) {
        for (lock in locks) {
            var match = true
            for (i in key.indices) {
                if (key[i] + lock[i] > size.y - 2) {
                    match = false
                }
            }
            if (match)
                matches++
        }
    }
    return matches
}

private fun parse(input: List<String>, size: Point): Pair<List<IntArray>, List<IntArray>> {

    val keys = mutableListOf<IntArray>()
    val locks = mutableListOf<IntArray>()
    val chunks = asChunks(input)

    for (chunk in chunks) {
        val map = parseCharacterGridToMap(chunk)
        if (map.get(Point(0, 0)) == '#') {
            val lock = IntArray(size.x)
            for (x in 0 until size.x) {
                for (y in 1 until size.y) {
                    if (map.get(Point(x, y)) == '#') {
                        lock[x]++
                    }
                }
            }
            locks.add(lock)
        }
        if (map.get(Point(0, size.y - 1)) == '#') {
            val key = IntArray(size.x)
            for (x in 0 until size.x) {
                for (y in size.y - 2 downTo 0) {
                    if (map.get(Point(x, y)) == '#') {
                        key[x]++
                    }
                }
            }
            keys.add(key)
        }
    }
    return keys to locks
}

private fun execute2(input: List<String>): String {
    return "God Jul 2024"
}