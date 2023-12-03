package aoc.aoc2023


import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 4361)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 467835)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    var sum = 0
    for (p in map.keys) {
        if (map[p] == '.') continue
        if (map[p]!!.isDigit()) {
            val sp = getEdgeOfNumber(p, -1, map) // start point
            val ep = getEdgeOfNumber(p, 1, map)  // end point
            val num = getNumber(sp, ep, map)         // read string from map
            if (hasSurroundingSymbol(sp, ep, map))   // check for parts
                sum += num.toInt()                   // add up
            erase(sp, ep, map)                       // erase all digits in number from map
        }
    }

    return sum
}

private fun execute2(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    var sum = 0
    for (p in map.keys) {
        if (map[p] == '.') continue
        if (map[p] == '*') {
            val nps = getAdjacentPoints(p, map)     // set of digits around symbol
            if (!nps.iterator().hasNext()) continue // there is no number, skip *
            val np1 = nps.iterator().next()
            val sp1 = getEdgeOfNumber(np1, -1, map)
            val ep1 = getEdgeOfNumber(np1, 1, map)
            val num1 = getNumber(sp1, ep1, map)
            erase(sp1, ep1, nps)

            if (!nps.iterator().hasNext()) continue // there is no second number, skip *
            val np2 = nps.iterator().next()
            val sp2 = getEdgeOfNumber(np2, -1, map)
            val ep2 = getEdgeOfNumber(np2, 1, map)
            val num2 = getNumber(sp2, ep2, map)
            erase(sp2, ep2, nps)

            if (nps.iterator().hasNext()) continue // there is a third number, skip *

            sum += num1.toInt() * num2.toInt()
        }
    }
    return sum
}

private fun hasSurroundingSymbol(sp: Point, ep: Point, map: HashMap<Point, Char>): Boolean {
    var found = false
    for (y in sp.y - 1..sp.y + 1) {
        for (x in sp.x - 1..ep.x + 1) {
            val c = map[Point(x, y)] ?: '.'
            if (!(c.isDigit() || c == '.')) found = true
        }
    }
    return found
}

private fun getAdjacentPoints(p: Point, map: HashMap<Point, Char>): HashSet<Point> {
    val nps = HashSet<Point>()
    for (n in p.surrounding())
        if ((map[n] ?: '.').isDigit())
            nps.add(n)
    return nps
}

private fun getNumber(sp: Point, ep: Point, map: HashMap<Point, Char>): String {
    var num = ""
    for (x in sp.x..ep.x) {
        val c = map[Point(x, sp.y)] ?: '.'
        if (c.isDigit()) num += c
    }
    return num
}

private fun erase(sp1: Point, ep1: Point, map: HashSet<Point>) {
    for (x in sp1.x..ep1.x) {
        if (map.contains(Point(x, sp1.y)))
            map.remove(Point(x, sp1.y))
    }
}

private fun erase(sp: Point, ep: Point, map: HashMap<Point, Char>) {
    for (x in sp.x..ep.x) {
        if (map.contains(Point(x, sp.y)))
            map[Point(x, sp.y)] = '.'
    }
}

private fun getEdgeOfNumber(p: Point, dx: Int, map: HashMap<Point, Char>): Point {
    var ep = p
    while ((map[Point(ep.x + dx, ep.y)] ?: '.').isDigit())
        ep = Point(ep.x + dx, ep.y)
    return ep
}