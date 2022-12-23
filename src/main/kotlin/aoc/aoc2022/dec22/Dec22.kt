package aoc.aoc2022.dec22


import aoc.ktutils.*
import java.lang.Math.floorMod
import kotlin.math.*
import kotlin.RuntimeException

fun main() {
    // check(execute1(readTestLines()), 6032)
    //execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(1)) }

    testWrap2()
    check(execute2(readTestLines()), 5031)
    //execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    val (map, path) = parse(input)
    val width = map.keys.map { it.x }.max()!!
    val height = map.keys.map { it.y }.max()!!
    //printSparseMatrix(map)

    val dirs = charArrayOf('>', 'v', '<', '^')
    var pos = Pos(Point(1, 1), '>')
    while (!map.containsKey(pos.p)) pos = Pos(Point(pos.p.x + 1, pos.p.y), pos.d)
    map[pos.p] = pos.d
    //printSparseMatrix(map)

    for (t in path) {
        when (t) {
            "R" -> {
                pos = Pos(pos.p, turnRight(dirs, pos.d))
                map[pos.p] = pos.d
            }

            "L" -> {
                pos = Pos(pos.p, turnLeft(dirs, pos.d))
                map[pos.p] = pos.d
            }

            else -> {
                for (i in 1..t.toInt()) {
                    var next = when (pos.d) {
                        '>' -> Pos(Point(pos.p.x + 1, pos.p.y), pos.d)
                        'v' -> Pos(Point(pos.p.x, pos.p.y + 1), pos.d)
                        '<' -> Pos(Point(pos.p.x - 1, pos.p.y), pos.d)
                        '^' -> Pos(Point(pos.p.x, pos.p.y - 1), pos.d)
                        else -> throw RuntimeException("CMH")
                    }
                    if (!map.containsKey(next.p)) {
                        next = Pos(wrap1(pos, map, width, height), pos.d)
                    }
                    if (map[next.p] == '#') break
                    pos = next
                    map[pos.p] = pos.d
                }
            }
        }
        //printSparseMatrix(map)
    }
    //printSparseMatrix(map)
    return 1000 * pos.p.y + 4 * pos.p.x + dirs.indexOf(pos.d)
}

private fun execute2(input: List<String>): Int {

    val (map, path) = parse(input)
    val width = map.keys.map { it.x }.max()!!
    val height = map.keys.map { it.y }.max()!!
    val side = max(width, height) / 4
    //printSparseMatrix(map)

    val dirs = charArrayOf('>', 'v', '<', '^')
    var pos = Pos(Point(1, 1), '>')
    while (!map.containsKey(pos.p)) pos = Pos(Point(pos.p.x + 1, pos.p.y), pos.d)
    map[pos.p] = pos.d
    // printSparseMatrix(map)

    for (t in path) {
        when (t) {
            "R" -> {
                pos = Pos(pos.p, turnRight(dirs, pos.d))
                map[pos.p] = pos.d
            }

            "L" -> {
                pos = Pos(pos.p, turnLeft(dirs, pos.d))
                map[pos.p] = pos.d
            }

            else -> {
                for (i in 1..t.toInt()) {
                    var next = when (pos.d) {
                        '>' -> Pos(Point(pos.p.x + 1, pos.p.y), '>')
                        'v' -> Pos(Point(pos.p.x, pos.p.y + 1), 'v')
                        '<' -> Pos(Point(pos.p.x - 1, pos.p.y), '<')
                        '^' -> Pos(Point(pos.p.x, pos.p.y - 1), '^')
                        else -> throw RuntimeException("CMH")
                    }
                    if (!map.containsKey(next.p)) {
                        next = wrap2(pos, side, map)
                    }
                    if (map[next.p] == '#')
                        break
                    // if (map[next] == '.') pos = next
                    map[pos.p] = pos.d
                }
            }
        }
        // printSparseMatrix(map)
    }
    //printSparseMatrix(map)
    return 1000 * pos.p.y + 4 * pos.p.x + dirs.indexOf(pos.d)
}

private data class Pos(val p: Point, val d: Char)

private fun turnRight(dirs: CharArray, dir: Char) = dirs[Math.floorMod(dirs.indexOf(dir) + 1, dirs.size)]
private fun turnLeft(dirs: CharArray, dir: Char) = dirs[Math.floorMod(dirs.indexOf(dir) - 1, dirs.size)]
private fun wrap1(pos: Pos, map: HashMap<Point, Char>, width: Int, height: Int): Point {
    when (pos.d) {
        '>' -> for (x in 0..width)
            if (map.containsKey(Point(x, pos.p.y)))
                return Point(x, pos.p.y)

        'v' -> for (y in 0..height)
            if (map.containsKey(Point(pos.p.x, y)))
                return Point(pos.p.x, y)

        '<' -> for (x in 0..width)
            if (map.containsKey(Point(width - x, pos.p.y)))
                return Point(width - x, pos.p.y)

        '^' -> for (y in 0..height)
            if (map.containsKey(Point(pos.p.x, height - y)))
                return Point(pos.p.x, height - y)
    }
    throw RuntimeException("CMH")
}

private fun testWrap2() {
    val (map, path) = parse(readTestLines(2))
    check(wrap2(Pos(Point(6, 1), '^'), 4, map), Pos(Point(1, 14), '>'))
    check(wrap2(Pos(Point(12, 2), '>'), 4, map), Pos(Point(8, 11), '<'))
    check(wrap2(Pos(Point(6, 12), 'v'), 4, map), Pos(Point(4, 14), '<'))
    check(wrap2(Pos(Point(3, 9), '^'), 4, map), Pos(Point(5, 7), '>'))
    check(wrap2(Pos(Point(8, 8), '>'), 4, map), Pos(Point(12, 4), '^'))
}

private fun check(actual: Pos, expected: Pos) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}



private fun wrap2(
    pos: Pos,
    side: Int,
    map: HashMap<Point, Char>): Pos {
    val mx = if (pos.p.x % side == 0) side else pos.p.x % side
    val my = if (pos.p.y % side == 0) side else pos.p.y % side
    val full = 4 * side
    when (pos.d) {
        '>' -> {
            val ps = mapOf(
                Pair(Point(floorMod(pos.p.x + 1, full), floorMod(pos.p.y, full)), '>'),
                Pair(Point(floorMod(pos.p.x + side - my + 1, full), floorMod(pos.p.y - my + side + 1, full)), 'v'),
                Pair(Point(floorMod(pos.p.x + my, full), floorMod(pos.p.y - my, full)), '^'),
                Pair(Point(floorMod(pos.p.x + side, full), floorMod(pos.p.y + 3 * side - my - my + 1, full)), '<'),
                Pair(Point(floorMod(pos.p.x - side, full), floorMod(pos.p.y + 3 * side - my - my + 1, full)), '<')
            )
            for ((p, d) in ps)
                if (map.containsKey(p))
                    return Pos(p, d)
        }
        'v' -> {
            val ps = mapOf(
                Pair(Point(floorMod(pos.p.x, full), floorMod(pos.p.y + 1, full)), 'v'),
                Pair(Point(floorMod(pos.p.x - mx, full), floorMod(pos.p.y + mx, full)), '<'),
                Pair(Point(floorMod(pos.p.x - mx + side + 1, full), floorMod(pos.p.y + side - mx + 1, full)), '>'),
                Pair(Point(floorMod(pos.p.x + 3 * side - mx - mx + 1, full), floorMod(pos.p.y + side, full)), '^'),
                Pair(Point(floorMod(pos.p.x + 3 * side - mx - mx + 1, full), floorMod(pos.p.y - side, full)), '^'),
            )
            for ((p, d) in ps)
                if (map.containsKey(p))
                    return Pos(p, d)
        }

        '<' -> {
            val ps = mapOf(
                Pair(Point(floorMod(pos.p.x - 1, full), floorMod(pos.p.y, full)), '<'),
                Pair(Point(floorMod(pos.p.x - my, full), floorMod(pos.p.y - my, full)), '^'),
                Pair(Point(floorMod(pos.p.x - side + my - 1, full), floorMod(pos.p.y + side - my + 1, full)),'v' ),
                Pair(Point(floorMod(pos.p.x - side, full), floorMod(pos.p.y - my - side - my + 1, full)),'>' ),
                Pair(Point(floorMod(pos.p.x + side, full), floorMod(pos.p.y - my - side - my + 1, full)), '>'),
            )
            for ((p, d) in ps)
                if (map.containsKey(p))
                    return Pos(p, d)
        }

        '^' -> {
            val ps = mapOf(
                Pair(Point(floorMod(pos.p.x, full), floorMod(pos.p.y - 1, full)), '^'),
                Pair(Point(floorMod(pos.p.x - mx, full), floorMod(pos.p.y - mx + 1, full)), '<'),
                Pair(Point(floorMod(pos.p.x + side - mx + 1, full), floorMod(pos.p.y - side + mx - 1, full)), '>'),
                Pair(Point(floorMod(pos.p.x - side - mx + 1, full), floorMod(pos.p.y - side + mx - 1, full)), '>'),
                Pair(Point(floorMod(pos.p.x - mx - mx - side + 1, full), floorMod(pos.p.y + side, full)), 'v'),
                Pair(Point(floorMod(pos.p.x - mx - mx - side + 1, full), floorMod(pos.p.y - side, full)), 'v'),
            )
            for ((p, d) in ps)
                if (map.containsKey(p))
                    return Pos(p, d)
        }
    }

    //printSparseMatrix(map)
    throw RuntimeException("CMH $pos")
}

private fun parse(input: List<String>): Pair<HashMap<Point, Char>, List<String>> {
    var grid = HashMap<Point, Char>()
    for (y in input.indices) {
        if (input[y].trim() == "") break
        for (x in 0 until input[y].length) {
            if (input[y][x] != ' ') {
                grid.put(Point(x + 1, y + 1), input[y][x])
            }
        }
    }

    val line = input[input.size - 1]
    val path = ArrayList<String>()
    var i = 0
    while (i < line.length) {
        if (line[i].isLetter()) {
            path.add(line.substring(i, i + 1))
            i++
            continue
        } else {
            var j = i
            while (j < line.length && line[j].isDigit()) j++
            path.add(line.substring(i, j))
            i = j
        }
    }

    return Pair(grid, path)
}

/*
when (dir) {
    '>' -> {
        if (pos.p.x == 3 * side && pos.p.y in 1..side)
            return Pair(Point(4 * side, 3 * side - my + 1), '<')
        if (pos.p.x == 3 * side && pos.p.y in side + 1..2 * side)
            return Pair(Point(4 * side - my + 1, side * 2 + 1), 'v')
        if (pos.p.x == 4 * side && pos.p.y in 2 * side + 1..3 * side)
            return Pair(Point(3 * side, side - my + 1), '<')
        throw RuntimeException("CMH $pos $dir")
    }

    'v' -> {
        if (pos.p.y == 2 * side && pos.p.x in 1..side)
            return Pair(Point(3 * side - mx + 1, 3 * side), '^')
        if (pos.p.y == 2 * side && pos.p.x in side + 1..2 * side)
            return Pair(Point(2 * side + 1, 3 * side - mx + 1), '>')
        if (pos.p.y == 3 * side && pos.p.x in 2 * side + 1..3 * side)
            return Pair(Point(side - mx + 1, 2 * side), '^')
        if (pos.p.y == 3 * side && pos.p.x in 3 * side + 1..4 * side)
            return Pair(Point(1, 2 * side - mx + 1), '>')
        throw RuntimeException("CMH $pos $dir")
    }
    '<' -> {
        if (pos.p.x == 2 * side + 1 && pos.p.y in 1..side)
            return Pair(Point(2 * side - my + 1, side + 1), 'v')
        if (pos.p.x == 1 && pos.p.y in side + 1..2 * side)
            return Pair(Point(4 * side - my + 1, 3 * side), '^')
        if (pos.p.x == 2 * side + 1 && pos.p.y in 2 * side + 1..3 * side)
            return Pair(Point(2 * side - my + 1, 2 * side), '^')
        throw RuntimeException("CMH $pos $dir")
    }

    '^' -> {
        if (pos.p.y == 1 * side + 1 && pos.p.x in 1..side)
            return Pair(Point(3 * side, side - mx + 1), '<')
        if (pos.p.y == 1 * side + 1 && pos.p.x in side + 1..2 * side)
            return Pair(Point(2 * side + 1, mx), '>')
        if (pos.p.y == 1 && pos.p.x in 2 * side + 1..3 * side)
            return Pair(Point(side - mx + 1, side + 1), 'v')
        if (pos.p.y == 2 * side + 1 && pos.p.x in 3 * side + 1..4 * side)
            return Pair(Point(3 * side, 2 * side - mx + 1), '<')
        throw RuntimeException("CMH $pos $dir")
    }
}
throw RuntimeException("CMH")

 */
