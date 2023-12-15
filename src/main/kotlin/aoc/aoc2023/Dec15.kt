package aoc.aoc2023

import aoc.ktutils.*

fun main() {
    check(hash("HASH"), 52)
    check(execute1(readTestText(1)), 1320)
    execute1(readText()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestText(1)), 145)
    execute2(readText()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

fun hash(s: String): Int {
    var r = 0
    for (c in s) {
        r += c.code
        r *= 17
        r %= 256
    }
    return r
}


private fun execute1(input: String): Int {
    val inputs = input.split(",")
    var sum = 0
    for (s in inputs) {
        sum += hash(s)
    }
    return sum
}

private fun execute2(input: String): Int {

    val map = Array(256) {ArrayList<Pair<String, Int>>()}
    val inputs = input.split(",")

    for (s in inputs) {
        if (s.contains("=")) {
            val label = s.split("=")[0]
            val num = s.split("=")[1].toInt()
            val pos = hash(label)
            val index = map[pos].indexOfFirst {it.first == label}
            if (index < 0) {
                map[pos].add(label to num)
            } else {
                map[pos][index] = label to num
            }
        }
        if (s.contains("-")) {
            val label = s.split("-")[0]
            val pos = hash(label)
            val index = map[pos].indexOfFirst {it.first == label}
            if (index >= 0) map[pos].removeAt(index)
        }
    }

    var sum = 0
    for (i in map.indices) {
        for (j in map[i].indices) {
            val prod = (i + 1) * (j + 1) * map[i][j].second
            sum += prod
        }
    }
    return sum
}