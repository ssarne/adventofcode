package aoc.aoc2022.dec25

import aoc.ktutils.*

fun main() {
    check(snafuToDecimal("1"), 1L)
    check(snafuToDecimal("10"), 5L)
    check(snafuToDecimal("1121-1110-1=0"), 314159265L)
    check(snafuToDecimal("2=-1=0"), 4890L)
    check(decToStr(123, ""), "123")
    check(decToSnafu(1L, ""), "1")
    check(decToSnafu(4890L, ""), "2=-1=0")

    check(execute1(readTestLines()), "2=-1=0")
    execute1(readLines()).let { println(it); check(it, readAnswer(1)) }
}

private fun execute1(input: List<String>): String {
    var sum = 0L
    for (line in input)
        sum += snafuToDecimal(line)
    val snafu = decToSnafu(sum, "")
    return snafu
}

fun snafuToDecimal(snafu: String): Long {
    return ufansToDecimal(snafu.reversed(), 0, 1)
}

fun ufansToDecimal(ufans: String, pos: Int, mul: Long): Long {
    if (pos == ufans.length) return 0L
    return when (ufans[pos]) {
        '=' -> -2 * mul + ufansToDecimal(ufans, pos + 1, mul * 5)
        '-' -> -1 * mul + ufansToDecimal(ufans, pos + 1, mul * 5)
        '0' -> 0 * mul + ufansToDecimal(ufans, pos + 1, mul * 5)
        '1' -> 1 * mul + ufansToDecimal(ufans, pos + 1, mul * 5)
        '2' -> 2 * mul + ufansToDecimal(ufans, pos + 1, mul * 5)
        else -> throw RuntimeException("CMH ${ufans[pos]}")
    }
}

fun snafuToDecimalOld(snafu: String): Long {
    var d = 0L
    for ((i, c) in snafu.reversed().withIndex())
        when (c) {
            '=' -> d += -2 * Math.pow(5.0, i.toDouble()).toLong()
            '-' -> d += -1 * Math.pow(5.0, i.toDouble()).toLong()
            '0' -> d += 0
            '1' -> d += 1 * Math.pow(5.0, i.toDouble()).toLong()
            '2' -> d += 2 * Math.pow(5.0, i.toDouble()).toLong()
        }
    return d
}

fun decToStr(dec: Long, str: String): String {
    if (dec == 0L && str.length > 0L) return str
    val rest = dec % 10
    val rem = dec / 10
    return decToStr(rem, rest.toString() + str)
}


fun decToSnafu(dec: Long, snafu: String): String {
    val sn = charArrayOf('0', '1', '2', '=', '-')
    if (dec == 0L && snafu.length > 0L) return snafu
    var rest = Math.floorMod(dec, 5)
    var rem = (dec + 2) / 5
    if (rest !in 0..4) throw RuntimeException("CMH $rest")
    return decToSnafu(rem, sn[rest] + snafu)
}