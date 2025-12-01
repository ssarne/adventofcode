package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 3L); println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 6L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    var dial = 50
    var zeros = 0L

    for (line in input) {
        var rotation = line.substring(1).toInt()
        while (rotation > 100) rotation -= 100

        if (line.startsWith('L')) {
            dial -= rotation
            if (dial == 0) zeros++
            while (dial < 0) dial += 100
        }
        if (line.startsWith('R')) {
            dial += rotation
            while (dial > 100) dial -= 100
            if (dial == 100) zeros++
        }
    }
    return zeros
}

private fun execute2(input: List<String>): Long {

    var dial = 50
    var zeros = 0L

    for (line in input) {

        var rotation = line.substring(1).toInt()
        while (rotation > 100) {
            zeros++
            rotation -= 100
        }

        if (line.startsWith('L')) {
            val from = dial
            dial -= rotation
            if (dial == 0)
                zeros++
            if (dial < 0) {
                dial += 100
                if (from != 0)
                    zeros++
            }
        }
        if (line.startsWith('R')) {
            dial += rotation
            if (dial >= 100) {
                dial -= 100
                zeros++
            }
        }
    }
    return zeros
}