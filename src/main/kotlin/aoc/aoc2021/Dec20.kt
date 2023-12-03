package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute(readTestLines()), 35)
    execute(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute(readTestLines(), iter = 50), 3351)
    execute(readLines(), iter = 50).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute(input: List<String>, print: Boolean = false, iter: Int = 2): Int {
    val palette = input.first()
    var grid = createImage(input.subList(2, input.size))
    for (i in 1..iter) {
        if (print) printImage(grid, true)
        grid = enhance(grid, palette, if (i % 2 == 0) palette.first() else '.')
    }

    return grid.values.filter { c -> c == '#' }.count()
}


fun enhance(grid: HashMap<Point, Char>, palette: String, inf: Char): HashMap<Point, Char> {
    val next = HashMap<Point, Char>()
    for (p in grid.keys) {
        for (x in p.x - 2..p.x + 2) {
            for (y in p.y - 2..p.y + 2) {
                val bin: String = read(grid, x, y, inf)
                val i = Integer.parseInt(bin, 2)
                next[Point(x, y)] = palette[i]
            }
        }
    }
    return next
}

fun read(grid: HashMap<Point, Char>, px: Int, py: Int, inf: Char): String {
    var pixel = ""
    for (y in py - 1..py + 1) {
        for (x in px - 1..px + 1) {
            val c = grid.getOrDefault(Point(x, y), inf)
            pixel += if (c == '#') '1' else '0'
        }
    }
    return pixel
}

fun createImage(input: List<String>): HashMap<Point, Char> {
    if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
    val grid = HashMap<Point, Char>()
    for (y in input.indices) {
        for (x in 0 until input[y].length) {
            grid[Point(x, y)] = input[y][x]
        }
    }
    return grid
}

fun printImage(dots: Map<Point, Char>, header: Boolean = false) {
    val minX = dots.keys.stream().mapToInt { it.x }.min().orElse(0)
    val minY = dots.keys.stream().mapToInt { it.y }.min().orElse(0)
    val maxX = dots.keys.stream().mapToInt { it.x }.max().orElse(1)
    val maxY = dots.keys.stream().mapToInt { it.y }.max().orElse(1)
    if (header) for (x in minX..maxX) print("-"); println("")
    for (y in minY..maxY) {
        for (x in minX..maxX) {
            if (dots.contains(Point(x, y))) {
                print(dots[Point(x, y)])
            } else {
                print(' ')
            }
        }
        println()
    }
}


