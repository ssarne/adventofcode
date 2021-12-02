package aoc.aoc2021

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    check(execute1(readTestLines()), 150)
    println(execute1(readLines())) // 1507611

    check(execute2(readTestLines()), 900)
    println(execute2(readLines())) // 1880593125
}

private fun execute1(input: List<String>): Int {
    var x = 0
    var z = 0
    for (line in input) {
        var cmd = line.split(" ")
        when (cmd[0]) {
            "forward" -> x += cmd[1].toInt()
            "down" -> z += cmd[1].toInt()
            "up" -> z -= cmd[1].toInt()
        }
    }

    return Math.abs(x) * Math.abs(z)
}

private fun execute2(input: List<String>): Int {
    var x = 0
    var z = 0
    var aim = 0
    for (line in input) {
        var cmd = line.split(" ")
        when (cmd[0]) {
            "forward" -> {
                x += cmd[1].toInt()
                z += cmd[1].toInt() * aim
            }
            "down" -> aim += cmd[1].toInt()
            "up" -> aim -= cmd[1].toInt()
        }
    }

    return Math.abs(x) * Math.abs(z)
}