package aoc.aoc2023

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 136)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 64)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    val board = parseCharacterGridToMap(input)
    val size = mapSize(board)
    tiltNorth(size, board)
    return weightNorth(size, board)
}

private fun execute2(input: List<String>, end: Int = 1000000000): Int {

    val board = parseCharacterGridToMap(input)
    val size = mapSize(board)

    var hare = gridClone(board)
    var turtle = gridClone(board)
    var skip = 0

    for (i in 1..end) {

        hare = tiltAll(size, hare)

        if (i + skip == end)
            break

        if (i % 2 == 0)
            turtle = tiltAll(size, turtle)

        if (skip == 0 && gridEquals(turtle, hare))
            skip = addUp(i, i - i / 2, end)
    }

    return weightNorth(size, hare)
}

private fun tiltAll(size: Pair<Point, Point>, board: HashMap<Point, Char>): HashMap<Point, Char> {

    var board1 = gridClone(board)     // north up
    tiltNorth(size, board1)           // tilt north
    // printSparseMatrix(board)

    board1 = rotateClockwise(board1)  // west up
    tiltNorth(size, board1)           // tilt west
    // printSparseMatrix(board)

    board1 = rotateClockwise(board1)  // south up
    tiltNorth(size, board1)           // tilt south
    // printSparseMatrix(board)

    board1 = rotateClockwise(board1)  // east up
    tiltNorth(size, board1)           // tilt east
    // printSparseMatrix(board)

    board1 = rotateClockwise(board1)  // north up again
    // printSparseMatrix(board)

    return board1
}

private fun weightNorth(size: Pair<Point, Point>, board: HashMap<Point, Char>): Int {
    var sum = 0
    for (x in 0..size.second.x) {
        for (y in 0..size.second.y) {
            if (board[Point(x, y)] == 'O') {
                sum += size.second.y - y + 1
            }
        }
    }
    return sum
}

private fun tiltNorth(size: Pair<Point, Point>, board: HashMap<Point, Char>) {

    for (x in 0..size.second.x) {
        var free = -1 // the free spot to move a rock to, negative means no spot
        for (y in 0..size.second.y) {
            when (board[Point(x, y)]) {
                '.' -> if (free < 0) free = y
                '#' -> free = -1
                'O' -> if (free > -1) {
                    board[Point(x, free)] = board[Point(x, y)]!!
                    board[Point(x, y)] = '.'
                    free += 1
                }
            }
        }
    }
}
