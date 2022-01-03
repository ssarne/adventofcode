package aoc.aoc2021

import aoc.ktutils.*
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.HashMap
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(), knownSteps, false), 12521)
    execute1(readLines()).let { println(it); check(it, 15299) }

    check(execute2(readTestLines()), 44169)
    execute2(readLines()).let { println(it); check(it, 47193) }
}

val knownSteps = mapOf(
    "...B.......BC.DADCA" to 40,
    "...B.C.....B..DADCA" to 240,
    "...B.......B.CDADCA" to 440,
)

private fun execute1(input: List<String>, known: Map<String, Int>? = null, print: Boolean = false): Int {
    val goal = "...........ABCDABCD"
    val board = Board.parse(input)
    return play(board, goal, known, print)
}

private fun execute2(input: List<String>, known: Map<String, Int>? = null, print: Boolean = false): Int {
    val goal = "...........ABCDABCDABCDABCD"
    val adjusted = input.toMutableList()
    adjusted.add(3, "  #D#C#B#A#  ")
    adjusted.add(4, "  #D#B#A#C#  ")
    val board = Board.parse(adjusted)
    return play(board, goal, known, print)
}

private fun play(startBoard: Board, goal: String, known: Map<String, Int>?, print: Boolean = false): Int {

    val cache = HashMap<String, Board>()
    val queue = PriorityQueue<Board>()

    queue.add(startBoard)
    while (queue.isNotEmpty()) {
        val board = queue.poll()
        if (cache.containsKey(board.board)) continue
        cache[board.board] = board
        if (print) board.print()

        if (board.board == goal) {
            if (print) board.printPath()
            return board.energy
        }

        if (known != null && known.containsKey(board.board)) {
            if (print) board.print()
        }

        for (i in 0 until board.length) {
            val c = board.pos(i)
            if (c != '.') {
                if (board.homeIsReady(c) && board.wayIsClear(i, board.home(c))) {
                    val next = board.moveHome(i, board.home(c))
                    queue.add(next)
                }
            }
        }

        for (h in 0 until board.rooms) {
            // can an amphipod go directly to a home? yes, but also stop on the way and it will be the same
            val d = 1 + board.homeFreeDepth(h)
            if (d == board.depth) continue // home is empty
            var p = board.entrance(h) + 1
            while (p < board.length) {
                if (board.pos(p) != '.') break
                val next = board.moveFromHomeToPos(h, d, p)
                queue.add(next)
                p += if (p < board.entrance(board.rooms - 1)) 2 else 1   // update p, consider entrances
            }

            p = board.entrance(h) - 1
            while (p >= 0) {
                if (board.pos(p) != '.') break
                val next = board.moveFromHomeToPos(h, d, p)
                queue.add(next)
                p -= if (p < board.entrance(0)) 1 else 2   // update p, consider entrances
            }
        }
    }

    return -1
}

private data class Board(
    val board: String,
    val energy: Int,
    val prev: Board?,
    val length: Int,
    val rooms: Int,
    val depth: Int
) : Comparable<Board> {

    override fun compareTo(other: Board): Int = this.energy - other.energy

    fun pos(i: Int) = board[i]
    fun home(amphipod: Char) = amphipod - 'A'
    fun entrance(home: Int) = (home + 1) * 2
    fun energy(amphipod: Char) = energies[amphipod - 'A']
    fun homeAt(amphipod: Char, depth: Int) = homeAt(home(amphipod), depth)
    fun homeAt(home: Int, depth: Int) = board[homeAtPos(home, depth)]
    fun homeAtPos(home: Int, depth: Int) = length + rooms * depth + home

    fun homeIsReady(a: Char): Boolean {
        for (d in 0 until depth)
            if (!(homeAt(a, d) == a || homeAt(a, d) == '.'))
                return false
        return true
    }

    fun homeFreeDepth(home: Int): Int {
        var dd = -1
        for (d in 0 until depth)
            if (homeAt(home, d) == '.')
                dd = d
        return dd
    }

    fun wayIsClear(pos: Int, home: Int): Boolean {
        val entrance = entrance(home)
        if (pos < entrance) {
            for (p in pos + 1..entrance) {
                if (board[p] != '.') {
                    return false
                }
            }
        } else {
            for (p in entrance until pos) {
                if (board[p] != '.') {
                    return false
                }
            }
        }
        return true
    }

    fun moveHome(pos: Int, home: Int): Board {
        val d = homeFreeDepth(home)
        if (d < 0) throw RuntimeException("wat - home not ready")
        val hp = homeAtPos(home, d)
        val chars = board.toCharArray()
        val c = chars[pos]
        chars[pos] = '.'
        chars[hp] = c
        val steps = abs(pos - entrance(home(c))) + d + 1
        val energy = steps * energy(c)
        return Board(String(chars), this.energy + energy, this, this.length, this.rooms, this.depth)
    }

    fun moveFromHomeToPos(home: Int, depth: Int, pos: Int): Board {
        if (depth < 0) throw RuntimeException("wat - depth=$depth")
        if (depth >= this.depth) throw RuntimeException("wat - depth=$depth")
        val chars = board.toCharArray()
        val homePos = homeAtPos(home, depth)
        val c = chars[homePos]
        if (c < 'A' || c > 'D') throw RuntimeException("wat - moving=$c")
        chars[homePos] = '.'
        chars[pos] = c
        val steps = abs(pos - entrance(home)) + depth + 1
        val energy = steps * energy(c)
        return Board(String(chars), this.energy + energy, this, this.length, this.rooms, this.depth)
    }

    fun print() {
        print('-'); for (i in 0 until length) print('-'); println("-  energy=$energy")
        print('|'); for (i in 0 until length) print(board[i]); println('|')

        print("--")
        for (h in 0 until rooms) print("|" + homeAt(h, 0))
        println("|--")

        for (d in 1 until depth) {
            print("  ")
            for (h in 0 until rooms) print("|" + homeAt(h, d))
            println("|  ")
        }

        print("  ")
        for (h in 0 until rooms) print("--")
        println("-  ")
    }

    fun printPath() {
        var current: Board? = this
        while (current != null) {
            current.print()
            current = current.prev
        }
    }

    companion object {
        var energies = arrayOf(1, 10, 100, 1000)
        fun parse(input: List<String>): Board {
            var board = input[1].replace("#", "")
            val hallwayLength = board.length
            val depth = input.size - 3
            var rooms = 0
            for (i in 2 until input.size - 1) {
                val homes = input[i].replace("#", "").replace(" ", "")
                rooms = homes.length
                board += homes
            }
            return Board(board, 0, null, hallwayLength, rooms, depth)
        }
    }
}