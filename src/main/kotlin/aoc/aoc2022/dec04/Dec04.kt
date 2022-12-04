package aoc.aoc2022.dec04

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 2)
    execute1(readLines()).let { println(it); check(it, 453) }

    check(execute2(readTestLines()), 4)
    execute2(readLines()).let { println(it); check(it, 919) }
}

private fun execute1(input: List<String>): Int {
    var count = 0
    for (line in input) {
        val (a1, a2, b1, b2) = parse(line)
        if (a1 in b1..b2 && a2 in b1..b2)
            count++
        else if (b1 in a1..a2 && b2 in a1..a2)
            count++
    }
    return count
}

private fun execute2(input: List<String>): Int {
    var count = 0
    for (line in input) {
        val (a1, a2, b1, b2) = parse(line)
        if (a1 in b1..b2)
            count++
        else if (a2 in b1..b2)
            count++
        else if (b1 in a1..a2)
            count++
        else if (b2 in a1..a2)
            count++
    }
    return count
}

private fun parse(line: String): IntArray {
    val pair = line.split(",")
    val elf1 = pair[0].split("-")
    val elf2 = pair[1].split("-")
    return intArrayOf(
        elf1[0].toInt(),
        elf1[1].toInt(),
        elf2[0].toInt(),
        elf2[1].toInt())
}
