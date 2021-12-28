package aoc.ktutils

import java.lang.RuntimeException

class IntMatrix(val matrix: Array<IntArray>, val width: Int, val height: Int): Comparable<Any>  {

    fun get(x: Int, y: Int) = matrix[y][x]
    fun set(x: Int, y: Int, i: Int) = run { matrix[y][x] = i }
    fun inc(x: Int, y: Int) = matrix[y][x]++

    operator fun times(that: IntMatrix): IntMatrix {
        if (this.width != that.height || this.height != that.width)
            throw RuntimeException("Size miss-match, cannot multiply ($width,$height) x (${that.width},${that.height}")
        val result = IntMatrix.create(this.width, that.width)
        for (y in 0 until result.height) {
            for (x in 0 until result.width) {
                var prod = 0
                for (i in 0 until this.width) {
                    prod += this.get(i, y) * that.get(x, i)
                }
                result.set(x, y, prod)
            }
        }
        return result
    }

    override fun equals(that: Any?): Boolean {
        if (that != null && that is IntMatrix) {
            if (this.width != that.height || this.height != that.width) return false
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (this.get(x, y) != that.get(x, y)) return false
                }
            }
            return true
        } else return false
    }

    override fun compareTo(that: Any): Int {
        if (that != null && that is IntMatrix) {
            if (this.height != that.height) return this.height - that.height
            if (this.width != that.width) return this.width - that.width
            for (y in 0 until height) {
                for (x in 0 until width) {
                    if (this.get(x, y) != that.get(x, y)) return this.get(x, y) - that.get(x, y)
                }
            }
            return 0
        } else return -1
    }

    override fun toString(): String {
        var sb = StringBuilder("[")
        for (y in 0 until height) {
            sb.append(matrix[y].joinToString(",", prefix="[",postfix="]"))
        }
        sb.append("]")
        return sb.toString()
    }

    companion object {
        fun create(width: Int, height: Int) = create(width, height, 0)

        fun create(width: Int, height: Int, init: Int): IntMatrix {
            val matrix = Array(height) { IntArray(width) { _ -> init } }
            return IntMatrix(matrix, width, height)
        }

        fun create(input: List<String>): IntMatrix {
            if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
            val height = input.size
            val width = input[0].length
            val matrix = Array(width) { IntArray(height) }
            for (y in input.indices) {
                for (x in 0 until input[y].length) {
                    matrix[y][x] = Character.getNumericValue(input[y][x])
                }
            }
            return IntMatrix(matrix, width, height)
        }

        @JvmName("createListIA")
        fun create(input: List<IntArray>): IntMatrix {
            if (input.isEmpty()) throw RuntimeException("Height should be larger than 0. Input list is empty.")
            val height = input.size
            val width = input[0].size
            val matrix = Array(width) { IntArray(height) }
            for (y in input.indices) {
                for (x in 0 until width) {
                    matrix[y][x] = input[y][x]
                }
            }
            return IntMatrix(matrix, width, height)
        }

    }
}
