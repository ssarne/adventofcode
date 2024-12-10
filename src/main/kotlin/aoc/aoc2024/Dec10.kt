package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 3)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 3)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parse(input)
    return countTrails(map).first
}

private fun execute2(input: List<String>): Int {
    val map = parse(input)
    return countTrails(map).second
}

fun countTrails(map: Map<Point, Int>): Pair<Int, Int> {
    var peaks = 0
    var trails = 0
    for (p in map) {
        if (p.value == 0) {
            val heads = mutableSetOf<Point>()
            trails += countTrails(map, p.key, heads)
            peaks += heads.size
        }
    }
    return peaks to trails
}

fun countTrails(map: Map<Point, Int>, p: Point, peaks: MutableSet<Point>): Int {

    if (map[p] == 9) {
        peaks.add(p)
        return 1
    }

    var sum = 0
    for (n in p.adjacent()) {
        if (map[n] == map[p]?.plus(1)) {
            sum += countTrails(map, n, peaks)
        }
    }
    return sum
}

private fun parse(input: List<String>): Map<Point, Int> {
    val map = parseCharacterGridToMap(input)
    return map.map { it.key to if (it.value.isDigit()) it.value.toString().toInt() else -1 }.toMap()
}