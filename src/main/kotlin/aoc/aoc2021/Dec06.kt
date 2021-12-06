package aoc.aoc2021

import aoc.ktutils.*

fun main() {

    check(simulate(readTestText(), 80), 5934)
    println(simulate(readText(), 80)) // 360268

    check(bucketize(readTestText(), 80), 5934L)
    check(bucketize(readTestText(), 256), 26984457539L)
    println(bucketize(readText(), 256)) // 1632146183902
}


private fun simulate(input: String, days: Int): Int {

    var fishes = HashMap<Int, Int>() // age
    var counter = 0

    for (fish in asIntArray(input)) {
        fishes[counter++] = fish
    }

    for (i in 1..days) {
        var next = HashMap<Int, Int>()
        for (fish in fishes.keys) {
            if (fishes[fish] == 0) {
                next[fish] = 6
                next[counter++] = 8
            } else {
                next[fish] = fishes[fish]!! - 1
            }
        }
        fishes = next
    }
    return counter
}

private fun bucketize(input: String, days: Int): Long {

    var buckets = LongArray(9) // age
    for (fish in asIntArray(input)) {
        buckets[fish]++
    }

    for (i in 1..days) {
        var next = LongArray(9)
        next[6] = buckets[0]
        next[8] = buckets[0]
        for (phase in 1 .. 8) {
            next[phase-1] += buckets[phase]
        }
        buckets = next
    }

    return buckets.sum()
}

