package aoc.aoc2025

import aoc.ktutils.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    execute1(readTestLines(1)).let { check(it, 5L) ; println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(2)).let { check(it, 2L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val servers = parseInput(input)
    return countPaths1(servers, "you", "out")
}

private fun countPaths1(servers: HashMap<String, HashSet<String>>, current: String, target: String): Long {

    if (current == target) return 1L

    var sum = 0L
    for (next in servers[current]!!) {
        sum += countPaths1(servers, next, target)
    }

    return sum
}

private fun execute2(input: List<String>): Long {

    val servers = parseInput(input)
    return countPaths2(servers, "svr", false, false, HashMap<String, Long>())
}

private fun countPaths2(servers: HashMap<String, HashSet<String>>, current: String, dac: Boolean, fft: Boolean, memory: HashMap<String, Long>): Long {

    if (current == "out") return if (dac && fft) 1L else 0L

    val key = "$current $dac $fft"
    if (memory.containsKey(key)) return memory[key]!!

    var sum = 0L
    for (next in servers[current]!!) {
        sum += countPaths2(servers, next, dac || current == "dac", fft || current == "fft", memory)
    }
    memory[key] = sum
    return sum
}

private fun parseInput(input: List<String>): HashMap<String, HashSet<String>> {

    val servers = HashMap<String, HashSet<String>>()
    for (line in input) {
        val id = line.split(":").first()
        val output = HashSet<String>()
        val outputs = line.split(": ")[1].trim().split(" ")
        for (next in outputs) output.add(next)
        servers.put(id, output)
    }
    servers.put("out", HashSet())
    return servers
}
