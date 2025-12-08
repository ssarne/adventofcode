package aoc.aoc2025

import aoc.ktutils.*

fun main() {

    execute1(readTestLines(1), 10).let { check(it, 40L) ; println("Test: $it") }
    execute1(readLines(), 1000).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines(1)).let { check(it, 25272L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>, connections: Int): Long {

    val boxes = ArrayList<Point3D>()
    val circuits = HashMap<String, HashSet<Point3D>>()
    val boxToC = HashMap<Point3D, String>() // box -> circuit
    val pairs = HashSet<String>() // box -> circuit

    for (line in input) {
        val split = line.split(",")
        val point = Point3D(split[0].toLong(), split[1].toLong(), split[2].toLong())
        val circuitId = point.toString()
        val circuit = HashSet<Point3D>()
        boxes.add(point)
        circuit.add(point)
        boxToC.put(point, circuitId)
        circuits.put(circuitId, circuit)
    }

    repeat(connections) {
        var from = Point3D(0, 0, 0)
        var to = Point3D(0, 0, 0)
        var dist = Double.MAX_VALUE
        for (p1 in boxes) {
            for (p2 in boxes) {
                if (p1 == p2) continue
                if (pairs.contains("$p1 $p2")) continue
                if (dist > p1.euclideanDistance(p2)) {
                    from = p1
                    to = p2
                    dist = p1.euclideanDistance(p2)
                }
            }
        }

        pairs.add("$from $to")
        pairs.add("$to $from")

        if (boxToC[from] != boxToC[to]) {
            val toCircuitId = boxToC[to]!!
            val toCircuit = circuits[toCircuitId]!!
            val fromCircuitId = boxToC[from]!!
            val fromCircuit = circuits[fromCircuitId]!!
            toCircuit.addAll(fromCircuit)
            circuits.remove(fromCircuitId)
            for (p in fromCircuit) boxToC[p] = toCircuitId
        }

        // println("Connecting $from $to  ${circuits.size}")
        if (circuits.size == 1) return from.x * to.x
    }

    val cs = circuits.values.map { it -> it.size }.toMutableList().sortedDescending()

    return 1L * cs[0] * cs[1] * cs[2]
}

private fun execute2(input: List<String>): Long {

    val boxes = ArrayList<Point3D>()
    val circuits = HashMap<String, HashSet<Point3D>>()
    val boxToC = HashMap<Point3D, String>() // box -> circuit
    val pairs = HashSet<String>() // box -> circuit

    for (line in input) {
        val split = line.split(",")
        val point = Point3D(split[0].toLong(), split[1].toLong(), split[2].toLong())
        val circuitId = point.toString()
        val circuit = HashSet<Point3D>()
        boxes.add(point)
        circuit.add(point)
        boxToC.put(point, circuitId)
        circuits.put(circuitId, circuit)
    }

    while (circuits.size > 1) {
        var from = Point3D(0, 0, 0)
        var to = Point3D(0, 0, 0)
        var dist = Double.MAX_VALUE
        for (p1 in boxes) {
            for (p2 in boxes) {
                if (p1 == p2) continue
                if (pairs.contains("$p1 $p2")) continue
                if (dist > p1.euclideanDistance(p2)) {
                    from = p1
                    to = p2
                    dist = p1.euclideanDistance(p2)
                }
            }
        }

        pairs.add("$from $to")
        pairs.add("$to $from")

        if (boxToC[from] != boxToC[to]) {
            val toCircuitId = boxToC[to]!!
            val toCircuit = circuits[toCircuitId]!!
            val fromCircuitId = boxToC[from]!!
            val fromCircuit = circuits[fromCircuitId]!!
            toCircuit.addAll(fromCircuit)
            circuits.remove(fromCircuitId)
            for (p in fromCircuit) boxToC[p] = toCircuitId
        }

        // println("Connecting $from $to  ${circuits.size}")
        if (circuits.size == 1) return from.x * to.x
    }

    val cs = circuits.values.map { it -> it.size }.toMutableList().sortedDescending()

    return 1L * cs[0] * cs[1] * cs[2]
}