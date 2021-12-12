package aoc.aoc2021

import aoc.ktutils.asIntArray
import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    check(execute1(readTestLines()), 4512)
    execute1(readLines()).let { println(it) ; check(it , 50008) }

    check(execute2(readTestLines()), 1924)
    execute2(readLines()).let { println(it) ; check(it, 17408) }
}

private fun execute1(input: List<String>): Int {

    val numbers = asIntArray(input.first())
    val boards = parseBoards(input)

    for (number in numbers) {
        val result = play(boards, number)
        if (result != null) return result.score
    }
    return 0
}

private fun execute2(input: List<String>): Int {

    val numbers = asIntArray(input.first())
    val boards = parseBoards(input)

    for (number in numbers) {
        val result = play(boards, number)
        if (result != null) {
            var next = result
            while (next != null) {
                boards.remove(next.id)
                next = next.next
            }
        }
        if (result != null && boards.size == 0) return result.score
    }

    return 0
}

private data class Result (var id: Int, var score: Int, var next: Result?)

private fun play(boards: HashMap<Int, IntArray>, number: Int): Result? {

    // mark numbers
    for (board in boards.values) {
        for (i in board.indices) {
            if (board[i] == number) {
                board[i] = -1 * number - 1  // add -1 for 0
            }
        }
    }

    var result: Result? = null
    for (k in boards.keys) {
        val board = boards[k]
        if (bingo(board!!)) {
            result = Result(k, number * posSum(board), result)
        }
    }
    return result
}

private fun posSum(board: IntArray): Int {
    var sum = 0
    for (i in board)
        if (i >= 0)
            sum += i
    return sum
}

private fun bingo(board: IntArray): Boolean {
    for (l in 0 until 5) { // check horizontal
        var bingo = true
        for (c in 0 until 5) {
            if (board[l * 5 + c] >= 0) bingo = false
        }
        if (bingo) return true
    }
    for (c in 0 until 5) { // check vertical
        var bingo = true
        for (l in 0 until 5) {
            if (board[l * 5 + c] >= 0) bingo = false
        }
        if (bingo) return true
    }

    return false
}

private fun parseBoards(input: List<String>): HashMap<Int, IntArray> {
    val boards = HashMap<Int, IntArray>()
    val noBoards = input.size / 6
    for (i in 0 until noBoards) {
        var board = IntArray(0)
        for (j in 0 until 5) {
            board += asIntArray(input[i * 6 + j + 2])
        }
        check(board.size, 25)
        boards[i] = board
    }
    return boards
}