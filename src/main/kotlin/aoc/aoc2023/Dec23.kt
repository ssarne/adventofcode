package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 94)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 154)
    val startTime = System.currentTimeMillis()
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
    val execTime = System.currentTimeMillis() - startTime
    println("time: $execTime ms")

}

private fun execute1(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val start = Pos(Point(1, 0), 'v')
    map[start.p] = 'O'
    return findLongestPath(start.move(), 1, map)
}

private fun execute2(input: List<String>): Int {
    val map = parseCharacterGridToMap(input)
    val size = mapSize(map)

    // Remove direction restrictions from map in part 2
    for (x in 1 until size.second.x)
        for (y in 1 until size.second.y)
            if (">v<^".contains(map[Point(x, y)]!!)) map[Point(x, y)] = '.'

    val maze = buildGraph(Pos(Point(1, 0), 'v'), map)
    return findLongestPath(maze[Point(1, 0)]!!, 0, maze[Point(size.second.x - 1, size.second.y)]!!)
}

fun findLongestPath(pos: Pos, steps: Int, map: HashMap<Point, Char>): Int {

    var best = 0
    for (next in setOf(pos.left().move(), pos.move(), pos.right().move())) {
        val c = map[next.p]
        map[pos.p] = 'O'
        // printSparseMatrix(map, true, "steps=$steps c=$c next=$next")
        if (c == null) best = steps
        if (c == '.' || c == next.dir) {
            val candidate = findLongestPath(next, steps + 1, map)
            if (candidate > best) best = candidate
            map[next.p] = c
        }
    }
    return best
}


// Cache crossing with two possible paths (will not need
private fun findLongestPath(current: Node, steps: Int, target: Node): Int {

    if (current == target)
        return steps

    current.visited = true
    var best = 0
    for (edge in current.edges) {
        if (edge.to.visited) continue
        val candidate = findLongestPath(edge.to, steps + edge.cost, target)
        if (candidate > best) best = candidate
    }
    current.visited = false
    return best
}

private fun buildGraph(pos: Pos, map: HashMap<Point, Char>): HashMap<Point, Node> {

    val nodes = HashMap<Point, Node>()
    val size = mapSize(map)

    // Remove the direction constraints, does not apply in part 2
    for (x in 1 until size.second.x)
        for (y in 1 until size.second.y)
            if (">v<^".contains(map[Point(x, y)]!!)) map[Point(x, y)] = '.'

    // Create nodes for all crossings
    val start = Point(1, 0)
    val end = Point(size.second.x - 1, size.second.y)
    nodes[start] = Node("start", ArrayList())
    nodes[end] = Node("end", ArrayList())
    for (x in 1 until size.second.x) {
        for (y in 1 until size.second.y) {
            Point(x, y).let {
                if (it.adjacent().count() {map[it] == '.'} > 2)
                    nodes[it] = Node(it.toString(), ArrayList())
            }
        }
    }

    // Create weighted edges for the paths between crossings
    for ((point, node) in nodes) {
        for (np in point.adjacent()) {
            if (map[np] == '.') {
                val dir = if (np.x < point.x) '<' else if (np.x > point.x) '>' else if (np.y < point.y) '^' else 'v'
                val (next, dist) = findNode(Pos(np, dir), 1, nodes, map)
                node.edges.add(Edge(node.name + "->" + next.name, dist, node, next))
            }
        }
    }
    return nodes
}

private fun findNode(pos: Pos, steps: Int, nodes: HashMap<Point, Node>, map: HashMap<Point, Char>): Pair<Node, Int> {
    if (nodes.containsKey(pos.p))
        return nodes[pos.p]!! to steps

    for (next in setOf(pos.left().move(), pos.move(), pos.right().move())) {
        if (map[next.p] == '.')
            return findNode(next, steps+1, nodes, map)
    }
    throw RuntimeException("CMH $pos")
}