package aoc.aoc2022.dec23


import aoc.ktutils.*
import kotlin.math.exp

fun main() {
    check(execute(readTestLines(20), 10, 20).first, 25)
    check(execute(readTestLines(0), 10, 0).first, 110)
    execute(readLines(), 10).let { println(it.first); check(it.first, readAnswerAsInt(1)) }

    check(execute(readTestLines(), 10000, 0).second, 20)
    execute(readLines(), 10000).let { println(it.second); check(it.second, readAnswerAsInt(2)) }
}

data class Elf(var id: String, var pos: Point, var next: Point? = null)

private fun execute(input: List<String>, iterations: Int, validate: Int? = null): Pair<Int, Int> {

    var strat = "NSWE"
    var map = parse(input)
    // printMap(map, "Start")

    for (i in 1..iterations) {
        var attempts = HashMap<Point, Int>()
        for (elf in map.values) {

            if (alone(map, elf)) continue

            for (d in strat) {
                if (elf.next != null) break
                when (d) {
                    'N' -> Point(elf.pos.x, elf.pos.y - 1).let {
                        if (empty(map, it.x - 1..it.x + 1, it.y..it.y)) {
                            attempts[it] = attempts.getOrDefault(it, 0) + 1
                            elf.next = it
                        }
                    }
                    'S' -> Point(elf.pos.x, elf.pos.y + 1).let {
                        if (empty(map, it.x - 1..it.x + 1, it.y..it.y)) {
                            attempts[it] = attempts.getOrDefault(it, 0) + 1
                            elf.next = it
                        }
                    }
                    'W' -> Point(elf.pos.x - 1, elf.pos.y).let {
                        if (empty(map, it.x..it.x , it.y - 1..it.y + 1)) {
                            attempts[it] = attempts.getOrDefault(it, 0) + 1
                            elf.next = it
                        }
                    }
                    'E' -> Point(elf.pos.x + 1, elf.pos.y).let {
                        if (empty(map, it.x..it.x , it.y - 1..it.y + 1)) {
                            attempts[it] = attempts.getOrDefault(it, 0) + 1
                            elf.next = it
                        }
                    }
                }
            }
        }

        if (attempts.size == 0) // steady state reached, no planned moves by the elfs
            return Pair(freeSpaces(map), i)

        var next = HashMap<Point, Elf>()
        for (elf in map.values) {
            if (elf.next != null && attempts[elf.next] == 1) {
                next[elf.next!!] = elf
                elf.pos = elf.next!!
                elf.next = null
            } else {
                next[elf.pos] = elf
                elf.pos = elf.pos
                elf.next = null
            }
        }

        map = next
        strat = strat.substring(1) + strat[0]
        //printMap(map, "Iteration $i", true)

        if (validate != null && hasTestFile(validate + i)) {
            var expected = parse(readTestLines(validate + i))
            for (e in map.keys) check(expected.containsKey(e), true, map[e].toString())
        }
    }

    return Pair(freeSpaces(map), iterations)
}

private fun freeSpaces(map: HashMap<Point, Elf>): Int {
    val minX = map.keys.map { it.x }.min()!!
    val maxX = map.keys.map { it.x }.max()!!
    val minY = map.keys.map { it.y }.min()!!
    val maxY = map.keys.map { it.y }.max()!!
    return (maxX - minX + 1) * (maxY - minY + 1) - map.size
}

fun alone(map: HashMap<Point, Elf>, elf: Elf): Boolean {
    for (p in elf.pos.surrounding())
        if (map.containsKey(p))
            return false
    return true
}

fun empty(map: HashMap<Point, Elf>, dx: IntRange, dy: IntRange): Boolean {
    for (y in dy) {
        for (x in dx) {
            if (map[Point(x, y)] != null)
                return false
        }
    }
    return true
}

fun parse(input: List<String>): HashMap<Point, Elf> {
    var grid = HashMap<Point, Elf>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            if (input[y][x] == '#')
                grid[Point(x, y)] = Elf("$x,$y", Point(x, y))
        }
    }
    return grid
}
