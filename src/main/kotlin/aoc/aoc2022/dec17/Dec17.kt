package aoc.aoc2022.dec17

import aoc.ktutils.*
import java.util.*
import kotlin.math.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

fun main() {
    check(execute(readTestLines(), 5), 9)
    check(execute(readTestLines(), 10), 17)
    check(execute(readTestLines(), 2022), 3068)
    execute(readLines(), 2022).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute(readTestLines(), 1000000000000L), 1514285714288L)
    execute(readLines(), 1000000000000L).let { println(it); check(it, readAnswerAsLong(2)) }
}

// wind index, rock index, heights -> iteration, top
private data class MemKey(val jet: Int, val rock: Int, val heights: ArrayList<Int>)
private data class MemValue(val iteration: Long, val top: Long)

private fun execute(input: List<String>, iterations: Long): Long {

    val (rocks, jets) = parse(input)
    var cave = BitSet()
    val debug = false

    val memory = HashMap<MemKey, MemValue>() // Memoization

    var iter = 0L    // number of rocks dropped
    var rocki = -1   // index in the rocks list
    var jeti = -1    // index in the jet stream
    var top = 0      // height of top line for the rocks
    var offset = 0L  // offset of cave, and actual top
    val width = 7    // width of cave
    val entryX = 2   // x coordinate for rocks to enter

    for (x in 0 until width) cave[0 * 7 + x] = true

    while (iter < iterations) {

        if (debug) {
            println("=== $top ===")
            printBitSetMatrixReversed(cave, width)
            println(heights(cave, width))
            println("=== === ===")
        }

        iter++
        rocki = (rocki + 1) % rocks.size

        val rock = rocks[rocki]
        var pos = Point(entryX, top + 3 + 1)
        var falling = true

        while (falling) {

            if (debug) {
                println("=== $top ===")
                addRock(cave, width, rock, pos)
                printBitSetMatrixReversed(cave, width)
                removeRock(cave, width, rock, pos)
                println("=== === ===")
            }

            // blow in wind
            jeti = (jeti + 1) % jets.length
            when (jets[jeti]) {
                '<' -> if (pos.x > 0)
                    Point(pos.x - 1, pos.y).let {
                        if (space(cave, width, rock, it))
                            pos = it
                    }

                '>' -> if (pos.x + rock[0].length < width)
                    Point(pos.x + 1, pos.y).let {
                        if (space(cave, width, rock, it))
                            pos = it
                    }
            }

            // check if down - break
            Point(pos.x, pos.y - 1).let {
                if (space(cave, width, rock, it)) {
                    pos = it
                } else {
                    addRock(cave, width, rock, pos)
                    top = max(pos.y + rock.size - 1, top)
                    falling = false
                }
            }
        }

        // Memoize
        val heights = heights(cave, width)
        val key = MemKey(jeti, rocki, heights)
        if (memory.containsKey(key)) { // found match, skip ahead as far as possible
            val value = memory[key]!!
            val dCount = iter - value.iteration
            val dTop = offset + top.toLong() - value.top
            if (debug) println("Match for $key :: ${value.iteration}->$iter dCount=$dCount dTop=$dTop")
            val jumps = (iterations - iter) / dCount
            iter += jumps * dCount
            offset += jumps * dTop
        } else {
            memory[key] = MemValue(iter, offset + top)
        }
    }
    return offset + top
}

private fun heights(cave: BitSet, width: Int): ArrayList<Int> {

    val heights = ArrayList<Int>(width)
    for (x in 0 until width) heights.add(0)
    val above = (cave.size() / width)
    var low = above
    for (x in 0 until width) {
        for (i in 0..above) {
            val h = above - i
            val b = h * width + x
            if (cave[b]) {
                heights[x] = h
                low = min(h, low)
                break
            }
        }
    }

    // Normalize to lowest top
    for (i in 0 until width) {
        heights[i] = heights[i] - low
    }

    return heights
}

fun addRock(cave: BitSet, width: Int, rock: ArrayList<String>, pos: Point) {
    for (y in rock.indices) {
        for (x in rock[y].indices) {
            if (rock[y][x] == '#') {
                val xx = pos.x + x
                val yy = pos.y + y
                cave[yy * width + xx] = true
            }
        }
    }
}

fun removeRock(cave: BitSet, width: Int, rock: ArrayList<String>, pos: Point) {
    for (y in rock.indices)
        for (x in rock[y].indices)
            if (rock[y][x] == '#')
                cave[(pos.y + y) * width + (pos.x + x)] = false
}

fun space(cave: BitSet, width: Int, rock: ArrayList<String>, pos: Point): Boolean {
    if (pos.y < 0) return false
    for (y in rock.indices)
        for (x in rock[y].indices)
            if (rock[y][x] == '#')
                if (cave[(pos.y + y) * width + (pos.x + x)])
                    return false
    return true
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