package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 57); println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check (decompress2("(3x3)XYZ"), "XYZXYZXYZ".length.toLong())
    check (decompress2("X(8x2)(3x3)ABCY"), "XABCABCABCABCABCABCY".length.toLong())
    check (decompress2("(27x12)(20x12)(13x14)(7x10)(1x12)A"), 241920L)
    check (decompress2("(25x3)(3x3)ABC(2x3)XY(5x2)PQRSTX(18x9)(3x2)TWO(5x7)SEVEN"), 445L)

    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {
    return input.sumOf { decompress1(it).length }
}

private fun decompress1(line: String): String {
    var result = ""
    var pos = 0
    while (pos < line.length) {
        if (line[pos] == '(') {
            val ep = line.indexOf(')', pos)
            val (len, reps) = asIntPair(line.substring(pos+1, ep), "x")
            val start = ep + 1
            val end = start + len
            repeat(reps) {
                result += line.substring(start until end)
            }
            pos = end
        } else {
            result += line[pos++]
        }
    }
    return result
}

private fun execute2(input: List<String>): Long {
    return input.sumOf { decompress2(it) }
}

private fun decompress2(line: String): Long {
    var result = 0L
    var pos = 0
    while (pos < line.length) {
        if (line[pos] == '(') {
            val ep = line.indexOf(')', pos)
            val (len, reps) = asIntPair(line.substring(pos+1, ep), "x")
            val addition = decompress2(line.substring(ep + 1, ep + 1 + len))
            result += reps * addition
            pos = ep + 1 + len
        } else {
            result += 1
            pos++
        }
    }
    return result
}