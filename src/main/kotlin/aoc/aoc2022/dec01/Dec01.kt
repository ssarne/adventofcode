package aoc.aoc2022.dec01

import aoc.ktutils.asChunks
import aoc.ktutils.readAnswer
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    aoc.ktutils.check(execute1(readTestLines()), 24000)
    execute1(readLines()).let { println(it); aoc.ktutils.check(it, readAnswer(1).toInt()) }

    aoc.ktutils.check(execute2(readTestLines()), 45000)
    execute2(readLines()).let { println(it); aoc.ktutils.check(it, readAnswer(2).toInt()) }
}

private fun execute1(lines: List<String>): Int {
    val elfes = readElfes(lines)
    return elfes.max()!!
}

private fun execute2(lines: List<String>): Int {
    val elfes: List<Int> = readElfes(lines)
    val sorted = elfes.sorted().asReversed()
    return sorted[0] + sorted[1] + sorted[2]
}

private fun readElfes(lines: List<String>): List<Int> {
    var chunks = asChunks(lines)
    return chunks.stream()
        .map { chunk -> chunk.map { it.toInt() }.reduce { a, n -> a + n } }
        .mapToInt {it.toInt()}.toArray().toList()
}
