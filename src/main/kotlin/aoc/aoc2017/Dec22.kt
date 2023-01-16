package aoc.aoc2017


import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(), 7), 5)
    check(execute1(readTestLines(), 70), 41)
    check(execute1(readTestLines(), 10000), 5587)
    execute1(readLines(), 10000).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(), 100), 26)
    check(execute2(readTestLines(), 10000000), 2511944)
    execute2(readLines(), 10000000).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>, iterations: Int): Int {
    var grid = parseCharacterGridToMap(input) as MutableMap<Point, Char>
    var pos = Pos(centerPoint(grid), '^')
    var count = 0
    grid = grid.filter { it.value != '.' }.toMutableMap()

    repeat(iterations) {
        pos = if (grid.containsKey(pos.p)) pos.right() else pos.left()
        if (grid.containsKey(pos.p)) grid.remove(pos.p)
        else { grid[pos.p] = '#' ; count++}
        pos = pos.move()
    }

    return count
}


private fun execute2(input: List<String>, iterations: Int): Int {
    var grid = parseCharacterGridToMap(input) as MutableMap<Point, Char>
    var pos = Pos(centerPoint(grid), '^')
    var count = 0
    grid = grid.filter { it.value != '.' }.toMutableMap()

    repeat(iterations) {
        val c = grid[pos.p]
        pos = when (c) {
            null -> pos.left()
            'W' -> pos
            '#' -> pos.right()
            'F' -> pos.right().right()
            else -> throw RuntimeException("CMH $c")
        }
        if (c == 'F') grid.remove(pos.p)
        else grid[pos.p] = when(c) {
            null -> 'W'
            'W' -> { count++ ; '#'}
            '#' -> 'F'
            // 'F' -> right(right(dir))
            else -> throw RuntimeException("CMH $c")
        }
        pos = pos.move()
    }

    return count
}