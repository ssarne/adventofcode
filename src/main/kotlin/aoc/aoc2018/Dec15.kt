package aoc.aoc2018

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap

fun main() {
    check(execute1(readTestLines(1)), 27828)
    check(execute1(readTestLines(2)), 27730)
    check(execute1(readTestLines(3)), 36334)
    check(execute1(readTestLines(4)), 39514)
    check(execute1(readTestLines(5)), 27755)
    check(execute1(readTestLines(6)), 28944)
    check(execute1(readTestLines(7)), 18740)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 4988)
    check(execute2(readTestLines(4)), 31284)
    check(execute2(readTestLines(5)), 3478)
    check(execute2(readTestLines(6)), 6474)
    check(execute2(readTestLines(7)), 1140)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private data class Unit(var position: Point, val type: Char, var hp: Int, val strength: Int) {

    var round: Int = 0
    fun enemy() = if (type == 'E') 'G' else 'E'
    override fun toString() = "[${position.x}, ${position.y}] $type hp=$hp"
}

private fun execute1(input: List<String>): Int {

    val (map, pop) = setup(input, 3)

    play(map, pop)

    val sum = pop.values.sumOf { it.hp }
    val rounds = pop.values.minOf { it.round }
    val res = sum * rounds

    // map.print(true, "All rounds completed.  $res")
    // for (c in pop.values.sortedBy { it.position.y * 100 + it.position.x }) println(c)

    return res
}

private fun execute2(input: List<String>): Int {

    for (power in 3..10000) {
        val (map, pop) = setup(input, power)
        val elves1 = pop.values.count() { it.type == 'E'}
        play(map, pop)

        val elves2 = pop.values.count() { it.type == 'E'}
        val sum = pop.values.sumOf { it.hp }
        val rounds = pop.values.minOf { it.round }
        val res = sum * rounds

        // map.print(true, " All rounds completed. Elves($elves1->$elves2) power=$power. Outcome: $rounds * $sum = $res")
        // for (c in pop.values.sortedBy { it.position.y * 100 + it.position.x }) println(c)

        if (elves1 == elves2) return res
    }

    return -1
}

private fun setup(input: List<String>, power: Int): Pair<CharMatrix, HashMap<Point, Unit>> {
    val map = CharMatrix.create(input)
    val pop = HashMap<Point, Unit>()      // population
    for (y in 0 until map.height) {
        for (x in 0 until map.width) {
            val c = map.get(x, y)
            if (c == 'G') Point(x, y).let { pop[it] = Unit(it, c, 200, 3) }
            if (c == 'E') Point(x, y).let { pop[it] = Unit(it, c, 200, power) }
        }
    }

    return Pair(map, pop)
}

private fun play(map: CharMatrix, pop: HashMap<Point, Unit>) {

    for (round in 0..10000) {
        for (y in 0 until map.height) {
            for (x in 0 until map.width) {
                val c = map.get(x, y)
                if (!(c == 'E' || c == 'G')) continue

                val champ = pop[Point(x, y)]!!
                if (champ.round != round) continue

                // Check there is someone to fight, otherwise break
                if (pop.values.find() { it.type == champ.enemy() } == null)
                    return

                // Champ starting its round
                champ.round++

                // Move, if not already positioned to combat
                if (!hasAdjacentEnemy(map, champ)) {
                    val path = getPathToClosestEnemy(map, champ)
                    if (path.isNotEmpty())
                        move(map, pop, champ, path.first())
                }

                // Combat, if in reach
                if (hasAdjacentEnemy(map, champ)) {
                    val enemy = getWeakestAdjacentEnemy(map, pop, champ)!!
                    combat(map, pop, champ, enemy)
                }
            }
        }

        // val sum = pop.values.sumOf { it.hp }
        // map.print(true, "Round " + round + " completed. hp=$sum")
        // for (c in pop.values.sortedBy { it.position.y * 100 +  it.position.x}) println(c)
    }
}

private fun combat(map: CharMatrix, pop: HashMap<Point, Unit>, champ: Unit, enemy: Unit) {

    enemy.hp -= champ.strength
    if (enemy.hp <= 0) {
        pop.remove(enemy.position)
        map.set(enemy.position.x, enemy.position.y, '.')
    }
}

// Shortest path, manhattan, to the closest enemy
// Of the enemies at the same distance, pick the one in reading order
private fun getPathToClosestEnemy(map: CharMatrix, champ: Unit): List<Point> {
    val paths: Queue<List<Point>> = LinkedList()
    paths.add(listOf(champ.position))
    val visited = map.copyOf()
    var best: MutableList<Point>? = null

    while (!paths.isEmpty()) {
        val path = paths.poll()

        // abort if there is an enemy at shorter distance found
        if (best != null && best.size <= path.size) continue

        val pos = path.last()
        for (np in getAdjacentReadingOrder(pos)) {
            val c = visited.get(np.x, np.y)
            if (c == champ.enemy()) {
                val nextPath = path.toMutableList()
                nextPath.add(np)
                if (best == null) best = nextPath
                else {
                    val bpe = best.last() // best path end point
                    if (np.y < bpe.y || np.y == bpe.y && np.x < bpe.x)
                        best = nextPath
                }
            }
            if (c == '.') {
                visited.set(np.x, np.y, '_')
                val nextPath = path.toMutableList()
                nextPath.add(np)
                paths.add(nextPath)
            }
        }
    }

    if (best != null) {
        val res = best.toMutableList()
        res.removeAt(0)
        return res
    }

    return emptyList()
}

private fun getAdjacentReadingOrder(p: Point): List<Point> {
    return listOf(
        Point(p.x, p.y - 1),
        Point(p.x - 1, p.y),
        Point(p.x + 1, p.y),
        Point(p.x, p.y + 1)
    )

}

private fun hasAdjacentEnemy(map: CharMatrix, champ: Unit): Boolean {
    for (p in getAdjacentReadingOrder(champ.position))
        if (map.get(p.x, p.y) == champ.enemy())
            return true
    return false
}

private fun getWeakestAdjacentEnemy(map: CharMatrix, pop: HashMap<Point, Unit>, champ: Unit): Unit? {

    var e: Unit? = null

    for (p in getAdjacentReadingOrder(champ.position)) {
        if (map.get(p.x, p.y) == champ.enemy()) {
            val t = pop.get(p)!!
            if (e == null || e.hp > t.hp)
                e = t
        }
    }

    return e
}

private fun move(map: CharMatrix, pop: HashMap<Point, Unit>, champ: Unit, np: Point) {
    map.set(champ.position.x, champ.position.y, '.')
    pop.remove(champ.position)
    champ.position = np
    pop[champ.position] = champ
    map.set(champ.position.x, champ.position.y, champ.type)
}