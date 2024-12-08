package aoc.aoc2024

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 14)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 34)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    val (size, antennas) = parse(input)
    val hotSpots = mutableSetOf<Point>()

    for (locations in antennas.values) {
        for (l1 in locations) {
            for (l2 in locations) {
                if (l1 == l2) continue
                val d = l2 - l1
                (l2 + d).let { if (it inside size) hotSpots.add(it) }
            }
        }
    }

    return hotSpots.size
}

private fun execute2(input: List<String>): Int {

    val (size, antennas) = parse(input)
    val hotSpots = mutableSetOf<Point>()

    for (ls in antennas.values) {
        for (l1 in ls) {
            for (l2 in ls) {
                if (l1 == l2) continue
                val d = l2 - l1
                for (i in 1..max(size.second.x, size.second.y)) {
                    (l1 + d * i).let { if (it inside size) hotSpots.add(it) }
                    (l1 - d * i).let { if (it inside size) hotSpots.add(it) }
                }
            }
        }
    }

    return hotSpots.size
}

private fun parse(input: List<String>): Pair<Pair<Point, Point>, MutableMap<Char, MutableSet<Point>>> {
    val map = parseCharacterGridToMap(input)
    val size = mapSize(map)
    val antennas = mutableMapOf<Char, MutableSet<Point>>()
    for (e in map.entries) {
        if (e.value != '.') {
            var locations = antennas[e.value]
            if (locations == null) {
                locations = mutableSetOf<Point>()
                antennas.put(e.value, locations)
            }
            locations.add(e.key)
        }
    }
    return Pair(size, antennas)
}

