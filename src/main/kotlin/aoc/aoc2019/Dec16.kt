package aoc.aoc2019

import aoc.ktutils.check
import aoc.ktutils.readAnswer
import aoc.ktutils.readLines
import kotlin.math.abs

fun main() {

    check(fft1("12345678", 4), "01029498")
    check(fft1("80871224585914546619083218645595", 100), "24176176")
    check(fft1("19617804207202209144916044189917", 100), "73745418")
    check(fft1("69317163492948606335995924319873", 100), "52432133")

    fft1(readLines()[0], 100).let { println(it) ; check(it, readAnswer(1)) }

    check(fft2("03036732577212944063491565474664", 100, 10000), "84462026")
    check(fft2("02935109699940807407585447034323", 100, 10000), "78725270")
    check(fft2("03081770884921959731165446850517", 100, 10000), "53553731")

    fft2(readLines()[0], 100, 10000).let { println(it) ; check(it, readAnswer(2)) }
}

private fun fft1(line: String, times: Int): String {

    val input = line.map { Character.digit(it, 10) }
    val length = input.size
    val base = intArrayOf(0, 1, 0, -1)
    var value = input.toIntArray()

    for (n in 0 until times) {
        val prev = value
        value = IntArray(length)
        for (p in value.indices) {
            var sum = 0
            for (i in prev.indices) {
                val m = base[((i + 1) / (p + 1)) % 4]
                sum += m * prev[i]
            }
            value[p] = abs(sum) % 10
        }
    }

    return value.take(8).joinToString("")
}

// Since the offset is beyond 3/4 of the input value and patter is {_, _, 0, 1}
// and is padded with zero for the anything prior to the offset
// it is possible to sum them up in starting from the last position (which is the same btw)
private fun fft2(line: String, times: Int, multiplier: Int): String {

    val input = line.map { Character.digit(it, 10) }
    val length = input.size
    val offset = input.take(7).fold(0) { acc, digit -> 10 * acc + digit }
    val fullLength = multiplier * length

    // Only work with offset to fullLength  (fullLength < 2 * offset)
    val value = (offset until fullLength).map { input[it % length] }.toIntArray()

    for (n in 0 until times) {
        var sum = 0
        for (i in value.indices.reversed()) {
            sum = abs(sum + value[i]) % 10
            value[i] = sum
        }
    }

    return value.take(8).joinToString("")
}