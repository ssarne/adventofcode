package aoc.aoc2017


import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.lang.RuntimeException
import java.util.*

fun main() {
    check(execute1(readTestLines()).first, "ABCDEF")
    println(execute1(readLines()).first) // MKXOIHZNBL

    check(execute1(readTestLines()).second, 38)
    println(execute1(readLines()).second) // 17872
}

private fun execute1(input: List<String>): Pair<String,Int> {
    var maze = parseMaze(input)    // Read maze
    // printMaze(maze)
    var letters = ""               // The result of the path
    var pos = getStart(maze)       // Find starting point
    var dir = Point.DOWN           // String at the top going down
    var steps = 0                  // Record the number of steps

    while (maze.containsKey(pos)) {
        when (maze[pos]) {         // Walk the path  (position, direction)
            '|','-' -> pos += dir  //     If straight, continue
            in 'A'..'Z' -> {       //     If letter, store it, continue
                letters += maze[pos]
                pos += dir
            }
            '+' -> {               //    If cross, continue and find new direction
                dir = if (maze.containsKey(pos + dir.rotLeft())) dir.rotLeft()
                else if (maze.containsKey(pos + dir.rotRight())) dir.rotRight()
                else throw RuntimeException("WAT " + pos + " " + maze[pos])
                pos += dir
            }
            else -> throw RuntimeException("WAT " + maze[pos])
        }
        steps++                    // Count steps
    }

    return Pair(letters,steps)     // return the letters from walking the path and the steps it took
}

private fun getStart(maze: Map<Point, Char>): Point {
    return maze.keys.stream()       // There is only one char on the top line, the entry point
        .filter { it.y == 0 }
        .findFirst().get()
}

private fun parseMaze(input: List<String>): Map<Point, Char> {
    var maze = HashMap<Point, Char>()
    for (y in input.indices) {
        val line = input[y]
        for (x in line.indices) {
            if (line[x] != ' ') maze[Point(x, y)] = line[x]
        }
    }
    return maze
}

private fun printMaze(maze: Map<Point, Char>) {

    var maxX = maze.keys.stream().mapToInt{ it.x }.max().orElse(0)
    var maxY = maze.keys.stream().mapToInt{ it.y }.max().orElse(0)

    for (y in 0 .. maxY) {
        for (x in 0 .. maxX) {
            print(maze[Point(x, y)] ?: ' ')
        }
        println()
    }
}

private data class Point(val x: Int, val y: Int) {
    companion object {
        val RIGHT = Point(1, 0)
        val UP = Point(0, -1)
        val LEFT = Point(-1, 0)
        val DOWN = Point(0, 1)
    }

    operator fun plus(that: Point): Point {
        return Point(this.x + that.x, this.y + that.y)
    }

    fun rotLeft(): Point {
        when (this) {
            RIGHT -> return UP
            UP -> return LEFT
            LEFT -> return DOWN
            DOWN -> return RIGHT
        }
        throw RuntimeException("WAT")
    }

    fun rotRight(): Point {
        when (this) {
            RIGHT -> return DOWN
            UP -> return RIGHT
            LEFT -> return UP
            DOWN -> return LEFT
        }
        throw RuntimeException("WAT")
    }
}
