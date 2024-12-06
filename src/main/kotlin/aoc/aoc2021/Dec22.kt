package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {
    check(execute1(readTestLines(1)), 39)
    check(execute1(readTestLines(2)), 590784)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(Box.deltas(1, 0, 2, 3), intArrayOf(0, 0, 1, 1))
    check(Box.deltas(0, 1, 2, 3), intArrayOf(0, 1, 1, 1))
    check(execute2(listOf("on x=10..12,y=10..12,z=10..12")), 27)
    check(execute2(listOf("on x=0..2,y=0..2,z=0..2", "on x=1..3,y=0..2,z=0..2")), 4 * 3 * 3)
    check(execute2(listOf("on x=0..2,y=0..2,z=0..2", "off x=1..3,y=0..2,z=0..2")), 1 * 3 * 3)
    check(execute2(listOf("on x=1..10,y=1..10,z=1..10", "off x=4..5,y=4..5,z=4..5")), 10 * 10 * 10 - 2 * 2 * 2)
    check(execute2(listOf("on x=-11..44,y=-5..40,z=-21..24", "on x=-14..15,y=7..18,z=-39..6")), 125984L)
    check(execute2(listOf("on x=-14..15,y=7..18,z=-39..6", "on x=-11..44,y=-5..40,z=-21..24")), 125984L)
    check(execute2(listOf("on x=0..2,y=0..2,z=0..2", "on x=1..3,y=1..3,z=0..2")), 3 * 3 * 3 + 1 * 2 * 3 + 1 * 2 * 3 + 1 * 1 * 3)
    check(execute2(listOf("on x=0..2,y=0..2,z=0..2", "on x=0..2,y=1..3,z=1..3")), 3 * 3 * 3 + 1 * 2 * 3 + 1 * 2 * 3 + 1 * 1 * 3)
    check(execute2(listOf("on x=0..2,y=0..2,z=0..2", "on x=1..3,y=1..3,z=1..3")), 2 * 3 * 3 * 3 - 2 * 2 * 2)
    check(execute2(readTestLines(1)), 39)
    check(execute2(readTestLines(3)), 2758514936282235L)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {
    val cubes = HashSet<Point3D>()
    for (line in input) {
        val action = line.split(" ")[0]
        val axis = line.split(" ")[1].split(",")
        val xs = axis[0].replace("x=", "").split("..")
        val ys = axis[1].replace("y=", "").split("..")
        val zs = axis[2].replace("z=", "").split("..")
        val xsp = max(-50, xs[0].toLong()) to min(50, xs[1].toLong())
        val ysp = max(-50, ys[0].toLong()) to min(50, ys[1].toLong())
        val zsp = max(-50, zs[0].toLong()) to min(50, zs[1].toLong())
        for (x in xsp.first..xsp.second) {
            for (y in ysp.first..ysp.second) {
                for (z in zsp.first..zsp.second) {
                    when (action) {
                        "on" -> cubes.add(Point3D(x, y, z))
                        "off" -> cubes.remove(Point3D(x, y, z))
                    }
                }
            }
        }
    }
    return cubes.size
}

private fun execute2(input: List<String>): Long {

    var boxes = HashSet<Box>()
    for (line in input) {
        val action = line.split(" ")[0]
        val axis = line.split(" ")[1].split(",")
        val xsp = axis[0].replace("x=", "").split("..").let { it[0].toInt() to it[1].toInt() }
        val ysp = axis[1].replace("y=", "").split("..").let { it[0].toInt() to it[1].toInt() }
        val zsp = axis[2].replace("z=", "").split("..").let { it[0].toInt() to it[1].toInt() }
        when (action) {
            "on" -> boxes = turnOn(
                Box(
                    xsp.first,
                    ysp.first,
                    zsp.first,
                    xsp.second - xsp.first + 1,
                    ysp.second - ysp.first + 1,
                    zsp.second - zsp.first + 1
                ), boxes
            )
            "off" -> boxes = turnOff(
                Box(
                    xsp.first,
                    ysp.first,
                    zsp.first,
                    xsp.second - xsp.first + 1,
                    ysp.second - ysp.first + 1,
                    zsp.second - zsp.first + 1
                ), boxes
            )
        }
    }
    return boxes.stream().mapToLong { b -> b.size() }.sum()
}

// When the box to add intersect with existing boxes
// Split it (up to 27 pieces) and queue the ones that add something
// To check if the intersect with any of the other existing cubes
private fun turnOn(inbox: Box, boxes: HashSet<Box>): HashSet<Box> {

    val queue = LinkedList<Box>()
    queue.add(inbox)
    while (queue.isNotEmpty()) {
        // if (queue.size % 100 == 50) println("boxes: ${boxes.size}  queue: ${queue.size}")
        val box = queue.remove()
        var added = false
        for (b in boxes) {
            if (box.isOverlapping3D(b)) {
                if (box.isInside(b)) {
                    added = true
                    break
                } else {
                    for (bb in box.split(b)) {
                        if (!bb.isInside(b)) {
                            if (bb.isOverlapping3D(b)) throw RuntimeException("$bb.isOverlapping3D($b) b=$b  box=$box")
                            queue.add(bb)
                        }
                    }
                }
                added = true
                break
            }
        }
        if (!added) boxes.add(box)
    }
    return boxes
}

private fun turnOff(box: Box, boxes: HashSet<Box>): HashSet<Box> {
    val result = HashSet<Box>()
    for (b in boxes) {
        if (box.isOverlapping3D(b)) {
            if (b.isInside(box)) continue
            val bb = HashSet(b.split(box))
            for (r in box.split(b)) {
                bb.remove(r)
            }
            result.addAll(bb)
        } else {
            result.add(b)
        }
    }
    return result
}

private data class Box(val x: Int, val y: Int, val z: Int, val dx: Int, val dy: Int, val dz: Int) {

    init {
        if (this.dx <= 0) throw RuntimeException("wat")
        if (this.dy <= 0) throw RuntimeException("wat")
        if (this.dz <= 0) throw RuntimeException("wat")
    }

    companion object {
        fun deltas(x0: Int, x1: Int, x2: Int, x3: Int): IntArray {
            val d1 = max(x1 - x0, 0)
            val d2 = max(min(x2, x3) - max(x0, x1), 0)
            val d3 = max(x3 - x2, 0)
            return intArrayOf(0, d1, d2, d3)
        }

        fun sum(arr: IntArray, s: Int, e: Int): Int {
            var sum = 0
            for (i in s..e) sum += arr[i]
            return sum
        }
    }

    fun size(): Long = 1L * dx * dy * dz

    fun isOverlapping1D(a1: Int, da1: Int, a2: Int, da2: Int) = a1 + da1 > a2 && a2 + da2 > a1

    fun isOverlapping3D(that: Box): Boolean {
        return isOverlapping1D(this.x, this.dx, that.x, that.dx) &&
                isOverlapping1D(this.y, this.dy, that.y, that.dy) &&
                isOverlapping1D(this.z, this.dz, that.z, that.dz)
    }

    fun isInside(that: Box): Boolean {
        return this.x >= that.x && this.x + this.dx <= that.x + that.dx &&
                this.y >= that.y && this.y + this.dy <= that.y + that.dy &&
                this.z >= that.z && this.z + this.dz <= that.z + that.dz
    }


    fun split(that: Box): List<Box> {
        val boxes = ArrayList<Box>()
        val dxs = deltas(this.x, that.x, that.x + that.dx, this.x + this.dx)  // 0,[),[],()
        val dys = deltas(this.y, that.y, that.y + that.dy, this.y + this.dy)
        val dzs = deltas(this.z, that.z, that.z + that.dz, this.z + this.dz)
        for (i in 1..3) {
            for (j in 1..3) {
                for (k in 1..3) {
                    if (dxs[i] > 0 && dys[j] > 0 && dzs[k] > 0) {
                        boxes.add(
                            Box(
                                x + sum(dxs, 0, i - 1),
                                y + sum(dys, 0, j - 1),
                                z + sum(dzs, 0, k - 1),
                                dxs[i],
                                dys[j],
                                dzs[k]
                            )
                        )
                    }
                }
            }
        }
        // for (b1 in boxes) for (b2 in boxes) if (b1 != b2) if (b1.isOverlapping3D(b2)) throw RuntimeException("$b1.isOverlapping3D($b2) this=$this  that=$that")
        return boxes
    }
}