package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readAnswerAsInt
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import aoc.utils.Utils

fun main() {
    check(countJumps(input(readTestLines())), 5)
    countJumps(input(readLines())).let { println(it); check(it, readAnswerAsInt(1)) }
    check(countJumps2(input(readTestLines())), 10)
    countJumps2(input(readLines())).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun countJumps(instructions: IntArray): Int {
    var steps = 0
    var pos = 0

    while (pos >= 0 && pos < instructions.size) {
        var offset = instructions[pos]
        instructions[pos]++
        pos += offset
        steps++
    }

    return steps
}

private fun countJumps2(instructions: IntArray): Int {
    var steps = 0
    var pos = 0

    while (pos >= 0 && pos < instructions.size) {
        var offset = instructions[pos]
        if (offset < 3) instructions[pos]++ else instructions[pos]--
        pos += offset
        steps++
    }

    return steps
}

private fun input(input: List<String>): IntArray {
    var instr = input.map { line -> line.toInt() }
        .toIntArray()
    return instr
}
