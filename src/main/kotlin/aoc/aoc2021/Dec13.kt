package aoc.aoc2021

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 17)
    execute1(readLines()).let { println(it); check(it, 631) }

    check(execute2(readTestLines()).size, 16)
    execute2(readLines()).let { printSparseSet(it); check(it.size, 92) }
}

private data class Fold(val axis: String, val pos: Int)

private fun execute1(input: List<String>): Int {

    var (dots, folds) = parseInput(input)
    dots = fold(dots, folds.first())
    return dots.size
}

private fun execute2(input: List<String>): Set<Point> {

    var (dots, folds) = parseInput(input)
    for (fold in folds) dots = fold(dots, fold)
    return dots
}


private fun fold(dots: Set<Point>, fold: Fold): Set<Point> {
    val next = HashSet<Point>()
    for (dot in dots) {
        when (fold.axis) {
            "x" -> next.add(if (dot.x > fold.pos) Point(fold.pos - (dot.x - fold.pos), dot.y) else dot)
            "y" -> next.add(if (dot.y > fold.pos) Point(dot.x, fold.pos - (dot.y - fold.pos)) else dot)
        }
    }
    return next
}

private fun parseInput(input: List<String>): Pair<Set<Point>, List<Fold>> {
    val dots = HashSet<Point>()
    val folds = ArrayList<Fold>()
    for (line in input) {
        if (line.isEmpty()) continue
        if (line.startsWith("fold along ")) {
            val fold = line.split(" ")[2].split("=")
            folds.add(Fold(fold[0], fold[1].toInt()))
        } else {
            dots.add(Point.create(line))
        }
    }
    return Pair(dots, folds)
}