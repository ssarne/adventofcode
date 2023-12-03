package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException
import java.util.*

fun main() {
    check(execute1(readTestLines()), 40)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 315)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private data class PV(val x: Int, val y: Int, val v: Int) : Comparable<PV> {
    override fun compareTo(other: PV): Int = this.v - other.v
}

private fun execute1(input: List<String>): Int {

    val map = IntMatrix.create(input)
    val ack = IntMatrix.create(map.width, map.height)
    djikstra(map, ack)
    return ack.get(ack.width - 1, ack.height - 1)
}

private fun execute2(input: List<String>): Int {

    val map0 = IntMatrix.create(input)
    val map = scaleMap(map0, 5, 5)
    val ack = IntMatrix.create(map.width, map.height)
    djikstra(map, ack)
    return ack.get(ack.width - 1, ack.height - 1)
}

private fun scaleMap(map0: IntMatrix, xFactor: Int, yFactor: Int): IntMatrix {
    val height = map0.height * yFactor
    val width = map0.width * xFactor
    val map = IntMatrix.create(width, height)
    for (j in 0 until yFactor) {
        for (i in 0 until xFactor) {
            for (y in 0 until map0.height) {
                for (x in 0 until map0.width) {
                    map.set(i * map0.width + x, j * map0.height + y, gv(map0.get(x, y), i, j))
                }
            }
        }
    }
    return map
}

private fun djikstra(map: IntMatrix, ack: IntMatrix) {
    var heap = PriorityQueue<PV>()
    heap.add(PV(0, 0, -map.get(0, 0)))
    while (heap.isNotEmpty()) {
        var p = heap.poll()
        if (p.x < 0 || p.x >= map.width || p.y < 0 || p.y >= map.height) continue
        if (ack.get(p.x, p.y) != 0) continue
        ack.set(p.x, p.y, p.v + map.get(p.x, p.y))
        heap.add(PV(p.x + 1, p.y, p.v + map.get(p.x, p.y)))
        heap.add(PV(p.x - 1, p.y, p.v + map.get(p.x, p.y)))
        heap.add(PV(p.x, p.y - 1, p.v + map.get(p.x, p.y)))
        heap.add(PV(p.x, p.y + 1, p.v + map.get(p.x, p.y)))
    }
}

private fun gv(v: Int, i: Int, j: Int): Int {
    var r = v + i + j
    while (r >= 10) r -= 9
    return r
}
