package aoc.aoc2017

import aoc.ktutils.*

fun main() {
    check(hash(5, intArrayOf(3, 4, 1, 5)), 12)
    hash(256, readInts()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(hash2(256, ""), "a2582a3a0e66e6e86e3812dcb672a272")
    check(hash2(256, "AoC 2017"), "33efeb34ea91902bb2f59c9920caa6cd")
    check(hash2(256, "1,2,3"), "3efbe78a8d82f29979031a4aa0b16a9d")
    check(hash2(256, "1,2,4"), "63960835bcdc130f0b66d7ff4f6a5a8e")

    hash2(256, readText()).let {
        println(it)
        checkNot(it, "899124dac201012ebc32e2f4d11eaec55")
        check(it, readAnswer(2))
    }
}

private fun hash(size: Int, input: IntArray): Int {
    var pos = 0
    var data = IntArray(size)
    for (i in data.indices) data[i] = i
    for ((skip, x) in input.withIndex()) {
        reverse(pos, x, data)
        pos = (pos + x + skip) % size
    }
    return data[0] * data[1]
}

private fun hash2(size: Int, input: String): String {

    var lengths = input
        .toCharArray()
        .map { c -> c.code }
        .toIntArray() +
            intArrayOf(17, 31, 73, 47, 23)

    var pos = 0
    var skip = 0
    var data = IntArray(size)
    for (i in data.indices) data[i] = i


    for (i in 1 .. 64) {
        for (x in lengths) {
            reverse(pos, x, data)
            pos = (pos + x + skip) % size
            skip++
        }
    }

    var dense = ""
    for (i in 0 until 16) {
        var c = data[16 * i]
        for (j in 1 until 16) {
            c = c.xor(data[16 * i + j])
        }
        dense += c.toString(16).padStart(2, '0')
    }
    return dense
}

private fun reverse(start: Int, length: Int, data: IntArray) {
    val end = start + length - 1
    val mid = (length + 1) / 2
    val size = data.size
    for (i in 0 until mid) {
        val s = (start + i) % size
        val e = (end - i) % size
        val tmp = data[s]
        data[s] = data[e]
        data[e] = tmp
    }
}