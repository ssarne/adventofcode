package aoc.aoc2024

import aoc.ktutils.*

fun main() {

    execute1(readTestLines(1)).let { check(it, 37327623) } // ; println("Test: $it") }
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(2)).let { check(it, 23) } // ; println("Test: $it") }
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    var secrets = input.map { it.toLong() }.toList()
    for (i in 0 until 2000) {
        val next = mutableListOf<Long>()
        for (n in secrets)
            next.add(secret(n))
        secrets = next
    }
    return secrets.sum()
}

fun secret(secret: Long): Long {
    var n = secret
    n = ((n * 64L) xor n) % 16777216L
    n = ((n / 32L) xor n) % 16777216L
    n = ((n * 2048L) xor n) % 16777216L
    return n
}

private data class Monkey(val seed: Long) {

    val secrets = LongArray(2001)
    val prices = IntArray(2001)
    val diffs = IntArray(2001)
    val sequences = mutableMapOf<Quadruple<Int, Int, Int, Int>, Int>()

    init {
        secrets[0] = seed
        for (i in 1..2000) secrets[i] = secret(secrets[i - 1])
        for (i in 0..2000) prices[i] = (secrets[i] % 10).toInt()
        for (i in 1..2000) diffs[i] = prices[i] - prices[i - 1]
        for (i in 4..2000)
            Quadruple(diffs[i - 3], diffs[i - 2], diffs[i - 1], diffs[i]).let {
                if (!sequences.containsKey(it))
                    sequences[it] = prices[i]
            }
    }
}

private fun execute2(input: List<String>): Long {

    val seeds = input.map { it.toLong() }.toList()
    val monkeys = mutableListOf<Monkey>()
    for (seed in seeds) {
        monkeys.add(Monkey(seed))
    }

    var max = 0
    for (c1 in -9..9) {
        for (c2 in -9..9) {
            for (c3 in -9..9) {
                for (c4 in -9..9) {
                    val sequence = Quadruple(c1, c2, c3, c4)
                    var bananas = 0
                    for (monkey in monkeys) {
                        bananas += monkey.sequences.getOrDefault(sequence, 0)
                    }
                    if (bananas > max) {
                        max = bananas
                    }
                }
            }
        }
    }

    return max.toLong()
}
