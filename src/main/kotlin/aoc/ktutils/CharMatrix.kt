package aoc.ktutils

import java.lang.RuntimeException

class CharMatrix(val matrix: Array<CharArray>, val width: Int, val height: Int) {

    fun get(x: Int, y: Int) = matrix[y][x]
    fun set(x: Int, y: Int, c: Char) = run { matrix[y][x] = c }

    fun print(header: Boolean = false, msg: String = "") {
        if (header) for (x in 0 until width) print("="); println("$msg")
        for (y in 0 until height) {
            for (x in 0 until width) {
                print(matrix[y][x])
            }
            println()
        }
    }

    companion object {
        fun create(width: Int, height: Int, init: Char): CharMatrix {
            val matrix = Array(height) { CharArray(width) { _ -> init } }
            return CharMatrix(matrix, width, height)
        }

        fun create(input: List<String>): CharMatrix {
            if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
            val height = input.size
            val width = input[0].length
            val matrix = Array(height) { CharArray(width) }
            for (y in 0 until height) {
                for (x in 0 until width) {
                    matrix[y][x] = input[y][x]
                }
            }
            return CharMatrix(matrix, width, height)
        }
    }
}