package aoc.aoc2021

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    check(execute1(readTestLines()), 7)
    execute1(readLines()).let { println(it) ; check(it, 1681) }

    check(execute2(readTestLines()), 5)
    execute2(readLines()).let { println(it) ; check(it, 1704) }
}

private fun execute1(lines: List<String>): Int {
    var prev = lines.first().toInt()
    var inc = 0
    for (line in lines) {
        if (line.toInt() > prev) {
            inc++
        }
        prev = line.toInt()
    }
    return inc
}

private fun execute2(lines: List<String>): Int {
    var prev = lines[1].toInt() + lines[1].toInt() + lines[2].toInt()
    var inc = 0
    for (i in 1..lines.size - 2) {
        val num = lines[i - 1].toInt() + lines[i].toInt() + lines[i + 1].toInt()
        if (num > prev) {
            inc++
        }
        prev = num
    }
    return inc
}
