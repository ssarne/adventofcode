package aoc.aoc2022.dec16

import aoc.ktutils.*
import java.util.*
import kotlin.math.*
import kotlin.RuntimeException
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    //check(execute1(readTestLines(), 1), 0)
    //check(execute1(readTestLines(), 2), 0)
    //check(execute1(readTestLines(), 3), 20)
    //check(execute1(readTestLines(), 4), 40)
    //check(execute1(readTestLines(), 5), 63)

    // "AA>DD>__>CC>BB>__>AA>II>JJ>__>II>AA>DD>EE>FF>GG>HH>__>GG>FF>EE>__>DD>CC>__>"
    check(execute1(readTestLines(), 30),1651)
    // execute1(readLines(), 30).let { println(it); check(it, readAnswerAsInt(1)) }

    //check(execute2(readTestLines(), 10), 1707)
    check(execute2(readTestLines(), 30), 1707)
    // execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
}

private data class Valve(val id: Int, val name: String, val rate: Int) {
    val neighbours = ArrayList<Valve>()
    val visited = HashMap<BitSet, Triple<Int, Int, Int>>()
    val visited2 = HashMap<BitSet, Triple<Int, Int, Int>>()
}

private enum class Action {open, move, stay}

private fun execute1(input: List<String>, duration: Int): Int {
    val valves = parse(input)
    val valve = valves["AA"]!!
    //val (pressure, path) = dfs_trace(valve, Action.move, duration, 0, 0, 0, "AA>", BitSet())
    // println("$path  $pressure")
    val pressure = dfs(valve, valve, Action.stay, duration, 0, 0, 0, BitSet())
    println("$pressure")
    return pressure
}


private fun execute2(input: List<String>, duration: Int): Int {

    val valves = parse(input)
    val valve = valves["AA"]!!
    // val (pressure, path1, path2) = dfs2(duration, 4, 0, 0, valve, valve, Action.move, Action.move, "H:>", "E:>", BitSet(), BitSet())
    val pressure = dfs2(duration, 4, 0, 0, valve, valve, valve, valve, Action.stay, Action.stay, BitSet(), BitSet())
    println("=====  $pressure  =====")
    return pressure
}

private fun dfs(valve: Valve, prev: Valve, action: Action, duration: Int, time: Int, rate: Int, tot: Int, open: BitSet): Int {

    // println("$time  $trace ($open)  r=$rate p=$tot")
    assert(time > duration)
    if (time == duration) return tot
    if (visited(action, valve, valve.visited, open, time, rate, tot)) return -1

    // Best so far, to do nothing for the rest of the period
    var best = tot + (duration - time) * rate

    // open this one
    if (valve.rate > 0 && !open[valve.id]) {
        open[valve.id] = true
        valve.visited[open.clone() as BitSet] = Triple(time, rate, tot)
        var pot  = dfs(valve, valve, Action.open, duration, time + 1, rate + valve.rate, tot + rate, open)
        if (pot > best) best = pot
        open[valve.id] = false
    }

    // move on
    for (next in valve.neighbours) {
        if (action == Action.move && prev == next) continue
        var pot = dfs(next, valve, Action.move, duration, time + 1, rate, tot + rate, open)
        if (pot > best) best = pot
    }

    return best
}


private fun dfs_trace(valve: Valve, action: Action, duration: Int, time: Int, rate: Int, tot: Int, trace: String, open: BitSet): Pair<Int, String> {

    // println("$time  $trace ($open)  r=$rate p=$tot")
    assert(time > duration)
    if (time == duration) return Pair(tot, trace)

    if (action == Action.move) {
        if (valve.visited.containsKey(open)) {
            if (better(valve.visited[open]!!, time, rate, tot)) {
                return Pair(-1, trace)
            }
        } else {
            valve.visited[open] = Triple(time, rate, tot)
        }
    }

    var best = tot
    var path = trace

    // Do nothing for the rest of the period
    for (i in time + 1..duration) {
        best += rate
        path += "-->"
    }

    // open this one
    if (valve.rate > 0 && !open[valve.id]) {
        open[valve.id] = true
        valve.visited[open.clone() as BitSet] = Triple(time, rate, tot)
        var (pot, potp) = dfs_trace(valve, Action.open, duration, time + 1, rate + valve.rate, tot + rate, trace + "__" + ">", open)
        if (pot > best) {
            best = pot
            path = potp
        }
        open[valve.id] = false
    }

    // move on
    for (next in valve.neighbours) {
        if (!trace.endsWith(next.name + ">" + valve.name + ">")) {
            var (pot, potp) = dfs_trace(next, Action.move, duration, time + 1, rate, tot + rate, trace + next.name + ">", open)
            if (pot > best) {
                best = pot
                path = potp
            }
        }
    }

    return Pair(best, path)
}

fun better(existing: Triple<Int, Int, Int>, time: Int, rate: Int, tot: Int): Boolean {
    val dt = time - existing.first
    if (dt == 0) return existing.second >= tot
    if (dt > 0) return existing.second + dt * existing.third >= tot
    return false // if (dt < 0) dunno
 }

private fun dfs2(
    duration: Int, time: Int, rate: Int, tot: Int,
    valve1: Valve, valve2: Valve,
    prev1: Valve, prev2: Valve,
    action1: Action, action2: Action,
    open1: BitSet, open2: BitSet
): Int {

    //println("$time r=$rate p=$tot")

    if (time == duration) return tot
    if (time > duration) throw RuntimeException("wtf")
    if (visited(action1, valve1, valve1.visited, open1, time, rate, tot)) return -1
    if (visited(action2, valve2, valve2.visited2, open2, time, rate, tot)) return -1

    var best = tot

    // Do nothing for the rest of the period
    for (i in time + 1..duration) {
        best += rate
    }

    val v1nexts = ArrayList<Valve>()
    if (valve1.rate > 0 && !open1[valve1.id] && !open2[valve1.id]) v1nexts.add(valve1)
    v1nexts.addAll(valve1.neighbours)
    if (action1 == Action.move) v1nexts.remove(prev1)
    val v2nexts = ArrayList<Valve>()
    if (valve2.rate > 0 && !open1[valve1.id] && !open2[valve1.id] && valve1 != valve2) v2nexts.add(valve2)
    v2nexts.addAll(valve2.neighbours)
    if (action2 == Action.move) v2nexts.remove(prev2)

    for (nextValve1 in v1nexts) {
        for (nextValve2 in v2nexts) {
            if (nextValve1 == valve1 && nextValve2 == valve2 && nextValve1 == nextValve2) throw RuntimeException("wtf")
            var nextRate = rate
            var nextAction1 = if (nextValve1 == valve1) Action.open else Action.move
            var nextAction2 = if (nextValve2 == valve2) Action.open else Action.move
            if (nextAction1 == Action.open) {
                open1[valve1.id] = true
                valve1.visited[open1.clone() as BitSet] = Triple(time, rate, tot)
                nextRate += valve1.rate
            }
            if (nextAction2 == Action.open) {
                open2[valve2.id] = true
                valve2.visited2[open2.clone() as BitSet] = Triple(time, rate, tot)
                nextRate += valve2.rate
            }
            var pot = dfs2(duration,time + 1, nextRate,rate + tot,
                nextValve1, nextValve2,
                valve1, valve2,
                nextAction1, nextAction2,
                open1, open2)
            if (pot > best) {
                best = pot
            }
            if (nextAction1 == Action.open) {
                open1[valve1.id] = false
            }
            if (nextAction2 == Action.open) {
                open2[valve2.id] = false
            }
        }
    }
    return best
}

/*
private fun dfs2_trace(
    duration: Int, time: Int, rate: Int, tot: Int,
    valve1: Valve, valve2: Valve,
    action1: Action, action2: Action,
    trace1: String, trace2: String,
    open1: BitSet, open2: BitSet
): Triple<Int, String, String> {

    //println("$time r=$rate p=$tot")
    //println("   $trace1 ($open1)")
    //println("   $trace2 ($open2)")

    if (time == duration) return Triple(tot, trace1, trace2)
    if (time > duration) throw RuntimeException("wtf")
    if (visited(action1, valve1, open1, time, rate, tot)) return Triple(-1, trace1, trace2)
    if (visited(action2, valve2, open2, time, rate, tot)) return Triple(-1, trace1, trace2)

    var best = tot
    var path1 = trace1
    var path2 = trace2

    // Do nothing for the rest of the period
    for (i in time + 1..duration) {
        best += rate
        path1 += "-->"
        path2 += "-->"
    }

    val v1nexts = ArrayList<Valve>()
    if (valve1.rate > 0 && !open1[valve1.id] && !open2[valve1.id]) v1nexts.add(valve1)
    v1nexts.addAll(valve1.neighbours)
    val v2nexts = ArrayList<Valve>()
    if (valve2.rate > 0 && !open1[valve1.id] && !open2[valve1.id] && valve1 != valve2) v2nexts.add(valve2)
    v2nexts.addAll(valve2.neighbours)

    for (nextValve1 in v1nexts) {
        for (nextValve2 in v2nexts) {
            if (nextValve1 == valve1 && nextValve2 == valve2 && nextValve1 == nextValve2) throw RuntimeException("wtf")
            var nextRate = rate
            var nextAction1 = if (nextValve1 == valve1) Action.open else Action.move
            var nextAction2 = if (nextValve2 == valve2) Action.open else Action.move
            var nextTrace1 = if (nextValve1 == valve1) trace1 + "__" + ">" else trace1 + nextValve1.name + ">"
            var nextTrace2 = if (nextValve2 == valve2) trace2 + "__" + ">" else trace2 + nextValve2.name + ">"
            if (nextAction1 == Action.open) {
                open1[valve1.id] = true
                valve1.visited[open1.clone() as BitSet] = Triple(time, rate, tot)
                nextRate += valve1.rate
            }
            if (nextAction2 == Action.open) {
                open2[valve2.id] = true
                valve2.visited2[open2.clone() as BitSet] = Triple(time, rate, tot)
                nextRate += valve2.rate
            }
            var (pot, potp1, potp2) = dfs2(
                duration,
                time + 1,
                nextRate,
                rate + tot,
                nextValve1,
                nextValve2,
                nextAction1,
                nextAction2,
                nextTrace1,
                nextTrace2,
                open1,
                open2
            )
            if (pot > best) {
                best = pot
                path1 = potp1
                path2 = potp2
            }
            if (nextAction1 == Action.open) {
                open1[valve1.id] = false
            }
            if (nextAction2 == Action.open) {
                open2[valve2.id] = false
            }
        }
    }
    return Triple(best, path1, path2)
}
*/


private fun visited(action: Action, valve: Valve, visited: HashMap<BitSet, Triple<Int, Int, Int>>, open: BitSet, time: Int, rate: Int, tot: Int): Boolean {
    if (action == Action.move) {
        if (visited.containsKey(open)) {
            if (better(visited[open]!!, time, rate, tot)) {
                return true
            }
        } else {
            visited[open] = Triple(time, rate, tot)
        }
    }
    return false
}

private fun open(open: String, valve: String): String {
    var so = "$open:$valve"
    return so.split(":").sorted().joinToString(separator = ":") { "$it" }
}

private fun parse(input: List<String>): Map<String, Valve> {
    val valves = HashMap<String, Valve>()
    for (line in input) {
        val chunks = line.replace(";", "").replace(",", "").replace("=", " ").split(" ")
        val valve = Valve(valves.size, chunks[1], chunks[5].toInt())
        valves[chunks[1]] = valve
    }
    for (line in input) {
        val chunks = line.replace(";", "").replace(",", "").replace("=", " ").split(" ")
        val valve = valves[chunks[1]]!!
        for (i in 10 until chunks.size) {
            valves[chunks[i]]?.let { valve.neighbours.add(it) }
        }
        valve.neighbours.sortBy { it.rate }
        valve.neighbours.reverse()
    }
    return valves
}