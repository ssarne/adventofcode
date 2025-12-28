package aoc.aoc2016

import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, 11L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): Long {
    val (locations, doubles) = parseInput(input)
    // prettyPrint(locations, "Start")
    val estate = collect(Estate(locations, 0, 1, "" to "", null), doubles)
    // prettyPrintPath(estate)
    return estate.turn.toLong()
}

private fun execute2(input: List<String>): Long {
    val (locations, doubles) = parseInput(input)
    locations.put("eg1", 1)
    locations.put("em1", 1)
    locations.put("dg1", 1)
    locations.put("dm1", 1)
    // prettyPrint(locations, "Start")
    val estate = collect(Estate(locations, 0, 1, "" to "", null), doubles)
    // prettyPrintPath(estate)
    return estate.turn.toLong()
}

private data class Estate(
    val locations: HashMap<String, Int>,
    val turn: Int,
    val elev: Int,
    val trans: Pair<String, String>,
    val prev: Estate?
) {
    init {
        locations["[ ]"] = elev
    }

    fun done() = locations.values.sum() == locations.size * 4

    fun empty(f: Int) = locations.keys.count { locations[it] == f } == 0

    fun hasGenerator(f: Int): Boolean {
        return locations.keys.count { it[1] == 'g' && locations[it] == f } != 0
    }

    fun hasMicrochipsGenerator(f: Int, m: String): Boolean {
        val prefix = getGeneratorPrefix(m)
        for (e in locations)
            if (e.key.startsWith(prefix))
                if (e.value == f)
                    return true
        return false
    }

    fun hasGeneratorsMicrochip(f: Int, m: String): Boolean {
        val prefix = getMicrochipPrefix(m)
        for (e in locations)
            if (e.key.startsWith(prefix))
                if (e.value == f)
                    return true
        return false
    }

    fun hasMicrochip(f: Int): Boolean {
        return locations.keys.count { it[1] == 'm' && locations[it] == f } != 0
    }

    fun valid(): Boolean {

        for (e in locations) {
            if (isElevator(e.key)) continue
            if (isGenerator(e.key)) continue
            if (isMicrochip(e.key)) {
                if (hasMicrochipsGenerator(e.value, e.key)) continue
                if (!hasGenerator(e.value)) continue
            }
            return false
        }
        return true
    }

    fun summarize(doubles: HashSet<Char>): String {
        val fs = IntArray(7 * 4)  // elevator, single microchips, single generators,
        for (e in locations) {

            var offset = (e.value - 1) * 7 // offset for floor in summary array
            if (doubles.contains(e.key.first())) offset += 3

            if (isElevator(e.key)) {
                fs[offset + 0]++
            } else if (isMicrochip(e.key)) {
                if (hasMicrochipsGenerator(e.value, e.key)) {
                    fs[offset + 3]++
                } else {
                    fs[offset + 1]++
                }
            } else { // is generator
                if (!hasGeneratorsMicrochip(e.value, e.key)) {
                    fs[offset + 2]++
                } // else pair, already counted
            }
        }
        return fs.joinToString(";")
    }
}

fun isGenerator(e: String) = e[1] == 'g'
fun isMicrochip(e: String) = e[1] == 'm'
fun isElevator(e: String) = e == "[ ]"
fun getGeneratorPrefix(e: String) = "" + e.first() + 'g'
fun getMicrochipPrefix(e: String) = "" + e.first() + 'm'

private fun collect(start: Estate, doubles: HashSet<Char>): Estate {

    val steps = LinkedList<Estate>()
    val visited = HashSet<String>()

    steps.add(start)
    visited.add(start.summarize(doubles))

    // BFS - sending elevator up and down, with 1 or 2 items in it
    while (!steps.isEmpty()) {
        val c = steps.removeAt(0)
        if (c.done()) return c // all items are on floor 4

        for (d in -1..1 step 2) { // send elevator up and down

            if (c.elev + d > 4) continue
            if (c.elev + d < 1) continue
            if (d == -1 && c.elev == 1 && c.empty(0)) continue
            if (d == -1 && c.elev == 2 && c.empty(0) && c.empty(1)) continue

            for (e1 in c.locations.filter { it.value == c.elev }) {

                if (isElevator(e1.key)) continue

                val next1 = Estate(HashMap(c.locations), c.turn + 1, c.elev + d, e1.key to "", c)
                next1.locations[e1.key] = e1.value + d
                val valid1 = next1.valid()
                val summary1 = next1.summarize(doubles)
                val visited1 = visited.contains(summary1)
                if (valid1 && !visited1) {
                    steps.add(next1)
                    visited.add(summary1)
                }

                for (e2 in next1.locations.filter { it.value == c.elev }) {
                    // elevator check
                    if (isElevator(e2.key)) continue
                    if (isGenerator(e1.key) && isGenerator(e2.key)
                        || isMicrochip(e1.key) && isMicrochip(e2.key)
                        || e1.key.first() == e2.key.first()
                    ) {
                        val next2 = Estate(HashMap(next1.locations), c.turn + 1, c.elev + d, e1.key to e2.key, c)
                        next2.locations[e2.key] = e2.value + d
                        val valid2 = next2.valid()
                        val summary2 = next2.summarize(doubles)
                        val visited2 = visited.contains(summary2)
                        if (valid2 && !visited2) {
                            steps.add(next2)
                            visited.add(summary2)
                        }
                    }
                }
            }
        }
    }

    return start
}

private fun parseInput(input: List<String>): Pair<HashMap<String, Int>, HashSet<Char>> {
    val floors = mapOf("first" to 1, "second" to 2, "third" to 3, "fourth" to 4)
    val locations = HashMap<String, Int>() // id -> floor
    for (line in input) {
        val tokens = line.replace(",", "").replace(".", "").split(" ")
        val floor = floors[tokens[1]]!!
        for (i in tokens.indices) {
            var post = ""
            if (tokens[i].equals("microchip")) post = "m"
            if (tokens[i].equals("generator")) post = "g"
            if (post == "") continue
            val item = "" + tokens[i - 1].first() + post
            for (j in 1..5) {
                if (locations.contains(item + j)) continue
                locations[item + j] = floor
                break
            }
        }
    }
    // analyze if there are substances with duplicates
    val substances = HashSet<Char>()
    val doubles = HashSet<Char>()
    for (k in locations.keys) {
        if (isMicrochip(k)) {
            if (substances.contains(k.first()))
                doubles.add(k.first())
            else
                substances.add(k.first())
        }
    }

    // Putting elevator in locations, for visited check to work
    locations["[ ]"] = 1
    return locations to doubles
}

private fun prettyPrintPath(state: Estate) {
    if (state.prev != null)
        prettyPrintPath(state.prev)
    prettyPrintState(state)
}

private fun prettyPrintState(state: Estate) {
    prettyPrint(state.locations, "Path turn=" + state.turn + "  elev=" + state.elev + " " + state.trans)
}

private fun prettyPrint(locations: HashMap<String, Int>, label: String) {
    val entities = locations.keys.toMutableList().sorted().toMutableList()
    println("$label:")
    for (i in 1..4) {
        val f = 5 - i
        print("F$f  ")
        for (e in entities)
            if (locations[e] == f)
                print(" $e")
            else
                print(" ...")
        println()
    }
    println()
}
