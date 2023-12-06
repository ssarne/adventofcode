package aoc.ktutils

import kotlin.math.abs

fun binarySearch(low: Long, high: Long, calculate: (Long) -> Long, analyze: (Long) -> Boolean): Long {

    var i = low
    var j = high
    while (abs(j-i) > 1) {
        val k = (i + j) / 2
        val n = calculate(k)
        if (analyze(n)) i = k else j = k
    }
    var res = i
    for (k in i..j) {
        val dist = calculate(k)
        if (analyze(dist)) res = k
    }
    return res
}