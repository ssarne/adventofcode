package aoc.aoc2023


import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 13)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 30)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        if (line.isEmpty()) continue
        val (id, matches) = parse(line)

        if (matches > 0) {
            var point = 1
            for (i in 1 until matches)
                point *= 2
            sum += point
        }
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    val extras = IntArray(input.size + 1)
    for (line in input) {
        if (line.isEmpty()) continue
        val (id, matches) = parse(line)
        val dups = 1 + extras[id]
        for (i in 1 .. matches) {
            if (id + i < extras.size)
                extras[id+i] += dups
        }
    }

    return extras.sum() + input.size
}

private fun parse(line: String): Pair<Int, Int> {
    //Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
    val id = line.split(": ").first().substring("Card".length).trim().toInt()
    val nums = line.split(": ")[1].trim()

    val winning = asIntArray(nums.split(" | ")[0])
    val mine = asIntArray(nums.split(" | ")[1])

    var matches = 0
    for (i in mine) {
        if (winning.contains(i)) matches++
    }
    return Pair(id, matches)
}
