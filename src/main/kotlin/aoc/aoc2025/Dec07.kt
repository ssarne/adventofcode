package aoc.aoc2025


import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    execute1(readTestLines(1)).let { println("Test:   $it") ; check(it, 21L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { println("Test:   $it") ; check(it, 40L) }
    execute2(readLines()).let { println("Result: $it") ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val grid = parseCharacterGridToMap(input)
    val start = grid.entries.find {it.value == 'S'}!!.key
    val positions = mutableListOf(start)
    var splits = 0L

    while (positions.isNotEmpty()) {
        val pos = positions.removeFirst()
        val s = Point(pos.x, pos.y+1)
        val se = Point(pos.x+1, pos.y+1)
        val sw = Point(pos.x-1, pos.y+1)

        if (grid[s] == '.') {
            grid[s] = '|'
            positions.add(s)
        }
        else if (grid[s] == '^') {
            splits++
            if (grid[se] == '.') {
                grid[se] = '|'
                positions.add(se)
            }
            if (grid[sw] == '.') {
                grid[sw] = '|'
                positions.add(sw)
            }
        }
    }
    return splits
}

private fun execute2(input: List<String>): Long {

    val grid = parseCharacterGridToMap(input)
    val start = grid.entries.find {it.value == 'S'}!!.key
    val timelines = HashMap<Point, Long>()

    return getTimelines(grid, start, timelines)
}

fun getTimelines(grid: HashMap<Point, Char>, pos: Point, timelines: HashMap<Point, Long>): Long {

    if (timelines.containsKey(pos))
        return timelines[pos]!!

    val s = Point(pos.x, pos.y + 1)
    if (!grid.containsKey(s))
        return 1L

    if (grid[s] == '.' || grid[s] == '|') {
        grid[s] = '|'
        val result = getTimelines(grid, s, timelines)
        timelines[pos] = result
        return result
    }

    val se = Point(pos.x+1, pos.y+1)
    val sw = Point(pos.x-1, pos.y+1)

    if (grid[s] == '^') {
        var result = 0L
        if (grid[se] == '.' || grid[se] == '|') {
            grid[se] = '|'
            result += getTimelines(grid, se, timelines)
        }
        if (grid[sw] == '.' || grid[sw] == '|') {
            grid[sw] = '|'
            result += getTimelines(grid, sw, timelines)
        }
        timelines[pos] = result
        return result
    }
    return 0L
}
