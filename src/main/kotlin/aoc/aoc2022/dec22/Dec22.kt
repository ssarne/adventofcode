package aoc.aoc2022.dec22

import aoc.ktutils.*
import java.lang.Math.floorMod
import kotlin.math.*
import kotlin.RuntimeException

fun main() {
    check(execute1(readTestLines()), 6032)
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(1)) }

    testWrapCube()
    check(execute2(readTestLines()), 5031)
    execute2(readLines()).let { println(it) } // ; check(it, readAnswerAsInt(2)) }
    // not 62256
}

private data class Pos(val p: Point, val d: Char)

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
                        next = Pos(wrapFlat(pos, map, width, height), pos.d)
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
    val (size, layout) = identify(map)
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
                        // printSparseMatrix(map, true, "t=$t  pos=$pos")
                        next = wrapCube(pos, size, layout)
                    }

                    if (map[next.p] == '#')
                        break

                    pos = next
                    map[pos.p] = pos.d
                }
            }
        }
        // printSparseMatrix(map)
    }
    //printSparseMatrix(map)
    return 1000 * pos.p.y + 4 * pos.p.x + dirs.indexOf(pos.d)
}

fun identify(map: HashMap<Point, Char>): Pair<Int, String> {

    val width = map.keys.map { it.x }.max()!!
    val height = map.keys.map { it.y }.max()!!
    val size = max(width, height) / 4

    var layout = ""
    for (y in 0..3) {
        for (x in 0..3) {
            val p = Point(x * size + 1, y * size + 1)
            if (map.containsKey(p))
                layout += side(p, size)
        }
    }
    return Pair(size, layout)
}

private fun side(p: Point, size: Int): Char {
    val ids = "ABCD" + "EFGH" + "IJKL" + "MNOP"
    val x = (p.x - 1) / size
    val y = (p.y - 1) / size
    return ids[4 * y + x]
}

private fun turnRight(dirs: CharArray, dir: Char) = dirs[floorMod(dirs.indexOf(dir) + 1, dirs.size)]
private fun turnLeft(dirs: CharArray, dir: Char) = dirs[floorMod(dirs.indexOf(dir) - 1, dirs.size)]
private fun wrapFlat(pos: Pos, map: HashMap<Point, Char>, width: Int, height: Int): Point {
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

/**
 * Wrap the edge of a cube and return the position on next side
 * @param pos For current position
 * @param size For size of a side of the cube
 * @param layout For which sides in a A..D/E..F/G..J/K..N grid the cube map uses
 */
private fun wrapCube(pos: Pos, size: Int, layout: String): Pos {

    val mx = if (pos.p.x % size == 0) size else pos.p.x % size
    val my = if (pos.p.y % size == 0) size else pos.p.y % size
    val side = side(pos.p, size)
    val label = "" + side + pos.d

    if (layout == "BCFIJM") {
        when (label) {
            "C>" -> return Pos(Point(2 * size, 3 * size - my + 1), '<') // J<
            "F>" -> return Pos(Point(2 * size + my, size), '^') // C^
            "J>" -> return Pos(Point(3 * size, size - my + 1), '<') // C<
            "M>" -> return Pos(Point(size + my, 3 * size), '^')  // J^
            "B<" -> return Pos(Point(1, 3 * size - my + 1), '>')  // I>
            "F<" -> return Pos(Point(my, 2 * size + 1), 'v')  // Iv
            "I<" -> return Pos(Point(size + 1, size - my + 1), '>')  // B>
            "M<" -> return Pos(Point(size + my, 1), 'v')  // Bv
            "I^" -> return Pos(Point(size + 1, size + mx), '>')  // F>
            "B^" -> return Pos(Point(1, 3 * size + mx), '>')  // M>
            "C^" -> return Pos(Point(mx, 4 * size), '^')  // M^
            "Mv" -> return Pos(Point(2 * size + mx, 1), 'v')  // Cv
            "Jv" -> return Pos(Point(size, 3 * size + mx), '<')  // M<
            "Cv" -> return Pos(Point(2 * size, size + mx), '<')  // F<
            // "__" -> return Pos(Point(1, 1), '_')  // __
            else -> throw RuntimeException("CMH: $layout ${pos.p} $label")
        }
    }

    if (layout == "CEFGKL") {
        when (label) {
            "G>" -> return Pos(Point(4 * size - my + 1, 2 * size + 1), 'v')  // Lv
            "Kv" -> return Pos(Point(size - mx + 1, 2 * size), '^')  // E^
            "F^" -> return Pos(Point(2 * size + 1, mx), '_')  // C>
            // "__" -> return Pos(Point(1, 1), '_')  // __
            else -> throw RuntimeException("CMH: $layout ${pos.p} $label")
        }
    }

    //printSparseMatrix(map)
    throw RuntimeException("CMH ${pos.p} $side ${pos.d}")
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

private fun testWrapCube() {

    val (map0, _) = parse(readTestLines())
    val (size0, layout0) = identify(map0)
    check(size0, 4)
    check(layout0, "CEFGKL")
    checkWrapCube(Pos(Point(12, 6), '>'), size0, layout0, map0, Pos(Point(15, 9), 'v'))

    val (map1, _) = parse(readTestLines(2))
    val (size1, layout1) = identify(map1)
    check(size1, 4)
    check(layout1, "BCFIJM")
    checkWrapCube(Pos(Point(6, 1), '^'), size1, layout1, map1, Pos(Point(1, 14), '>'))
    checkWrapCube(Pos(Point(12, 2), '>'), size1, layout1, map1, Pos(Point(8, 11), '<'))
    checkWrapCube(Pos(Point(6, 12), 'v'), size1, layout1, map1, Pos(Point(4, 14), '<'))
    checkWrapCube(Pos(Point(3, 9), '^'), size1, layout1, map1, Pos(Point(5, 7), '>'))
    checkWrapCube(Pos(Point(8, 8), '>'), size1, layout1, map1, Pos(Point(12, 4), '^'))

    val (map2, _) = parse(readLines())
    val (size2, layout2) = identify(map2)
    check(size2, 50)
    check(layout2, "BCFIJM")

    checkWrapCube(Pos(Point(150, 1), '>'), size2, layout2, map2, Pos(Point(100, 150), '<'))
    checkWrapCube(Pos(Point(150, 10), '>'), size2, layout2, map2, Pos(Point(100, 141), '<'))
    checkWrapCube(Pos(Point(100, 60), '>'), size2, layout2, map2, Pos(Point(110, 50), '^'))
    checkWrapCube(Pos(Point(100, 110), '>'), size2, layout2, map2, Pos(Point(150, 41), '<'))
    checkWrapCube(Pos(Point(50, 160), '>'), size2, layout2, map2, Pos(Point(60, 150), '^'))

    checkWrapCube(Pos(Point(51, 10), '<'), size2, layout2, map2, Pos(Point(1, 141), '>'))
    checkWrapCube(Pos(Point(51, 60), '<'), size2, layout2, map2, Pos(Point(10, 101), 'v'))
    checkWrapCube(Pos(Point(1, 110), '<'), size2, layout2, map2, Pos(Point(51, 41), '>'))
    checkWrapCube(Pos(Point(1, 160), '<'), size2, layout2, map2, Pos(Point(60, 1), 'v'))

    checkWrapCube(Pos(Point(10, 101), '^'), size2, layout2, map2, Pos(Point(51, 60), '>'))
    checkWrapCube(Pos(Point(60, 1), '^'), size2, layout2, map2, Pos(Point(1, 160), '>'))
    checkWrapCube(Pos(Point(110, 1), '^'), size2, layout2, map2, Pos(Point(10, 200), '^'))

    checkWrapCube(Pos(Point(10, 200), 'v'), size2, layout2, map2, Pos(Point(110, 1), 'v'))
    checkWrapCube(Pos(Point(60, 150), 'v'), size2, layout2, map2, Pos(Point(50, 160), '<'))
    checkWrapCube(Pos(Point(110, 50), 'v'), size2, layout2, map2, Pos(Point(100, 60), '<'))
}

private fun checkWrapCube(from: Pos, size: Int, layout: String, map: HashMap<Point, Char>, to: Pos) {

    // val fromSide = side(from.p, size)
    // val toSide = side(to.p, size)
    // println("Wrap ${from.p} $fromSide${from.d} $toSide${to.d} ${to.p}")

    val actual = wrapCube(from, size, layout)

    if (actual != to)
        throw RuntimeException("Failure: actual=$actual  expected=$to")

    if (!map.containsKey(to.p))
        throw RuntimeException("${to.p} not in map")
}
