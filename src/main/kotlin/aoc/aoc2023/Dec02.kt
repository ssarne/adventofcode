package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 8)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 2286)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    var sum = 0
    for (line in input) {
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        val id = line.split(": ").first().substring("Game ".length).toInt()
        var imp = 0
        for (set in line.split(": ")[1].split("; ")) {
            val map = HashMap<String, Int>()
            for (c in set.split(", ")) {
                val n = c.split(" ")[0].toInt()
                val color = c.split(" ")[1]
                map[color] = n
            }
            // bag contained only 12 red cubes, 13 green cubes, and 14 blue cubes
            if ((map["red"] ?: 0) > 12) imp++
            if ((map["green"] ?: 0) > 13) imp++
            if ((map["blue"] ?: 0) > 14) imp++
        }
        // sum ids of possible games
        if (imp == 0) sum += id
    }
    return sum
}

private fun execute2(input: List<String>): Int {

    var sum = 0
    for (line in input) {
        // Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
        val id = line.split(": ")[0].substring("Game ".length).toInt()
        val gmap = HashMap<String, Int>()
        for (set in line.split(": ")[1].split("; ")) {
            val map = HashMap<String, Int>()
            for (c in set.trim().split(", ")) {
                val n = c.split(" ")[0].toInt()
                val color = c.split(" ")[1]
                map[color] = n
            }
            for (color in arrayOf("red", "green", "blue")) {
                val n = map[color] ?: 0
                val m = gmap[color] ?: 0
                gmap[color] = if (n > m) n else m
            }
        }
        val power = gmap["red"]!! * gmap["green"]!! * gmap["blue"]!!
        sum += power
    }
    return sum
}