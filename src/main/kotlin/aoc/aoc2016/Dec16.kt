package aoc.aoc2016

import aoc.ktutils.*
import java.util.BitSet

fun main() {

    test()
    
    execute(testLines(), 20).let { println("Test:   $it") ; check(it, "01100") }
    execute(readLines(), 272).let { println("Result: $it") ; check(it, answerS(1)) }
    execute(readLines(), 35651584).let { println("Result: $it") ; check(it, answerS(2)) }
}

private fun execute(input: List<String>, diskSize: Int): String {

    var state = parse(input.first()) // bitset -> length
    while (state.second < diskSize) {
        state = extend(state)
    }
    state = state.first to diskSize
    state = checksum(state)
    return asBinaryString(state)
}

private fun parse(input: String): Pair<BitSet, Int> {
    val bitset = BitSet()
    var pos = 0
    for (c in input) bitset.set(pos++, c == '1')
    return bitset to pos
}

private fun checksum(state: Pair<BitSet, Int>): Pair<BitSet, Int> {
    var result = state
    do {
        val checksum = BitSet()
        for (i in 0 until result.second step 2)
            checksum.set(i / 2, result.first.get(i) == result.first.get(i + 1))
        result = checksum to result.second / 2
    } while (result.second % 2 == 0)
    return result
}

private fun extend(state: String): String {
    val a = state
    var b = ""
    for (i in a.indices) b += if (a[a.length - i - 1] == '1') "0" else "1"
    return a + "0" + b
}

private fun extend(state: Pair<BitSet, Int>): Pair<BitSet, Int> {
    val next = BitSet()
    var pos = 0
    for (b in 0 until state.second)
        next.set(pos++, state.first.get(b))
    next.set(pos++, false)
    for (i in 0 until state.second)
        next.set(pos++, if (state.first.get(state.second - i - 1)) false else true)
    return next to pos
}

private fun asBinaryString(state: Pair<BitSet, Int>): String {
    val result = StringBuilder()
    for (i in 0 until state.second)
        result.append(if (state.first.get(i)) '1' else '0')
    return result.toString()
}

private fun test() {
    check(extend("1"), "100")
    check(extend("0"), "001")
    check(extend("11111"), "11111000000")
    check(extend("111100001010"), "1111000010100101011110000")
    // check(checksum("110010110100"), "100")
    check(asBinaryString(checksum(parse("110010110100"))), "100")
}
