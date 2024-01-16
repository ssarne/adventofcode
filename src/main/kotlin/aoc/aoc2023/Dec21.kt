package aoc.aoc2023


import aoc.ktutils.*
import java.util.BitSet

fun main() {
    check(execute1(readTestLines(1), 6), 16)
    check(execute1(readTestLines(1), 10), 33)
    check(execute1(readTestLines(2), 10), 50)
    check(execute1(readTestLines(3), 5), 26)
    check(execute1(readTestLines(4), 10), 90)
    check(execute1(readTestLines(4), 15), 192)
    execute1(readLines(), 64).let { println(it); check(it, readAnswerAsLong(1)) }

    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>, steps: Int): Long {
    val map = parseCharacterGridToMap(input)
    val start = map.entries.find { e -> e.value == 'S' }!!.key
    map[start] = '.' // clean starting point away
    return countPositions(map, start, steps)
}


fun countPositions(input: HashMap<Point, Char>, start: Point, steps: Int): Long {
    var current = input.toMutableMap()
    current[start] = 'O'
    for (i in 1..steps) {
        val next = HashMap<Point, Char>()
        for (e in current.entries) {
            val p = e.key
            when (current[e.key]) {
                '#' -> next[p] = '#'
                '.' -> if (!next.contains(p)) next[p] = '.'
                'O' -> {
                    for (a in p.adjacent())
                        if (current[a] != null && current[a] != '#')
                            next[a] = 'O'
                    next[p] = '.'
                }
            }
        }
        current = next
    }
    return current.count { e -> e.value == 'O' }.toLong()
}

// Calculate for a large number of steps with an infinite map (repeated)
// Following the guide in https://www.youtube.com/watch?v=9UOMZSL0JTg&t=450s
// Use real input only, which is simpler in a few ways, with properties like
//  - the map is a square
//  - starting position is in the center of the map
//  - free path vertically and horizontally to edge of map
//  - free path along the border all the way around
//  - the map is sparse, all parts are reached within manhattan distance
//  - the map size is odd
//  - the edge of map is just reached after given steps
// So the steps will create a diamond of maps
private fun execute2(input: List<String>): Long {

    val map = parseCharacterGridToMap(input)
    val start = map.entries.find { e -> e.value == 'S' }!!.key
    val mapSize = mapSize(map)
    map[start] = '.' // clean starting point away

    val steps = 26501365L
    val size = mapSize.second.x - mapSize.first.x + 1

    check(mapSize.second.y - mapSize.first.y + 1, mapSize.second.x - mapSize.first.x + 1) // the map is a square
    check(start.x, size / 2) // starting position is in the center of the map
    check(start.y, size / 2) // starting position is in the center of the map
    check(steps % size, 1L * size / 2) // the edge of the map is just reached after given steps

    val gridWidth = steps / size - 1
    val oddGrids = square(gridWidth / 2 * 2 + 1)
    val evenGrids = square((gridWidth + 1) / 2 * 2)
    val odd = countPositions(map, start, size * 2 + 1)
    val even = countPositions(map, start, size * 2)

    val topNorth = countPositions(map, Point(start.x, size - 1), size - 1)
    val topSouth = countPositions(map, Point(start.x, 0), size - 1)
    val topWest = countPositions(map, Point(size - 1, start.y), size - 1)
    val topEast = countPositions(map, Point(0, start.y), size - 1)

    val smallNE = countPositions(map, Point(0, size - 1), size / 2 - 1)
    val smallNW = countPositions(map, Point(size - 1, size - 1), size / 2 - 1)
    val smallSE = countPositions(map, Point(0, 0), size / 2 - 1)
    val smallSW = countPositions(map, Point(size - 1, 0), size / 2 - 1)

    val largeNE = countPositions(map, Point(0, size - 1), 3 * size / 2 - 1)
    val largeNW = countPositions(map, Point(size - 1, size - 1), 3 * size / 2 - 1)
    val largeSE = countPositions(map, Point(0, 0), 3 * size / 2 - 1)
    val largeSW = countPositions(map, Point(size - 1, 0), 3 * size / 2 - 1)

    return oddGrids * odd +
            evenGrids * even +
            topNorth + topSouth + topWest + topEast +
            (gridWidth + 1) * (smallNE + smallNW + smallSE + smallSW) +
            gridWidth * (largeNE + largeNW + largeSE + largeSW)
}