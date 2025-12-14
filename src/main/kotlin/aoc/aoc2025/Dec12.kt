package aoc.aoc2025

import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    execute1(readTestLines(1)).let { check(it, 2L) ; println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }
}

private fun execute1(input: List<String>): Long {
    val (presents, areas) = parseInput(input)
    var count = 0L
    for (area in areas) {
        val regionSize = area.first.first * area.first.second
        val presentsSize = presentsArea(area.second, presents)
        if (presentsSize > regionSize) continue
        val fit = canFit(area.first, area.second, presents)
        if (fit) count++
    }
    return count
}

fun presentsArea(numbers: List<Int>, presents: HashMap<Int, HashSet<HashMap<Point, Char>>>): Int {
    var total = 0
    for ((id, n) in numbers.withIndex()) {
        val present = presents[id]!!.first()
        for (x in 0 until 3)
            for (y in 0 until 3)
                if (present[Point(x, y)] == '#')
                    total += n
    }
    return total
}

private fun canFit(size: Pair<Int, Int>, numbers: List<Int>, shapes: HashMap<Int, HashSet<HashMap<Point, Char>>>): Boolean {

    val area = HashMap<Point, Char>()
    for (x in 0 until size.first) for (y in 0 until size.second) area[Point(x, y)] = '.'
    val presents = LinkedList<Int>()
    for ((id, n) in numbers.withIndex()) for (i in 0 until n) presents.add(id)
    return canFit(area, size, presents, shapes)
}

private fun canFit(area: HashMap<Point, Char>, size: Pair<Int, Int>, presents: LinkedList<Int>, shapes: HashMap<Int, HashSet<HashMap<Point, Char>>>): Boolean {

    if (presents.isEmpty()) return true
    val present = presents.remove()
    for (x in 0 until size.first) { // for all positions
        for (y in 0 until size.second) {
            for (shape in shapes[present]!!) { // for all rotations and flips
                val pos = Point(x, y)
                if (canFitHere(area, shape, pos)) {
                    putShapeHere(area, shape, pos)
                    if (canFit(area, size, presents, shapes))
                        return true
                    removeShapeHere(area, shape, pos)
                }
            }
        }
    }

    presents.addFirst(present)
    return false
}

private fun canFitHere(area: HashMap<Point, Char>, shape: HashMap<Point, Char>, pos: Point): Boolean {
    for (px in 0..3)
        for (py in 0..3)
            if (shape[Point(px, py)] == '#')
                if (!area.containsKey(Point(px + pos.x, py + pos.y)) || area[Point(px + pos.x, py + pos.y)] == '#')
                    return false
    return true
}

private fun putShapeHere(area: HashMap<Point, Char>, shape: HashMap<Point, Char>, pos: Point) {
    for (px in 0..3)
        for (py in 0..3)
            if (shape[Point(px, py)] == '#')
                area[Point(px + pos.x, py + pos.y)] = '#'
}

private fun removeShapeHere(area: HashMap<Point, Char>, shape: HashMap<Point, Char>, pos: Point) {
    for (px in 0..3)
        for (py in 0..3)
            if (shape[Point(px, py)] == '#')
                area[Point(px + pos.x, py + pos.y)] = '.'
}

private fun parseInput(input: List<String>): Pair<HashMap<Int, HashSet<HashMap<Point, Char>>>, ArrayList<Pair<Pair<Int, Int>, List<Int>>>> {
    val presents = HashMap<Int, HashSet<HashMap<Point, Char>>>()
    val areas = ArrayList<Pair<Pair<Int, Int>, List<Int>>>()
    var present = HashMap<Point, Char>()
    var presentId = -1
    var presentRow = 0
    for (line in input) {
        if (line == "") {
            val rotations = HashSet<HashMap<Point, Char>>()
            rotations.add(present)
            for (i in 1..3) {
                present = rotate(present)
                rotations.add(present)
            }
            present = flip(present)
            rotations.add(present)
            for (i in 1..3) {
                present = rotate(present)
                rotations.add(present)
            }
            presents.put(presentId, rotations)
        } else if (line.first().isDigit() && line[1] == ':') {
            present = HashMap<Point, Char>()
            presentId = line.first().digitToInt()
            presentRow = 0
        } else if (line.first() == '.' || line.first() == '#') {
            present.put(Point(presentRow, 0), line[0])
            present.put(Point(presentRow, 1), line[1])
            present.put(Point(presentRow, 2), line[2])
            presentRow++
        } else if (line.contains("x")) {
            val split1 = line.split(": ")
            val split2 = split1[0].split("x")
            val split3 = split1[1].split(" ")
            val size = split2[0].toInt() to split2[1].toInt()
            val numbers = split3.map { it.toInt() }.toList()
            areas.add(size to numbers)
        }
    }
    return presents to areas
}


fun rotate(present: HashMap<Point, Char>): HashMap<Point, Char> {
    val next = HashMap<Point, Char>()
    for (x in 0 until 3) {
        for (y in 0 until 3) {
            next[Point( 2 - y, x)] = present[Point(x, y)]!!
        }
    }
    return next
}

fun flip(present: HashMap<Point, Char>): HashMap<Point, Char> {
    val next = HashMap<Point, Char>()
    for (x in 0 until 3) {
        for (y in 0 until 3) {
            next[Point( 2 - x, y)] = present[Point(x, y)]!!
        }
    }
    return next
}