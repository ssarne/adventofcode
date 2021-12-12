package aoc.aoc2021

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import kotlin.math.abs

fun main() {
    check(execute1(readTestLines()), 150)
    execute1(readLines()).let { println(it) ; check(it, 1507611) }

    check(execute2(readTestLines()), 900)
    execute2(readLines()).let { println(it) ; check(it, 1880593125) }
}

private fun execute1(input: List<String>): Int {
    var x = 0
    var z = 0
    for (line in input) {
        val cmd = line.split(" ")
        when (cmd[0]) {
            "forward" -> x += cmd[1].toInt()
            "down" -> z += cmd[1].toInt()
            "up" -> z -= cmd[1].toInt()
        }
    }

    return abs(x) * abs(z)
}

private fun execute2(input: List<String>): Int {
    var x = 0
    var z = 0
    var aim = 0
    for (line in input) {
        val cmd = line.split(" ")
        when (cmd[0]) {
            "forward" -> {
                x += cmd[1].toInt()
                z += cmd[1].toInt() * aim
            }
            "down" -> aim += cmd[1].toInt()
            "up" -> aim -= cmd[1].toInt()
        }
    }

    return abs(x) * abs(z)
}