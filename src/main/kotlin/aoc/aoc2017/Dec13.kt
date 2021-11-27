package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readTestLines
import aoc.ktutils.readLines

fun main() {
    check(severity(parse(readTestLines()), 0).second, 24)
    println(severity(parse(readLines()), 0).second) // 1580
    check(probe(parse(readTestLines())), 10)
    println(probe(parse(readLines()))) // 3943252
}

private fun probe(firewall: Map<Int, Int>): Int {
    var startTime = 0
    while (severity(firewall, startTime).first == true) startTime++
    return startTime
}

private fun severity(firewall: Map<Int, Int>, startTime: Int): Pair<Boolean, Int> {
    var severity = 0
    var caught = false
    var max = firewall.keys.max() ?: 0
    for (level in 0..max) {
        var range = firewall[level]
        if (range != null) {
            if (((startTime + level) % (2 * (range - 1))) == 0) {
                caught = true
                severity += level * range
            }
        }
    }
    return Pair(caught, severity)
}

private fun parse(lines: List<String>): Map<Int, Int> {
    var firewall = hashMapOf<Int, Int>()
    for (line in lines) {
        var tokens = line.split(": ")
        firewall.put(tokens.first().toInt(), tokens.last().toInt())
    }
    return firewall
}
