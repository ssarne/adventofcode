package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readInts
import aoc.ktutils.readText

fun main() {
    check(hash(5, intArrayOf(3, 4, 1, 5)), 12)
    println(hash(256, readInts())) // 54675
    check(hash2(256, ""), "a2582a3a0e66e6e86e3812dcb672a272")
    check(hash2(256, "AoC 2017"), "33efeb34ea91902bb2f59c9920caa6cd")
    check(hash2(256, "1,2,3"), "3efbe78a8d82f29979031a4aa0b16a9d")
    check(hash2(256, "1,2,4"), "63960835bcdc130f0b66d7ff4f6a5a8e")
    println(hash2(256, readText())) // a7af2706aa9a09cf5d848c1e6605dd2a
}

private fun hash(size: Int, input: IntArray): Int {
    var pos = 0
    var skip = 0
    var data = IntArray(size)
    for (i in data.indices) data[i] = i
    for (x in input) {
        reverse(pos, x, data)
        pos = (pos + x + skip) % size
        skip++
    }
    return data[0] * data[1]
}

private fun hash2(size: Int, input: String): String {

    var lengths = input
        .toCharArray()
        .map { c -> c.toInt() }
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
    for (i in 0..15) {
        var c = data[16 * i]
        for (j in 1..15) {
            c = c.xor(data[16 * i + j])
        }
        if (c <= 16) dense += "0"
        dense += c.toString(16)
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