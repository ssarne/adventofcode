package aoc.aoc2022.dec16

import aoc.ktutils.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {

    // "AA>DD>__>CC>BB>__>AA>II>JJ>__>II>AA>DD>EE>FF>GG>HH>__>GG>FF>EE>__>DD>CC>__>"
    check(execute1(readTestLines()), 1651)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 1707)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

const val DURATION = 30
enum class Action { open, move, stay }
data class Valve(val id: Int, val name: String, val rate: Int) {
    val neighbours = ArrayList<Valve>()
}

// Find the best ways to open the valves in 30 minutes
fun execute1(input: List<String>): Int {
    val valves = parse(input)
    val valve = valves["AA"]!!
    return dfs(valve, Action.stay, valve,0, 0, 0, BitSet(), HashMap(), HashMap())
}


// Find the best ways to open the valves in 30-4 minutes (with 2 workers)
// Store best result for each combination of valves (memory2 in dfs)
// Add up any two complementing bitmaps, take best pair
fun execute2(input: List<String>): Int {

    val valves = parse(input)
    val valve = valves["AA"]!!
    val memory2 = HashMap<BitSet, Int>()
    dfs(valve, Action.stay, valve, 4, 0, 0, BitSet(), HashMap(), memory2)

    var best = 0
    for (mem1 in memory2.keys) {
        for (mem2 in memory2.keys) {
            var res = mem1.clone() as BitSet
            res.and(mem2)
            if (res.isEmpty) {
                val pot1 = memory2[mem1] ?: 0
                val pot2 = memory2[mem2] ?: 0
                if (best < pot1 + pot2) best = pot1 + pot2
            }
        }
    }

    return best
}

/**
 * Depth first search through the cave system, answering how much pressure is released with this path
 * Neighbours are sorted for a greedy search, based on next valve
 * In addition to Open and Move to next cave, staying still is a last option
 * Memory1 - Memoize on time, valve, pressure released and open valves
 * Memory2 - Store best opening order for each combination of valves
 */
fun dfs(
    valve: Valve,
    action: Action,
    prev: Valve,
    time: Int,
    rate: Int,
    released: Int,
    open: BitSet,
    memory1: HashMap<String, Int>,
    memory2: HashMap<BitSet, Int>
): Int {

    // End condition, time is up after 30 rounds.
    if (time == DURATION) return 0

    // Memoization check, don't continue after reaching the same vault with the same result
    // Release is needed here to populate memory2 correctly
    val key1 = "$time ${valve.name} $released $open"   // -> pressure
    memory1[key1].let { if (it != null) return it }

    // Store best result for each permutation of open valves
    val total = released + rate * (DURATION - time)
    memory2[open].let {
        if (it == null || it < total) {
            val key2 = open.clone() as BitSet
            memory2[key2] = total
        }
    }

    // do nothing for the rest of the time
    var best = (DURATION - time - 1) * rate

    // open this valve
    if (valve.rate > 0 && !open[valve.id]) {
        open[valve.id] = true
        var pot = dfs(valve, Action.open, valve, time + 1, rate + valve.rate, released + rate, open, memory1, memory2)
        if (pot > best) {
            best = pot
        }
        open[valve.id] = false
    }

    // move on to another valve
    for (next in valve.neighbours) {
        // Don't go back the same path directly
        if (action == Action.move && prev == next) continue

        var pot = dfs(next, Action.move, valve, time + 1, rate, released + rate, open, memory1, memory2)
        if (pot > best) {
            best = pot
        }
    }

    // Add up, with the pressure released this round
    var pressure = best + rate

    // Memoize this result
    memory1[key1] = pressure

    return pressure
}

fun parse(input: List<String>): Map<String, Valve> {
    val valves = HashMap<String, Valve>()
    for (line in input) {
        val chunks = line.replace(";", "").replace(",", "").replace("=", " ").split(" ")
        val valve = Valve(valves.size, chunks[1], chunks[5].toInt())
        valves[chunks[1]] = valve
    }
    for (line in input) {
        val tokens = line.replace(";", "").replace(",", "").replace("=", " ").split(" ")
        val valve = valves[tokens[1]]!!
        for (i in 10 until tokens.size) {
            valves[tokens[i]]?.let { valve.neighbours.add(it) }
        }
        valve.neighbours.sortBy { it.rate }
        valve.neighbours.reverse()
    }

    return valves
}