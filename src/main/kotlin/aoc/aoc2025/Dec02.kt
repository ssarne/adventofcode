package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 1227775554L); println("Test: $it") }
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 4174379265L); println("Test: $it") }
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    var sum = 0L

    val ranges = input.first().split(",")
    for (range in ranges) {
        val start = range.split("-").first().toLong()
        val end = range.split("-").last().toLong()
        for (i in start..end) {
            val id = i.toString()
            val t = id.substring(0, id.length / 2)
            val tt = t + t
            if (tt == id)
                sum += i
        }
    }

    return sum
}

private fun execute2(input: List<String>): Long {

    var sum = 0L

    val ranges = input.first().split(",")
    for (range in ranges) {
        val start = range.split("-").first().toLong()
        val end = range.split("-").last().toLong()
        for (i in start..end) {
            val id = i.toString()
            for (j in 1..id.length - 1) {
                val t = id.substring(0, j)
                var tt = t + t
                while (tt.length < id.length)
                    tt += t
                if (tt == id) {
                    sum += i
                    break
                }
            }
        }
    }

    return sum
}