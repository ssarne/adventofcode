package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 2028)
    check(execute1(readTestLines(2)), 10092)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(2)), 9021)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    var (map, pos, moves) = parse1(input)
    // printMap(map, pos)

    // for each token on moves, move all goods in front of it (if possible)
    for (m in moves) {
        pos = Pos(pos.p, m)
        val canMove = moveIfPossible(map, pos.move())
        if (canMove) pos = pos.move()
    }

    // count all boxes
    val sum = map.entries.sumOf { if (it.value == 'O') it.key.x + 100 * it.key.y else 0 }
    return sum.toLong()
}

private fun execute2(input: List<String>): Long {

    var (map, pos, moves) = parse2(input)
    // printMap(map, pos)

    for (m in moves) {
        pos = Pos(pos.p, m)

        val canMove = canMoveGoods(map, pos.move())
        if (canMove) {
            doMoveGoods(map, pos.move())
            pos = pos.move()
        }
    }

    // count all boxes
    var sum = 0
    for (e in map)
        if (e.value == '[')
            sum += e.key.x + 100 * e.key.y
    return sum.toLong()
}

private fun parse1(input: List<String>): Triple<MutableMap<Point, Char>, Pos, String> {

    val map = mutableMapOf<Point, Char>()
    var pos = Pos(Point(-1, -1), '-')
    var moves = ""

    for ((y, line) in input.withIndex()) {
        if (line == "") continue
        if (line.startsWith('#')) {
            for ((x, c) in line.withIndex()) {
                when (c) {
                    '@' -> {
                        map[Point(x, y)] = '.'
                        pos = Pos(Point(x, y), '-')
                    }

                    '#' -> map[Point(x, y)] = c
                    'O' -> map[Point(x, y)] = c
                    '.' -> map[Point(x, y)] = c
                }
            }
        } else {
            moves += line
        }
    }
    return Triple(map, pos, moves)
}

private fun parse2(input: List<String>): Triple<MutableMap<Point, Char>, Pos, String> {
    val map = mutableMapOf<Point, Char>()
    var pos = Pos(Point(-1, -1), '-')
    var moves = ""

    for ((y, line) in input.withIndex()) {
        if (line == "") continue
        if (line.startsWith('#')) {
            for ((x, c) in line.withIndex()) {
                when (c) {
                    '@' -> {
                        pos = Pos(Point(2 * x, y), '-')
                        map[Point(2 * x, y)] = '.'
                        map[Point(2 * x + 1, y)] = '.'
                    }

                    'O' -> {
                        map[Point(2 * x, y)] = '['
                        map[Point(2 * x + 1, y)] = ']'
                    }

                    '#' -> {
                        map[Point(2 * x, y)] = '#'
                        map[Point(2 * x + 1, y)] = '#'
                    }

                    '.' -> {
                        map[Point(2 * x, y)] = '.'
                        map[Point(2 * x + 1, y)] = '.'
                    }
                }
            }
        } else {
            moves += line

        }
    }
    return Triple(map, pos, moves)
}

private fun printMap(map: MutableMap<Point, Char>, pos: Pos) {
    val c = map[pos.p]!!
    map[pos.p] = '@'
    printSparseMatrix(map)
    map[pos.p] = c
}

fun moveIfPossible(map: MutableMap<Point, Char>, pos: Pos): Boolean {
    if (map[pos.p] == '#') return false
    if (map[pos.p] == '.') return true
    if (map[pos.p] == 'O') {
        val canMove = moveIfPossible(map, pos.move())
        if (canMove) {
            map[pos.move().p] = 'O'
            map[pos.p] = '.'
        }
        return canMove
    }
    throw RuntimeException("CMH: $pos ${map[pos.p]}")
}

fun canMoveGoods(map: MutableMap<Point, Char>, pos: Pos): Boolean {

    if (map[pos.p] == null)
        throw RuntimeException("CMH: $pos ${map[pos.p]}")

    if (map[pos.p] == '#') return false
    if (map[pos.p] == '.') return true
    if (map[pos.p]!! in "[]" && pos.dir in "^v") {
        val side = getSidePos(map[pos.p]!!, pos)
        return canMoveGoods(map, pos.move()) && canMoveGoods(map, side.move())
    }
    if (map[pos.p]!! in "[]" && pos.dir in "<>") {
        return canMoveGoods(map, pos.move())
    }

    throw RuntimeException("CMH: $pos ${map[pos.p]}")
}

private fun getSidePos(c: Char, pos: Pos): Pos {
    return if (c == '[') Pos(Point(pos.p.x + 1, pos.p.y), pos.dir)
    else Pos(Point(pos.p.x - 1, pos.p.y), pos.dir)
}

fun doMoveGoods(map: MutableMap<Point, Char>, pos: Pos): Boolean {
    if (map[pos.p] == '#') return false
    if (map[pos.p] == '.') return true
    if (map[pos.p]!! in "[]" && pos.dir in "^v") {
        val side = getSidePos(map[pos.p]!!, pos)
        doMoveGoods(map, pos.move())
        doMoveGoods(map, side.move())
        map[pos.move().p] = map[pos.p]!!
        map[pos.p] = '.'
        map[side.move().p] = map[side.p]!!
        map[side.p] = '.'
        return true
    }
    if (map[pos.p]!! in "[]" && pos.dir in "<>") {
        doMoveGoods(map, pos.move())
        map[pos.move().p] = map[pos.p]!!
        map[pos.p] = '.'
        return true
    }

    throw RuntimeException("CMH: $pos ${map[pos.p]}")
}
