package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, 1) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answerI(1)) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answerI(2)) }
}

private fun execute1(input: List<String>): Int {
    var match = 0
    for (line in input) {
        if (line.isEmpty()) continue
        var s = line.replace("\t", " ").trim()
        while (s.contains("  ")) s = s.replace("  ", " ")
        val ls = s.split(" ").map { it.trim().toInt() }.toIntArray()
        if (ls.max() < ls.sum() - ls.max()) match++
    }
    return match
}

private fun execute2(input: List<String>): Int {
    var match = 0
    for (i in input.indices step 3) {
        if (input[i].isEmpty()) continue
        val ls1 = ints(input[i])
        val ls2 = ints(input[i+1])
        val ls3 = ints(input[i+2])
        for (j in 0..2) {
            val max = maxOf(ls1[j], ls2[j], ls3[j])
            val sum = ls1[j] + ls2[j] + ls3[j]
            if (max < sum - max) match++
        }
    }
    return match
}

private fun ints(input: String): IntArray {
    var s = input.replace("\t", " ").trim()
    while (s.contains("  ")) s = s.replace("  ", " ")
    val ls = s.split(" ").map { it.trim().toInt() }.toIntArray()
    return ls
}