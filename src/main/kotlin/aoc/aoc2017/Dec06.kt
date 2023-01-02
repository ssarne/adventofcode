package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readAnswerAsInt
import aoc.ktutils.readLines
import java.util.*

private data class Result(var allocs: Int, var loop: Int)

fun main() {
    check(countReallocations(intArrayOf(0, 2, 7, 0)).allocs, 5)
    countReallocations(input(readLines())).allocs.let { println(it); check(it, readAnswerAsInt(1)) }
    check(countReallocations(intArrayOf(0, 2, 7, 0)).loop, 4)
    countReallocations(input(readLines())).loop.let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun countReallocations(banks: IntArray): Result {
    val history = HashMap<String, Int>()
    var allocs = 0
    while (!history.contains(banks.contentToString())) {
        history[banks.contentToString()] = allocs
        val pos = getMax(banks)
        val blocks = banks[pos]
        banks[pos] = 0
        for (i in 1..blocks) {
            banks[(pos+i) % banks.size]++
        }
        allocs++
    }
    return Result(allocs, allocs - history.getOrDefault(banks.contentToString(), 0))
}

private fun getMax(banks: IntArray): Int {
    var max = 0
    for (i in banks.indices)
        if (banks[i] > banks[max])
            max = i
    return max
}

private fun input(input: List<String>): IntArray {
    return input
        .first()
        .split(" ", "\t")
        .map { t -> t.toInt() }
        .toIntArray()
}
