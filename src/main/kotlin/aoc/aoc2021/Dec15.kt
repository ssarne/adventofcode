package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException
import java.util.*

fun main() {
    check(execute1(readTestLines()), 40)
    execute1(readLines()).let { println(it); check(it, 717) }

    check(execute2(readTestLines()), 315)
    execute2(readLines()).let { println(it); check(it, 2993) }
}

private data class PV(val x: Int, val y: Int, val v: Int) : Comparable<PV> {
    override fun compareTo(other: PV): Int = this.v - other.v
}

private fun execute1(input: List<String>): Int {

    val (map, width, height) = createIntMatrixFromDigits(input)
    val ack = Array(width) { IntArray(height) }
    djikstra(map, width, height, ack)
    return ack[width - 1][height - 1]
}

private fun execute2(input: List<String>): Int {

    val (map0, _, _) = createIntMatrixFromDigits(input)
    val (map, width, height) = scaleMap(map0, 5, 5)
    val ack = Array(width) { IntArray(height) }
    djikstra(map, width, height, ack)
    return ack[width - 1][height - 1]
}

private fun scaleMap(map0: Array<IntArray>, xFactor: Int, yFactor: Int): Triple<Array<IntArray>, Int, Int> {
    val h0 = map0.size
    val w0 = map0[0].size
    val height = h0 * yFactor
    val width = w0 * xFactor
    val map = Array(width) { IntArray(height) }
    for (j in 0 until yFactor) {
        for (i in 0 until xFactor) {
            for (y in map0.indices) {
                for (x in map0[y].indices) {
                    map[i * w0 + x][j * h0 + y] = gv(map0[x][y], i, j)
                }
            }
        }
    }
    return Triple(map, width, height)
}

private fun djikstra(map: Array<IntArray>, width: Int, height: Int, ack: Array<IntArray>) {
    var heap = PriorityQueue<PV>()
    heap.add(PV(0, 0, -map[0][0]))
    while (heap.isNotEmpty()) {
        var p = heap.poll()
        if (p.x < 0 || p.x >= width || p.y < 0 || p.y >= height) continue
        if (ack[p.x][p.y] != 0) continue
        ack[p.x][p.y] = p.v + map[p.x][p.y]
        heap.add(PV(p.x + 1, p.y, p.v + map[p.x][p.y]))
        heap.add(PV(p.x - 1, p.y, p.v + map[p.x][p.y]))
        heap.add(PV(p.x, p.y - 1, p.v + map[p.x][p.y]))
        heap.add(PV(p.x, p.y + 1, p.v + map[p.x][p.y]))
    }
}

private fun gv(v: Int, i: Int, j: Int): Int {
    var r = v + i + j
    while (r >= 10) r -= 9
    return r
}
