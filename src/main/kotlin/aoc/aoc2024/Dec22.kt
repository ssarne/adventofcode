package aoc.aoc2024

import aoc.ktutils.*

fun main() {

    execute1(readTestLines(1)).let { check(it, 37327623); println("Test: $it") }
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    //execute2(readTestLines(2)).let { check(it, 23); println("Test: $it") }
    //execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(2)) }

    execute22(readTestLines(2)).let { check(it, 23); println("Test: $it") }
    execute22(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class Monkey(val id: Int, val seed: Long) {

}

private fun execute1(input: List<String>): Long {

    var secrets = parse(input)
    for (i in 0 until 2000) {
        val next = mutableListOf<Long>()
        for (n in secrets)
            next.add(secret(n))
        secrets = next
    }
    return secrets.sum()
}

private fun execute22(input: List<String>): Long {
    val seeds = input.map { it.toLong() }.toList()
    val monkeys = mutableListOf<Triple<LongArray, IntArray, IntArray>>()
    for (seed in seeds) {
        val monkey = Triple(LongArray(2001), IntArray(2001), IntArray(2001))
        monkey.first[0] = secret(seed)
        monkey.second[0] = (monkey.first[0] % 10).toInt()
        monkey.third[0] = 0
        for (i in 1..2000) {
            monkey.first[i] = secret(monkey.first[i-1])
            monkey.second[i] = (monkey.first[i] % 10).toInt()
            monkey.third[i] = monkey.second[i] -  monkey.second[i-1]
        }
        monkeys.add(monkey)
    }

    var maxBananas = 0
    for (c1 in -9..9) {
        for (c2 in -9..9) {
            for (c3 in -9..9) {
                for (c4 in -9..9) {
                    var bananas = 0
                    for (monkey in monkeys) {
                        bananas += getBananas(monkey, c1, c2, c3, c4)
                    }
                    if (bananas > maxBananas) {
                        maxBananas = bananas
                    }
                }
            }
            println("Max Bananas ($c1 $c2) $maxBananas")
        }
    }

    return maxBananas.toLong()
}

private fun getBananas(monkey: Triple<LongArray, IntArray, IntArray>, c1: Int, c2: Int, c3: Int, c4: Int ): Int {
    val diffs = monkey.third
    for (c in 4..2000) {
        if (diffs[c - 3] == c1 && diffs[c - 2] == c2 && diffs[c - 1] == c3 && diffs[c] == c4) {
            return monkey.second[c]
        }
    }
    return 0
}


private fun execute21(input: List<String>): Long {

    val seeds = parse(input)
    var maxBananas = 0

    for (c1 in -9..9) {
        for (c2 in -9..9) {
            for (c3 in -9..9) {
                for (c4 in -9..9) {
                    var bananas = 0
                    for (seed in seeds) {

                        var s1 = secret(seed) // secrets
                        var s2 = secret(s1)
                        var s3 = secret(s2)
                        var s4 = secret(s3)
                        var s5 = secret(s4)

                        var p1 = (s1 % 10).toInt()
                        var p2 = (s2 % 10).toInt()
                        var p3 = (s3 % 10).toInt()
                        var p4 = (s4 % 10).toInt()
                        var p5 = (s5 % 10).toInt()

                        var d1 = p2 - p1
                        var d2 = p3 - p2
                        var d3 = p4 - p3
                        var d4 = p5 - p4

                        for (c in 5..2000) {
                            if (d1 == c1 && d2 == c2 && d3 == c3 && d4 == c4) {
                                bananas += p5
                                break
                            }
                            s1 = s2; s2 = s3; s3 = s4; s4 = s5; s5 = secret((s5))
                            p1 = p2; p2 = p3; p3 = p4; p4 = p5; p5 = (s5 % 10).toInt()
                            d1 = d2; d2 = d3; d3 = d4; d4 = p5 - p4
                        }
                    }
                    if (bananas > maxBananas) {
                        maxBananas = bananas
                    }
                }
            }
            println("Max Bananas ($c1 $c2) $maxBananas")
        }
    }

    return maxBananas.toLong()
}

fun secret(secret: Long): Long {
    var n = secret
    //n = prune(mix(n * 64L))
    //n = prune(mix(n / 32L))
    //n = prune(mix(n * 32L))
    n = ((n * 64L) xor n) % 16777216L
    n = ((n / 32L) xor n) % 16777216L
    n = ((n * 2048L) xor n) % 16777216L
    return n
}

private fun parse(input: List<String>): List<Long> {

    var numbers = mutableListOf<Long>()
    for (line in input) {
        numbers.add(line.toLong())
    }
    return numbers
}