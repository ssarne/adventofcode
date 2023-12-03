package aoc.aoc2021

import aoc.ktutils.check
import aoc.ktutils.readAnswerAsInt
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 198)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 230)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    val size = input.first().length
    val ones = occurrences(size, input, '1')
    val zeros = occurrences(size, input, '0')

    var gamma = ""
    var epsilon = ""
    for (i in 0 until size) {
        when {
            ones[i] > zeros[i] -> {
                gamma += '1'
                epsilon += '0'
            }
            ones[i] < zeros[i] -> {
                gamma += '0'
                epsilon += '1'
            }
            else -> throw RuntimeException("WAT")
        }
    }

    return Integer.parseInt(gamma, 2) * Integer.parseInt(epsilon, 2)
}

private fun execute2(input: List<String>): Int {

    val oxygen = getRating(input.toMutableList(), Substance.OXYGEN)
    val carbons = getRating(input.toMutableList(), Substance.CO2)
    return Integer.parseInt(oxygen, 2) * Integer.parseInt(carbons, 2)
}

private enum class Substance {OXYGEN, CO2}
private fun getRating(input: List<String>, type: Substance): String {

    val funnel = input.toMutableList()
    val size = input.first().length

    for (i in 0 until size) {
        if (funnel.size == 1) return funnel.first()

        val ones = occurrences(size, funnel, '1')
        val zeros = occurrences(size, funnel, '0')

        val c = when (type) {
            Substance.OXYGEN -> if (ones[i] >= zeros[i]) '1' else '0'
            Substance.CO2 -> if (ones[i] < zeros[i]) '1' else '0'
        }

        funnel.retainAll { it[i] == c }
    }

    if (funnel.size == 1) return funnel.first()
    throw RuntimeException("WAT")
}

private fun occurrences(length: Int, input: List<String>, c: Char): IntArray {
    val counts = IntArray(length)
    for (i in 0 until length) {
        for (line in input) {
            if (line[i] == c) {
                counts[i]++
            }
        }
    }
    return counts
}