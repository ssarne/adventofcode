package aoc.aoc2022.dec19


import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException
import java.util.BitSet

fun main() {
    check(execute1(readTestLines()), 33)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 56 * 62)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}


private fun execute1(input: List<String>): Int {

    var res = 0
    val blueprints = parseBlueprints(input)
    for ((id, blueprint) in blueprints) {
        val maxRobots = getMaxCostsPerRobotType(100, blueprint)
        val r = countGeodes(blueprint, 24, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0), maxRobots, HashMap())
        res += id * r
        // println("Blueprint $id: $r ($res)")
    }
    return res
}

private fun execute2(input: List<String>): Int {
    var res = 1
    val blueprints = parseBlueprints(input)
    for ((id, blueprint) in blueprints) {
        println("Blueprint $id")
        if (id > 3) continue
        val maxRobots = getMaxCostsPerRobotType(100, blueprint)
        val r = countGeodes(blueprint, 32, intArrayOf(1, 0, 0, 0), intArrayOf(0, 0, 0, 0), maxRobots, HashMap())
        res *= r
        println("Blueprint $id: $r ($res)")
    }
    return res
}


private fun getMaxCostsPerRobotType(inf: Int, blueprint: java.util.HashMap<String, IntArray>): IntArray {
    val maxRobots = intArrayOf(0, 0, 0, inf)
    for ((_, costs) in blueprint) {
        for (i in intArrayOf(ORE, CLAY, OBSIDIAN))
            maxRobots[i] = max(maxRobots[i], costs[i])
    }
    return maxRobots
}

private enum class Resources { ore, clay, obsidian, geode }

private val ORE = Resources.ore.ordinal
private val CLAY = Resources.clay.ordinal
private val OBSIDIAN = Resources.obsidian.ordinal
private val GEODE = Resources.geode.ordinal

private fun countGeodes(
    blueprint: HashMap<String, IntArray>,
    time: Int,
    robots: IntArray,
    resources: IntArray,
    maxRobots: IntArray,
    memory: HashMap<String, Int>
): Int {

    if (time == 0) return resources[GEODE]
    if (time == 1) return resources[GEODE] + robots[GEODE]

    val key =
        "$time - ${robots[ORE]}:${robots[CLAY]}:${robots[OBSIDIAN]}:${robots[GEODE]} - ${resources[ORE]}:${resources[CLAY]}:${resources[OBSIDIAN]}"
    memory[key].let {
        if (it != null)
            return it
        else
            memory[key] = resources[GEODE]
    }

    // println("[$time] resources=${resources.toList()} robots=${robots.toList()}")

    // choose robot to build
    var best = 0
    var greed = false
    for ((robot, costs) in blueprint) {
        if (costs[ORE] <= resources[ORE] && costs[CLAY] <= resources[CLAY] && costs[OBSIDIAN] <= resources[OBSIDIAN]) {

            val robi = Resources.valueOf(robot).ordinal

            // constraint, no need to get more robots
            if (robots[robi] >= maxRobots[robi]) continue

            // Always build geode robots when possible, no need to delay
            if (robi == GEODE) greed = true

            for (r in robots.indices) resources[r] += robots[r]  // collect resources
            for (i in costs.indices) resources[i] -= costs[i]    // pay for robot
            robots[robi]++ // get robot

            var pot = countGeodes(blueprint, time - 1, robots, resources, maxRobots, memory)
            if (pot > best) best = pot

            robots[robi]-- // reset robot
            for (i in costs.indices) resources[i] += costs[i]    // reset payments
            for (r in robots.indices) resources[r] -= robots[r]  // reset resources
        }
    }

    // skip building robot
    if (!greed) {
        for (r in robots.indices) resources[r] += robots[r]  // collect resources
        var pot = countGeodes(blueprint, time - 1, robots, resources, maxRobots, memory)
        if (pot > best) best = pot
        for (r in robots.indices) resources[r] -= robots[r]  // reset resources
    }
    return best
}

private fun parseBlueprints(input: List<String>): HashMap<Int, HashMap<String, IntArray>> {
    val blueprints = HashMap<Int, HashMap<String, IntArray>>()
    for (line in input) {
        val sections = line.split(": ")
        val id = sections[0].split(" ")[1].replace(":", "").trim().toInt()
        val robots = HashMap<String, IntArray>()
        for (section in sections[1].trim().split(".")) {
            val tokens = section.trim().split(" ")
            if (tokens.size == 1) continue
            val resource = tokens[1]
            val costs = IntArray(4)
            for (i in 4..tokens.size step 3) {
                costs[Resources.valueOf(tokens[i + 1]).ordinal] = tokens[i].toInt()
            }
            robots.put(resource, costs)
        }
        blueprints.put(id, robots)
    }
    return blueprints
}
