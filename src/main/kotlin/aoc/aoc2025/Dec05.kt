package aoc.aoc2025


import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 3L); println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 14L); println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val (ranges, numbers) = parseInput(input)
    var count = 0L

    for (n in numbers) {
        for (range in ranges) {
            if (n >= range.first && n <= range.second) {
                count++
                break
            }
        }
    }
    return count
}

private fun execute2(input: List<String>): Long {

    var (ranges, _) = parseInput(input)

    // Create merged ranges and put them in next set, as long as merges happen
    do {
        var changed = false
        val nextRanges = HashSet<Pair<Long, Long>>()
        for (base in ranges) {
            var min = base.first
            var max = base.second
            for (comp in ranges) {
                if (comp.first <= min && comp.second >= min ||
                    comp.first <= max && comp.second >= max ||
                    comp.first <= min && comp.second >= max
                ) {
                    min = Math.min(min, comp.first)
                    max = Math.max(max, comp.second)
                }
            }
            nextRanges.add(min to max) // set handles duplicates
            changed = (min != base.first || max != base.second)
        }
        ranges = nextRanges
    } while (changed)

    var count = 0L
    for (range in ranges) {
        count += range.second - range.first + 1
    }
    return count
}

private fun parseInput(input: List<String>): Pair<HashSet<Pair<Long, Long>>, List<Long>> {

    val ranges = HashSet<Pair<Long, Long>>()
    val numbers = ArrayList<Long>()

    for (line in input) {
        if (line == "") continue
        if (line.contains("-")) {
            val range = line.split("-")
            ranges.add(range[0].toLong() to range[1].toLong())
            continue
        }

        val n = line.toLong()
        numbers.add(n)
    }
    return ranges to numbers
}