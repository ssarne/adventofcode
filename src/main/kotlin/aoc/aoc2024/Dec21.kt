package aoc.aoc2024

import aoc.ktutils.*

fun main() {

    test()

    execute(readTestLines(1), 2 + 1).let { check(it, 126384L) } // ; println("Test: $it")}
    execute(readLines(), 2 + 1).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute(readTestLines(1), 25 + 1).let { check(it, 154115708116294L) } // ; println("Test: $it") } //
    execute(readLines(), 25 + 1).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun test() {

    val cache = mutableMapOf<Triple<Point, Point, Int>, Long>()

    val seq1 = pressNumber("029A", 1, cache)
    //println(seq1)
    check(seq1.toInt(), "<A^A>^^AvvvA".length)

    val seq2 = pressNumber("029A", 2, cache)
    //println(seq2)
    check(seq2.toInt(), "v<<A>>^A<A>AvA<^AA>A<vAAA>^A".length)

    val seq3 = pressNumber("029A", 3, cache)
    //println(seq3)
    check(seq3.toInt(), ("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A".length))
}

private fun execute(input: List<String>, robots: Int): Long {

    val cache = mutableMapOf<Triple<Point, Point, Int>, Long>() // from -> to, level -> length
    var sum = 0L

    for (line in input) {
        val sequences = pressNumber(line, robots, cache)
        val num = line.replace("A", "").toInt()
        sum += sequences * num
    }

    return sum
}

private fun pressNumber(line: String, robots: Int,
    cache: MutableMap<Triple<Point, Point, Int>, Long>): Long {

    val numPad = arrayOf("789", "456", "123", "_0A")
    var pos = findKey(numPad, 'A')
    val ignore = findKey(numPad, '_')

    var result = 0L
    for (c in line) {
        val next = findKey(numPad, c)
        result += pressNumber(pos, next, robots, ignore, cache)
        pos = next
    }
    return result
}

private fun pressNumber(start: Point, end: Point, robots: Int, ignore: Point,
          cache: MutableMap<Triple<Point, Point, Int>, Long>): Long {

    var result: Long? = null
    val queue = mutableListOf(start to "")

    while (queue.isNotEmpty()) {
        val (pos, path) = queue.removeAt(0)
        if (pos == end) {
            val res = pressDirection(path + "A", robots, cache)
            if (result == null || res < result) result = res
            continue
        }
        if (pos == ignore) continue
        if (pos.x < end.x) queue.add(Point(pos.x + 1, pos.y) to path + ">")
        if (pos.x > end.x) queue.add(Point(pos.x - 1, pos.y) to path + "<")
        if (pos.y < end.y) queue.add(Point(pos.x, pos.y + 1) to path + "v")
        if (pos.y > end.y) queue.add(Point(pos.x, pos.y - 1) to path + "^")
    }
    return result!!
}

private fun pressDirection(path: String, robots: Int,
                           cache: MutableMap<Triple<Point, Point, Int>, Long>): Long {

    if (robots == 1) return path.length.toLong()

    val keyPad = arrayOf("_^A", "<v>")
    val ignore = findKey(keyPad, '_')
    var pos = findKey(keyPad, 'A')
    var result = 0L

    for (c in path) {
        val next = findKey(keyPad, c)
        result += pressDirection(pos, next, robots, ignore, cache)
        pos = next
    }
    return result
}

private fun pressDirection(start: Point, end: Point, robots: Int, ignore: Point,
                cache: MutableMap<Triple<Point, Point, Int>, Long>): Long {

    val key = Triple(start, end, robots)
    cache[key].let { if (it != null) return it}

    var result: Long? = null
    val queue = mutableListOf(start to "")
    while (queue.isNotEmpty()) {
        val (pos, path) = queue.removeAt(0)
        if (pos == end) {
            val res = pressDirection(path + "A", robots - 1, cache)
            if (result == null || res < result) result = res
            continue
        }
        if (pos == ignore) continue
        if (pos.x < end.x) queue.add(Point(pos.x + 1, pos.y) to path + ">")
        if (pos.x > end.x) queue.add(Point(pos.x - 1, pos.y) to path + "<")
        if (pos.y < end.y) queue.add(Point(pos.x, pos.y + 1) to path + "v")
        if (pos.y > end.y) queue.add(Point(pos.x, pos.y - 1) to path + "^")
    }

    cache[key] = result!!
    return result
}

private fun findKey(pad: Array<String>, key: Char): Point {
    for (y in pad.indices)
        for (x in pad[y].indices)
            if (pad[y][x] == key)
                return Point(x, y)
    throw RuntimeException("CMH $key ${pad.toList()}")
}
