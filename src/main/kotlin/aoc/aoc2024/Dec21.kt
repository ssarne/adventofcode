package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    test("029A")
    execute(readTestLines(1), 2).let { println("Test: $it") ; check(it, 126384L) }
    execute(readLines(), 2).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute(readTestLines(1), 25).let { println("Test: $it") ; check(it, -1L) }
    execute(readLines(), 25).let { println(it) } // ; check(it, readAnswerAsLong(2)) }
}

fun press(pad: Array<String>, start: Point, line: String,
          cache: MutableMap<Triple<Point, Point, String>, Set<String>>): Set<String> {
    var paths = mutableSetOf("")
    var pos = start
    for (c in line) {
        val next = findKey(pad, c)
        val nexts = findPaths(pos, next, pad, cache)
        val all = mutableSetOf<String>()
        for (p in paths)
            for (n in nexts)
                all.add(p + n)
        pos = next
        paths = all
    }
    return paths
}

fun findPaths(start: Point, end: Point, pad: Array<String>,
              cache: MutableMap<Triple<Point, Point, String>, Set<String>>): Set<String> {
    val dx = if (end.x - start.x > 0) 1 else if (end.x - start.x < 0) -1 else 0
    val dy = if (end.y - start.y > 0) 1 else if (end.y - start.y < 0) -1 else 0
    val paths = enumeratePaths(start, end, dx, dy, pad, "", cache)
    return paths
}

fun enumeratePaths(pos: Point, target: Point, dx: Int, dy: Int, pad: Array<String>, path: String,
                   cache: MutableMap<Triple<Point, Point, String>, Set<String>>): Set<String> {

    val key = Triple(pos, target, path)
    cache[key].let {  if (it != null) return it}

    if (pos == target) return mutableSetOf(path + 'A')
    if (pad[pos.y][pos.x] == '_') return mutableSetOf()
    val paths = mutableSetOf<String>()
    if (pos.x != target.x) {
        val next = path + if (dx == -1) '<' else '>'
        paths.addAll(enumeratePaths(Point(pos.x + dx, pos.y), target, dx, dy, pad, next, cache))
    }
    if (pos.y != target.y) {
        val next = path + if (dy == -1) '^' else 'v'
        paths.addAll(enumeratePaths(Point(pos.x, pos.y + dy), target, dx, dy, pad, next, cache))
    }

    // cannot just pick shortest interim result
    // val shortest = paths.minBy { it.length }
    // paths = mutableSetOf(shortest)

    cache[key] = paths
    return paths
}

private fun test(input: String) {
    val numPad = arrayOf("789", "456", "123", "_0A")
    val keyPad = arrayOf("_^A", "<v>")

    val seq1 = press(numPad, findKey(numPad, 'A'), input, mutableMapOf())
    //for (l in seq1) println(l)
    check(seq1.contains("<A^A>^^AvvvA"))

    val cache = mutableMapOf<Triple<Point, Point, String>, Set<String>>()
    val seq2 = press(keyPad, findKey(keyPad, 'A'), "<A^A>^^AvvvA", cache)
    //for (l in seq2) println(l)
    check(seq2.contains("v<<A>>^A<A>AvA<^AA>A<vAAA>^A"))

    val seq3 = press(keyPad, findKey(keyPad, 'A'), "v<<A>>^A<A>AvA<^AA>A<vAAA>^A", cache)
    //for (l in seq3) println(l)
    check(seq3.contains("<vA<AA>>^AvAA<^A>A<v<A>>^AvA^A<vA>^A<v<A>^A>AAvA^A<v<A>A>^AAAvA<^A>A"))
}


private fun execute(input: List<String>, levels: Int): Long {

    val numPad = arrayOf("789", "456", "123", "_0A")
    val keyPad = arrayOf("_^A", "<v>")
    val cache = mutableMapOf<Triple<Point, Point, String>, Set<String>>()
    var sum = 0L

    for (line in input) {
        var sequences = press(numPad, findKey(numPad, 'A'), line, mutableMapOf())
        for (i in 1 .. levels) {
            val next = mutableSetOf<String>()
            for (seq in sequences) next.addAll(press(keyPad, findKey(keyPad, 'A'), seq, cache))
            sequences = next
        }
        val shortest = sequences.minBy { it.length }
        val num = line.replace("A", "").toInt()
        sum += shortest.length * num
        println("$line ${shortest.length} * $num: $shortest")
    }

    return sum
}

fun findKey(pad: Array<String>, key: Char): Point {
    for (y in pad.indices)
        for (x in pad[y].indices)
            if (pad[y][x] == key)
                return Point(x, y)
    throw RuntimeException("CMH $key ${pad.toList()}")
}
