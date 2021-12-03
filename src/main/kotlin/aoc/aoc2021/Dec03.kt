package aoc.aoc2021

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 198)
    println(execute1(readLines())) // 1307354

    check(execute2(readTestLines()), 230)
    println(execute2(readLines())) // 482500
}

private fun execute1(input: List<String>): Int {

    var size = input.first().length
    var ones = occurencies(size, input, '1')
    var zeros = occurencies(size, input, '0')

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

    var oxygen = getRating(input.toMutableList(), Substance.OXYGEN)
    var carbons = getRating(input.toMutableList(), Substance.CO2)
    return Integer.parseInt(oxygen, 2) * Integer.parseInt(carbons, 2)
}

private enum class Substance {OXYGEN, CO2}
private fun getRating(input: List<String>, type: Substance): String {

    var funnel = input.toMutableList()
    var size = input.first().length

    for (i in 0 until size) {
        if (funnel.size == 1) return funnel.first()

        var ones = occurencies(size, funnel, '1')
        var zeros = occurencies(size, funnel, '0')

        var c = when (type) {
            Substance.OXYGEN -> if (ones[i] >= zeros[i]) '1' else '0'
            Substance.CO2 -> if (ones[i] < zeros[i]) '1' else '0'
        }

        funnel.retainAll { it[i] == c }
    }

    if (funnel.size == 1) return funnel.first()
    throw RuntimeException("WAT")
}

private fun occurencies(length: Int, input: List<String>, c: Char): IntArray {
    var counts = IntArray(length)
    for (i in 0 until length) {
        for (line in input) {
            if (line[i] == c) {
                counts[i]++
            }
        }
    }
    return counts
}