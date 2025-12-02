package aoc.aoc2025

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    //execute1(readTestLines(1)).let { check(it, -1L) ; println("Test: $it") }
    ///(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 4174379265L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    var sum = 0L

    for (line in input) {
        val ranges = line.split(",")
        for (range in ranges) {
            if (range == "") continue
            val start = range.split("-").first().toLong()
            val end = range.split("-").last().toLong()
            for (i in start .. end) {
                val id = i.toString()
                val token = id.substring(0, id.length / 2)
                val tt = token + token
                if (tt == id) sum += i
            }
        }
    }

    return sum
}

private fun execute2(input: List<String>): Long {
    var sum = 0L

    for (line in input) {
        val ranges = line.split(",")
        for (range in ranges) {
            if (range == "") continue
            val start = range.split("-").first().toLong()
            val end = range.split("-").last().toLong()
            for (i in start .. end) {
                val id = i.toString()
                for (j in 1 .. id.length - 1) {
                    val token = id.substring(0, j)
                    var tt = token + token
                    while (tt.length < id.length)
                        tt += token
                    if (tt == id) {
                        sum += i
                        println(i)
                        break
                    }
                }
            }
        }
    }

    return sum
}