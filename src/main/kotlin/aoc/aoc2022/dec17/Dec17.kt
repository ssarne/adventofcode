package aoc.aoc2022.dec17

import aoc.ktutils.*
import kotlin.math.*
import kotlin.RuntimeException

fun main() {
    check(execute1(readTestLines(), 5), 9)
    check(execute1(readTestLines(), 10), 17)
    check(execute1(readTestLines(), 2022), 3068)
    execute1(readLines(), 2022).let { println(it) } // ; check(it, readAnswerAsInt(1)) }

    // check(execute2(readTestLines()), -1)
    // execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>, iterations: Int): Int {
    val (rocks, winds) = parse(input)
    val cave = HashMap<Point, Char>()

    var floor = 0
    val width = 7
    val entryX = 3
    for (x in 1..width) cave[Point(x, floor)] = '-'
    var top = floor
    var windIterator = 0

    for (r in 0 until iterations) {
        val rock = rocks[r % rocks.size]
        var pos = Point(entryX, top + 3 + 1)

        while (true) {
            //addRock(cave, rock, pos, '@')
            //printSparseMatrixReversed(cave)
            //removeRock(cave, rock, pos)

            // blow in wind
            val d = winds[windIterator++ % winds.length]
            if (d == '<' && pos.x > 1 && space(cave, rock, Point(pos.x - 1, pos.y)))
                pos = Point(pos.x - 1, pos.y)
            else if (d == '>' && pos.x + rock[0].length <= width && space(cave, rock, Point(pos.x + 1, pos.y)))
                pos = Point(pos.x + 1, pos.y)

            // check if down - break
            if (space(cave, rock, Point(pos.x, pos.y - 1))) {
                pos = Point(pos.x, pos.y - 1)
            } else {
                addRock(cave, rock, pos, '#')
                top = max(pos.y + rock.size - 1, top)
                break
            }
        }
        //print("=== $top ===")
        //printSparseMatrixReversed(cave)
    }
    return top
}

fun addRock(cave: HashMap<Point, Char>, rock: ArrayList<String>, pos: Point, c: Char) {
    for (y in rock.indices)
        for (x in rock[y].indices)
            if (rock[y][x] == '#')
                cave[Point(pos.x + x, pos.y + y)] = c
}

fun removeRock(cave: HashMap<Point, Char>, rock: ArrayList<String>, pos: Point) {
    for (y in rock.indices)
        for (x in rock[y].indices)
            if (rock[y][x] == '#')
                cave.remove(Point(pos.x + x, pos.y + y))
}

fun space(cave: HashMap<Point, Char>, rock: ArrayList<String>, pos: Point): Boolean {
    for (y in rock.indices)
        for (x in rock[y].indices)
            if (rock[y][x] == '#')
                if (cave.containsKey(Point(pos.x + x, pos.y + y)))
                    return false
    return true

}

private fun execute2(input: List<String>): Int {
    return 0
}

private fun parse(input: List<String>): Pair<ArrayList<ArrayList<String>>, String> {
    val rocks = ArrayList<ArrayList<String>>()
    val rock1 = ArrayList<String>()
    rock1.add("####")
    val rock2 = ArrayList<String>()
    rock2.add(".#.")
    rock2.add("###")
    rock2.add(".#.")
    val rock3 = ArrayList<String>() // reversed since 0 is bottom
    rock3.add("###")
    rock3.add("..#")
    rock3.add("..#")
    val rock4 = ArrayList<String>()
    rock4.add("#")
    rock4.add("#")
    rock4.add("#")
    rock4.add("#")
    val rock5 = ArrayList<String>()
    rock5.add("##")
    rock5.add("##")
    rocks.add(rock1)
    rocks.add(rock2)
    rocks.add(rock3)
    rocks.add(rock4)
    rocks.add(rock5)
    return Pair(rocks, input[0])
}