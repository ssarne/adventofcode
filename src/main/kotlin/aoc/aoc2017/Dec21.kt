package aoc.aoc2017

import aoc.ktutils.*
import kotlin.math.roundToInt
import kotlin.math.sqrt

fun main() {
    check(execute(readTestLines(), 2), 12)
    execute(readLines(), 5).let { println(it); check(it, readAnswerAsInt(1)) }
    execute(readLines(), 18).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute(input: List<String>, iterations: Int): Int {
    val rules = parse(input)
    var square = Square(listOf(".#.", "..#", "###"))
    repeat(iterations) {
        square = square.evolve(rules)
    }
    return square.count()
}

/**
 * Parse the input rules, return a mapping of squares from->to
 * Also populate the rule set with rotated and mirrored alternatives.
 * The input rules are created, so there are no duplicates when rotating/mirroring.
 */
private fun parse(input: List<String>): Map<Square, Square> {
    val rules = HashMap<Square, Square>()
    for (line in input) {
        var from = Square.create(line.split(" => ")[0].trim())
        var to = Square.create(line.split(" => ")[1].trim())
        repeat(4) {
            rules[from] = to
            from = from.rotate()
        }
        from = from.flip()
        repeat(4) {
            rules[from] = to
            from = from.rotate()
        }
    }
    return rules
}

private data class Square(val pixels: List<String>) {
    fun count() = pixels.sumOf { it.count { c -> c == '#' } }
    fun size() = pixels.size

    fun evolve(rules: Map<Square, Square>): Square {
        val (chunks, chunkSize) = split()
        val parts2 = chunks.map { rules[it]!! }.toList()
        val nextPixels = merge(parts2, chunkSize + 1)
        return Square(nextPixels)
    }

    fun split(): Pair<List<Square>, Int> {
        val parts = ArrayList<Square>()
        val size = if (pixels.size % 2 == 0) 2 else 3
        for (y in pixels.indices step size) {
            for (x in pixels[y].indices step size) {
                val part = ArrayList<String>()
                for (i in 0 until size) {
                    part.add(pixels[y + i].substring(x, x + size))
                }
                parts.add(Square(part))
            }
        }
        return Pair(parts, size)
    }

    fun merge(parts: List<Square>, chunkSize: Int): ArrayList<String> {
        val chunksPerSide = sqrt(parts.size.toDouble()).roundToInt()
        val next = ArrayList<String>(chunksPerSide * chunkSize)
        for (y in 0 until chunksPerSide * chunkSize) next.add("")
        for ((i, part) in parts.withIndex()) {
            for (j in 0 until chunkSize) {
                next[(i / chunksPerSide) * chunkSize + j] += part!!.pixels[j]
            }
        }
        return next
    }

    fun rotate(): Square {
        val nextPixels = ArrayList<String>()
        val size = pixels.size
        for (y in 0 until size) nextPixels.add("")
        for (y in 0 until size) {
            for (x in 0 until size) {
                nextPixels[y] = nextPixels[y] + pixels[x][size - y - 1]
            }
        }
        return Square(nextPixels)
    }

    fun flip(): Square {
        val nextPixels = ArrayList<String>()
        val size = pixels.size
        for (y in 0 until size) nextPixels.add("")
        for (y in 0 until size) {
            for (x in 0 until size) {
                nextPixels[y] = nextPixels[y] + pixels[y][size - x - 1]
            }
        }
        return Square(nextPixels)
    }

    companion object Factory {
        fun create(input: String) = Square(input.split("/").toList())
    }
}

