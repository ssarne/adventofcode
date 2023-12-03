package aoc.aoc2021

import aoc.ktutils.*

fun main() {

    check(simulate(readTestText(), 80), 5934)
    simulate(readText(), 80).let { println(it); check(it, readAnswerAsInt(1)) }

    check(bucketize(readTestText(), 80), 5934L)
    check(bucketize(readTestText(), 256), 26984457539L)
    bucketize(readText(), 256).let { println(it); check(it, readAnswerAsLong(2)) }
}


private fun simulate(input: String, days: Int): Int {

    var fish = HashMap<Int, Int>() // age
    var counter = 0

    for (f in asIntArray(input)) {
        fish[counter++] = f
    }

    for (i in 1..days) {
        val next = HashMap<Int, Int>()
        for (f in fish.keys) {
            if (fish[f] == 0) {
                next[f] = 6
                next[counter++] = 8
            } else {
                next[f] = fish[f]!! - 1
            }
        }
        fish = next
    }
    return counter
}

private fun bucketize(input: String, days: Int): Long {

    var buckets = LongArray(9) // age
    for (fish in asIntArray(input)) {
        buckets[fish]++
    }

    for (i in 1..days) {
        val next = LongArray(9)
        next[6] = buckets[0]
        next[8] = buckets[0]
        for (phase in 1..8) {
            next[phase - 1] += buckets[phase]
        }
        buckets = next
    }

    return buckets.sum()
}
