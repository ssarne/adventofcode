package aoc.aoc2024


import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 140)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 80)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val map = parseCharacterGridToMap(input)
    val visited = mutableSetOf<Point>()
    var sum = 0

    for (p in map.keys) {
        if (visited.contains(p)) continue

        val c = map[p]
        val neighbours = mutableListOf(p)
        var size = 0
        var perimeter = 0
        while (neighbours.size > 0) {
            val f = neighbours.removeAt(0)
            if (visited.contains(f)) continue
            size++
            visited.add(f)
            for (n in f.adjacent()) {
                if (map[n] == c) {
                    if (!visited.contains(n))
                        neighbours.add(n)
                } else {
                    perimeter++
                }
            }
        }
        sum += size * perimeter
    }
    return sum.toLong()
}

private fun execute2(input: List<String>): Long {

    val map = parseCharacterGridToMap(input)
    val visited = mutableSetOf<Point>()
    var sum = 0

    for (p in map.keys) {
        if (visited.contains(p)) continue

        val c = map[p]
        val area = mutableSetOf<Point>()
        val visit = mutableListOf(p)
        while (visit.size > 0) {
            val f = visit.removeAt(0)
            if (area.contains(f)) continue
            area.add(f)

            for (n in f.adjacent())
                if (map[n] == c)
                    if (!area.contains(n))
                        visit.add(n)
        }

        visited.addAll(area)

        val paid = mutableSetOf<Pair<Point, Point>>()
        val free = mutableSetOf<Pair<Point, Point>>()
        for (a in area) {
            for (b in a.adjacent()) {
                if (map[b] != c) {
                    if (a to b in paid) continue
                    if (a to b in free) continue
                    paid.add(a to b)
                    val ab = b - a // direction cross the fence
                    val d1 = Point(if (ab.x == 0) 1 else 0, if (ab.y == 0) 1 else 0)
                    val d2 = Point(if (ab.x == 0) -1 else 0, if (ab.y == 0) -1 else 0)
                    free.addAll(addFreeFence(a, ab, d1, map))
                    free.addAll(addFreeFence(a, ab, d2, map))
                }
            }
        }

        // println("$c :: ${area.size} * ${paid.size}")
        sum += area.size * paid.size
    }
    return sum.toLong()
}

private fun addFreeFence(
    start: Point,
    across: Point,
    along: Point,
    map: HashMap<Point, Char>
): Set<Pair<Point, Point>> {
    val fence = mutableSetOf<Pair<Point, Point>>()
    var step = 1
    while (true) {
        val inside = start + along * step
        val outside = start + across + along * step
        if (map[start] != map[inside] || map[start] == map[outside])
            return fence
        fence.add(inside to outside)
        step++
    }
}