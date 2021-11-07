package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readText

fun main() {
    test()
    println(sumNeighbourPairs(readText("aoc2017/dec01.txt")))
    println(sumOppositePairs(readText("aoc2017/dec01.txt")))
}

fun test() {
    check(sumNeighbourPairs("1122"), 3)
    check(sumNeighbourPairs("1111"), 4)
    check(sumNeighbourPairs("1234"), 0)
    check(sumNeighbourPairs("91212129"), 9)

    check(sumOppositePairs("1212"), 6)
    check(sumOppositePairs("1221"), 0)
    check(sumOppositePairs("123425"), 4)
    check(sumOppositePairs("123123"), 12)
    check(sumOppositePairs("12131415"), 4)
}

fun sumNeighbourPairs(input: String): Int {
    var sum = 0
    for (i in input.indices)
        if (input[i] == input[(i + 1) % input.length])
            sum += Character.getNumericValue(input[i])
    return sum
}

fun sumOppositePairs(input: String): Int {
    var sum = 0
    for (i in input.indices)
        if (input[i] == input[(i + (input.length / 2)) % input.length])
            sum += Character.getNumericValue(input[i])
    return sum
}