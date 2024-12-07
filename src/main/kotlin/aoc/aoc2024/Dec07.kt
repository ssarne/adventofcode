package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 3749)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 11387)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val (result, factors) = parse(line)
        sum += evaluate(result, factors, "+*", 0, 0)
    }
    return sum
}

private fun execute2(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val (result, factors) = parse(line)
        sum += evaluate(result, factors, "+*|", 0, 0)
    }
    return sum
}

private fun evaluate(result: Long, factors: LongArray, ops: String, current: Long, pos: Int): Long {

    if (pos == factors.size)
        return 0L

    for (op in ops) {
        val res = when (op) {
            '*' -> current * factors[pos]
            '+' -> current + factors[pos]
            '|' -> (current.toString() + factors[pos].toString()).toLong()
            else -> throw RuntimeException("Unknown op $op")
        }
        if (pos + 1 == factors.size && res == result)
            return result

        if (evaluate(result, factors, ops, res, pos + 1) == result)
            return result
    }
    
    return 0L
}

private fun parse(line: String): Pair<Long, LongArray> {
    val sides = line.split(":")
    val result = sides[0].toLong()
    val factors = sides[1].trim().split(" ").map { t -> t.toLong() }.toLongArray()
    return Pair(result, factors)
}