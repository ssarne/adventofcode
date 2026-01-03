package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, 3L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }

    execute2(testLines(), 9L).let { println("Test:   $it") ; check(it, 2) }
    execute2(readLines(), 4294967295L).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): Long {
    val ranges = parse(input)
    return nextFree(0L, ranges)
}

private fun execute2(input: List<String>, max: Long): Long {
    val ranges = parse(input)
    var sum = 0L
    var start = nextFree(0L, ranges)
    while (start <= max) {
        var end = lastFree(start, ranges)
        if (end > max) end = max
        sum += end - start + 1
        start = nextFree(end + 1, ranges)
    }
    return sum
}

fun nextFree(address: Long, ranges: ArrayList<Pair<Long, Long>>): Long {
    var current = address
    while (true) {
        var next = current
        for (range in ranges)
            if (next >= range.first && next <= range.second)
                next = range.second + 1
        if (next == current) return current
        current = next
    }
}

fun lastFree(address: Long, ranges: ArrayList<Pair<Long, Long>>): Long {
    var min = Long.MAX_VALUE
    for (range in ranges) {
        if (range.second < address) continue
        if (range.first <= address) throw RuntimeException("Address $address inside range $range")
        if (range.first <= min) min = range.first - 1
    }
    return min
}

private fun parse(input: List<String>): ArrayList<Pair<Long, Long>> {
    val ranges = ArrayList<Pair<Long, Long>>()
    for (line in input)
        line.split("-")
            .let { ranges.add(it[0].toLong() to it[1].toLong()) }
    return ranges
}
