package aoc.aoc2021

import aoc.ktutils.*
import java.util.*

fun main() {
    check(execute1(readTestLines()), 15)
    execute1(readLines()).let { println(it); check(it, 475) }
    check(execute2(readTestLines()), 1134)
    execute2(readLines()).let { println(it); check(it, 1092012) }
}

private fun execute1(input: List<String>): Int {
    var sum = 0
    for (i in input.indices) {
        for (j in input[i].indices) {
            val c = input[i][j]
            if (isLower(input, c, j - 1, i) &&
                isLower(input, c, j + 1, i) &&
                isLower(input, c, j, i - 1) &&
                isLower(input, c, j, i + 1)
            ) {
                sum += ("" + c).toInt() + 1
            }
        }
    }
    return sum
}

fun isLower(input: List<String>, c: Char, x: Int, y: Int): Boolean {
    if (y < 0 || y >= input.size) return true
    if (x < 0 || x >= input[y].length) return true
    return c.toInt() < input[y][x].toInt()
}

private fun execute2(input: List<String>): Int {
    val basins = LinkedList<Int>()
    val width = input[0].length
    val marked = Array(input.size) { BooleanArray(width) }
    for (y in input.indices) {
        for (x in input[y].indices) {
            if (isBasin(input, x, y, marked)) {
                val size = basinSize(x, y, input, marked)
                basins.add(size)
            }
        }
    }
    basins.sort()
    return basins[basins.size - 3] * basins[basins.size - 2] * basins[basins.size - 1]
}

private fun basinSize(
    x: Int,
    y: Int,
    input: List<String>,
    marked: Array<BooleanArray>
): Int {
    var size = 0
    val queue = LinkedList<Point>()
    queue.add(Point(x, y))
    while (queue.size > 0) {
        val p = queue.removeFirst()
        if (isBasin(input, p.x, p.y, marked)) {
            size++
            queue.add(Point(p.x, p.y - 1))
            queue.add(Point(p.x, p.y + 1))
            queue.add(Point(p.x - 1, p.y))
            queue.add(Point(p.x + 1, p.y))
            marked[p.y][p.x] = true
        }
    }
    return size
}

fun isBasin(input: List<String>, x: Int, y: Int, marked: Array<BooleanArray>): Boolean {
    if (y < 0 || y >= input.size) return false
    if (x < 0 || x >= input[y].length) return false
    val c2 = input[y][x]
    if (c2 == '9') return false
    if (marked[y][x]) return false
    return true
}
