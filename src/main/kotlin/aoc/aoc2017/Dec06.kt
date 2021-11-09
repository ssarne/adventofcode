package aoc.aoc2017

import aoc.utils.Utils
import aoc.ktutils.check
import java.util.*

data class Result(var allocs: Int, var loop: Int)

fun main() {
    check(countReallocations(intArrayOf(0, 2, 7, 0)).allocs, 5)
    println(countReallocations(input(null)).allocs) // 14029
    check(countReallocations(intArrayOf(0, 2, 7, 0)).loop, 4)
    println(countReallocations(input(null)).loop) // 2765
}

fun countReallocations(banks: IntArray): Result {
    var history = HashMap<String, Int>()
    var allocs = 0
    while (!history.contains(Arrays.toString(banks))) {
        //println("$allocs: " + Arrays.toString(banks))
        history.put(Arrays.toString(banks), allocs)
        var pos = getMax(banks)
        var blocks = banks[pos]
        banks[pos] = 0
        for (i in 1..blocks) {
            banks[(pos+i) % banks.size]++
            // println("   " + Arrays.toString(banks))
        }
        allocs++
    }
    return Result(allocs, allocs - history.getOrDefault(Arrays.toString(banks), 0))
}

fun getMax(banks: IntArray): Int {
    var max = 0
    for (i in banks.indices)
        if (banks[i] > banks[max])
            max = i
    return max
}

private fun input(fileName: String?): IntArray {
    var ints = Utils.getLines(fileName)
        .first()
        .split(" ", "\t")
        .map { t -> t.toInt() }
        .toIntArray()
    return ints
}
