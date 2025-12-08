package aoc.aoc2025

import aoc.ktutils.*

fun main() {
    val test = execute(readTestLines(1), 10)
    val result = execute(readLines(), 1000)

    check(test.first, 40L)
    check(result.first, readAnswerAsLong(1))

    check(test.second, 25272L)
    check(result.second, readAnswerAsLong(2))

    println("Test: $test")
    println("Result: $result")
}

private fun execute(input: List<String>, steps: Int): Pair<Long, Long> {

    val boxes = ArrayList<Point3D>()
    val boxToC = HashMap<Point3D, String>() // box -> circuit label
    val circuits = HashMap<String, Int>()
    val connections = ArrayList<Triple<Point3D, Point3D, Double>>()   // box1, box2, distance
    var part1 = 0L
    var part2 = 0L

    for (line in input) {
        val split = line.split(",")
        val point = Point3D(split[0].toLong(), split[1].toLong(), split[2].toLong())
        val circuitId = point.toString()
        boxes.add(point)
        boxToC.put(point, circuitId)
        circuits.put(circuitId, 1)
    }

    for (i in 0 until boxes.size - 1)
        for (j in i + 1 until boxes.size)
            connections.add(Triple(boxes[i], boxes[j], boxes[i].euclideanDistance(boxes[j])))

    connections.sortWith(compareBy({it.third}))

    for (i in generateSequence(0) { it + 1 }.takeWhile { circuits.size > 1 }) {

        val connection = connections[i]

        if (boxToC[connection.first] != boxToC[connection.second]) {
            val toCircuitId = boxToC[connection.first]!!
            val fromCircuitId = boxToC[connection.second]!!
            for (e in boxToC)
                if (e.value == fromCircuitId)
                    boxToC[e.key] = toCircuitId

            circuits[toCircuitId] = circuits[toCircuitId]!! + circuits[fromCircuitId]!!
            circuits.remove(fromCircuitId)
        }

        if (i+1 == steps) {
            val cs = circuits.values.toMutableList().sortedDescending()
            part1 = 1L * cs[0] * cs[1] * cs[2]
        }

        if (circuits.size == 1) {
            part2 = connections[i].first.x * connections[i].second.x
        }
    }

    return part1 to part2
}