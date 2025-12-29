package aoc.aoc2016

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, 5L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): Long {
    val (lengths, positions) = parse(input)
    return simulate(lengths, positions)
}

private fun execute2(input: List<String>): Long {
    val (lengths, positions) = parse(input)
    lengths.add(11)
    positions.add(0)
    return simulate(lengths, positions)
}

private fun simulate(lengths: MutableList<Int>, positions: MutableList<Int>): Long {
    var time = -1L
    do {
        time++
        var pass = true
        for (i in 1 until lengths.size) {
            if ((time + i + positions[i]) % lengths[i] != 0L) {
                pass = false
                break
            }
        }
    } while (pass == false)
    return time
}

private fun parse(input: List<String>): Pair<MutableList<Int>, MutableList<Int>> {
    val lengths = mutableListOf(1)
    val positions = mutableListOf(0)
    for (line in input) {
        // Disc #1 has 5 positions; at time=0, it is at position 4.
        val tokens = line.split(" ")
        lengths.add(tokens[3].toInt())
        positions.add(tokens[11].replace(".", "").toInt())
    }
    return Pair(lengths, positions)
}

