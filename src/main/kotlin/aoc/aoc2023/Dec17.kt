package aoc.aoc2023

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 102)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    //check(execute2(readTestLines(1)), 94)
    //execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    return execute(input, 0, 3)
}

private fun execute2(input: List<String>): Int {
    return execute(input, 4, 10)
}

private fun execute(input: List<String>, min: Int, max: Int): Int {

    val map = parseCharacterGridToMap(input)
    val size = mapSize(map)
    val visited = HashMap<Pos, Int>()
    visited[Pos(size.first, '>')] = 0
    visited[Pos(size.first, 'v')] = 0
    val runners = HashMap(visited)

    while (true) {
        var best: Pair<Pos, Int>? = null
        var prev: Pos? = null

        for (pos in runners.keys) {
            // Find positions in reach from this position
            // Include the range of positions from going straight
            // Therefore always assume turn when reaching a position
            if (best != null && runners[pos]!! > best.second) continue
            val candidates = mutableMapOf<Pos, Int>()
            for (turn in setOf(pos.left(), pos.right())) {
                var heat = runners[pos]!!
                for (i in 1..max) {
                    val next = turn.move(i)
                    if (!map.contains(next.p)) break
                    heat += asInt(map[next.p]!!)
                    if (visited.contains(next)) continue
                    if (i < min) continue
                    candidates[next] = heat
                }
            }

            for (next in candidates)
                if (best == null || best.second > next.value) {
                    best = next.key to next.value
                    prev = pos
                }
        }
        if (best == null) throw RuntimeException("CMH")

        println("[" + visited.size + "] best=$best")
        visited[best.first] = best.second
        runners[best.first] = best.second
        runners.remove(prev)
        if (best.first.p == size.second) return best.second
    }
}