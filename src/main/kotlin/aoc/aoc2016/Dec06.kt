package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, "easter") }
    execute1(readLines()).let { println("Result: $it") ; check(it, answerS(1)) }

    execute2(testLines()).let { println("Test:   $it") ; check(it, "advent") }
    execute2(readLines()).let { println("Result: $it") ; check(it, answerS(2)) }
}

private fun execute1(input: List<String>): String {
    return execute(
        input,
        fun(a, b): Map.Entry<Char, Long> {
            return if (a.value > b.value) a else b
        })
}


private fun execute2(input: List<String>): String {
    return execute(
        input,
        fun(a, b): Map.Entry<Char, Long> {
            return if (a.value < b.value) a else b
        })
}

private fun execute(input: List<String>,
                    comp: (a: Map.Entry<Char, Long>, b: Map.Entry<Char, Long>) -> Map.Entry<Char, Long>): String {

    // position -> letter -> occurrences
    val letters = Array(input.first().length) { mutableMapOf<Char, Long>() }

    for (line in input) {
        for ((i, c) in line.withIndex()) {
            letters[i].addOrPut(c, 1L)
        }
    }

    var res = ""
    for (m in letters) {
        val c = m.entries.reduce(comp).key
        res += c
    }

    return res
}