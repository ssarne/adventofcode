package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), "easter")
    execute1(readLines()).let { println(it) ; check(it, readAnswer(1)) }

    check(execute2(readTestLines(1)), "advent")
    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
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