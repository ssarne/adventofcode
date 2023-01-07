package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readAnswerAsInt
import java.lang.RuntimeException

fun main() {
    check(countBits("flqrgnkx"), 8108)
    countBits("hfdlxzhv").let { println(it) ; check(it, readAnswerAsInt(1)) }
    check(countRegions("flqrgnkx"), 1242)
    countRegions("hfdlxzhv").let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun countRegions(input: String): Int {
    var hashes = ArrayList<String>()
    for (i in 0 .. 127) {
        var hash = knotHash(256, "$input-$i" )
        hashes.add(hash)
    }
    var regions = Array(128) { IntArray(128) }
    var sum = 0
    for (i in 0 .. 127) {
        for (j in 0..127) {
            if (hashes[i][j] == '1' && regions[i][j] == 0) {
                fill(hashes, regions, i, j, ++sum)
            }
        }
    }
    return sum
}

private fun fill(hashes: ArrayList<String>, regions: Array<IntArray>, i: Int, j: Int, sum: Int) {
    regions[i][j] = sum
    if (i > 0 && hashes[i-1][j] == '1' && regions[i-1][j] == 0) fill(hashes, regions, i-1, j, sum)
    if (i < hashes.size - 1 && hashes[i+1][j] == '1' && regions[i+1][j] == 0) fill(hashes, regions, i+1, j, sum)
    if (j > 0 && hashes[i][j-1] == '1' && regions[i][j-1] == 0) fill(hashes, regions, i, j-1, sum)
    if (j < hashes[i].length - 1 && hashes[i][j+1] == '1' && regions[i][j+1] == 0) fill(hashes, regions, i, j+1, sum)
}


private fun countBits(input: String): Int {
    var sum = 0
    for (i in 0 .. 127) {
        var hash = knotHash(256, "$input-$i" )
        for (c in hash) {
            when (c) {
                '0' -> sum = sum
                '1' -> sum += 1
                else -> throw RuntimeException("CMH: c=$c")
            }
        }
    }
    return sum
}

private fun knotHash(size: Int, input: String): String {

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
    var len = (size / 16) - 1
    for (i in 0..len) {
        var c = data[16 * i]
        for (j in 1..15) {
            c = c.xor(data[16 * i + j])
        }
        dense += Integer.toBinaryString(c).padStart(8, '0')
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