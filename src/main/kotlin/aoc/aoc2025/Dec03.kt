package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    execute(2, readTestLines(1)).let { check(it, 357L) ; println("Test: $it") }
    execute(2, readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute(12, readTestLines(1)).let { check(it, 3121910778619L); println("Test: $it") }
    execute(12, readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute(length: Int, input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val n = joltage(line, 0, length, HashMap())
        sum += n
    }
    return sum
}

private fun joltage(line: String, start: Int, length: Int, cache: HashMap<Int, Long>): Long {

    if (length == 0) return 0L
    if (start + length > line.length) return 0L

    val key = start * 100 + length
    if (cache.containsKey(key)) return cache[key]!!

    val n = line[start].digitToInt().toLong() * 10L.pow(length - 1L)

    val later = joltage(line, start + 1, length, cache)
    val here = n + joltage(line, start + 1, length - 1, cache)
    val best = if (here > later) here else later

    cache[key] = best
    return best
}
