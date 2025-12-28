package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    execute(testLines()).let {
        check(it.first, 6)
        println("Test:   ${it.first}")
    }

    val (count, screen) = execute(readLines())
    check(count, answerI(1))
    println("Result: $count")
    printSparseMatrix(screen)
}

private fun execute(input: List<String>): Pair<Int, Map<Point, Char>> {
    val screen = initScreen()
    for (line in input) {
        if (line.startsWith("rect ")) { // rect 3x2
            val dimensions = line.substring("rect ".length).split("x")
            rectangle(screen, dimensions[0].toInt(), dimensions[1].toInt())
        }
        if (line.startsWith("rotate column x=")) { // rotate column x=1 by 1
            val dimensions = line.substring("rotate column x=".length).split(" by ")
            rotateColumn(screen, dimensions[0].toInt(), dimensions[1].toInt())
        }
        if (line.startsWith("rotate row y=")) { // rotate row y=0 by 4
            val dimensions = line.substring("rotate row y=".length).split(" by ")
            rotateRow(screen, dimensions[0].toInt(), dimensions[1].toInt())
        }
    }
    return screen.values.count { it == '#' } to screen
}

private fun rectangle(screen: HashMap<Point, Char>, width: Int, height: Int) {
    for (x in 0 until width)
        for (y in 0 until height)
            screen[Point(x, y)] = '#'
}

private fun rotateColumn(screen: HashMap<Point, Char>, column: Int, length: Int) {
    val list = mutableListOf<Char>()
    for (y in 0 until 6)
        list.add(screen[Point(column, y)]!!)
    repeat (length) {
        val tmp = list.removeAt(list.size - 1)
        list.add(0, tmp)
    }
    for (y in 0 until 6)
        screen[Point(column, y)] = list[y]
}

private fun rotateRow(screen: HashMap<Point, Char>, row: Int, length: Int) {
    val list = mutableListOf<Char>()
    for (x in 0 until 50)
        list.add(screen[Point(x, row)]!!)
    repeat (length) {
        val tmp = list.removeAt(list.size - 1)
        list.add(0, tmp)
    }
    for (x in 0 until 50)
        screen[Point(x, row)] = list[x]
}


private fun execute2(input: List<String>): Long {
    return 0L
}

private fun initScreen(): HashMap<Point, Char> {
    val screen = HashMap<Point, Char>() // 50 x 6
    for (x in 0 until 50)
        for (y in 0 until 6)
            screen[Point(x, y)] = ' '
    return screen
}

