package aoc.aoc2018

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), -1)
    check(execute1(readTestLines(2)), -1)
    check(execute1(readTestLines(3)), -1)
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(1)) }

    // check(execute2(readTestLines()), -1)
    // execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val map = CharMatrix.create(input)
    val pop = HashMap<Point, Int>()      // population
    for (y in 0 until map.height)
        for (x in 0 until map.width)
            map.get(x, y).let {
                if (it == 'G' || it == 'E')
                    pop[Point(x, y)] = 200
            }
    // map.print()

    for (y in 0 until map.height) {
        for (x in 0 until map.width) {
            val ct = map.get(x, y)    // champion type
            val cp = Point(x, y)      // champion position
            if (ct == 'E' || ct == 'G') {
                val et = if (ct == 'E') 'G' else 'C'
                var ep = getWeakestEnemy(map, cp, et)
                if (et == null) moveToClosestEnemy(map, cp, et)
                ep = getWeakestEnemy(map, cp, et)
                if (ep != null) combat(map, pop, cp, ep)
            }
        }
    }
    return 0
}

fun getWeakestEnemy(map: CharMatrix, cp: Point, et: Char): Point? {
    var ep: Point? = null // enemy position
    var eh: Int? = null // enemy health
    map.get(cp.x, cp.y + 1).let {
        if (it == et) {
            eh = if (eh == null) pop.get(it.x, it.y)
        }
    }
}

private fun execute2(input: List<String>): Int {
    return 0
}