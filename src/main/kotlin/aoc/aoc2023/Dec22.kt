package aoc.aoc2023

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 5)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 7)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}


private fun execute1(input: List<String>): Int {

    //println("Parsing input ${input.size}")
    val blocks = parse(input)

    //println("Checking blocks ${input.size}")
    var count = 0
    for (block in blocks) {
        if (block.safeToDisintegrate(blocks))
            count++
    }
    return count
}

private fun execute2(input: List<String>): Int {

    //println("Parsing input ${input.size}")
    val blocks = parse(input)

    //println("Checking blocks ${input.size}")
    var sum = 0
    for (block in blocks) {
        val remains = blocks.toMutableSet()
        remains.remove(block)
        do {
            val affected = HashSet<Block>()
            for (b in remains) {
                if (b.canFall(remains)) {
                    affected.add(b)
                }
            }
            for (b in affected)
                remains.remove(b)
        } while (affected.size > 0)

        val potential = blocks.size - remains.size - 1
        //println("Block $block removes $potential")

        sum += potential
    }
    return sum
}

private data class Block(var p1: Point3D, var p2: Point3D) {

    var above: HashSet<Block>? = null
    var below: HashSet<Block>? = null

    fun canFall(blocks: Set<Block>): Boolean {
        if (p1.z == 1L) return false
        return below(blocks).isEmpty()
    }

    fun above(blocks: Set<Block>): Set<Block> {
        if (above == null) throw RuntimeException("Must be prepared")
        val intersection = HashSet<Block>()
        for (block in blocks)
            if (above!!.contains(block))
                intersection.add(block)
        return intersection
    }

    fun below(blocks: Set<Block>): Set<Block> {
        if (below == null) throw RuntimeException("Must be prepared")
        val intersection = HashSet<Block>()
        for (block in blocks)
            if (below!!.contains(block))
                intersection.add(block)
        return intersection
    }

    fun calculateAbove(blocks: Set<Block>) {
        val above = HashSet<Block>()
        for (that in blocks)
            if (isAbove(that))
                above.add(that)
        this.above = above
    }

    fun calculateBelow(blocks: Set<Block>) {
        val below = HashSet<Block>()
        for (that in blocks)
            if (isBelow(that))
                below.add(that)
        this.below = below
    }

    private fun isAbove(that: Block): Boolean {
        if (this.p2.z + 1 != that.p1.z) return false
        return horizontalOverlap(that)
    }

    private fun isBelow(that: Block): Boolean {
        if (this.p1.z - 1 != that.p2.z) return false
        return horizontalOverlap(that)
    }

    private fun horizontalOverlap(that: Block): Boolean {
        if (horizontalInside(Point(that.p1.x.toInt(), that.p1.y.toInt()))) return true
        if (horizontalInside(Point(that.p1.x.toInt(), that.p2.y.toInt()))) return true
        if (horizontalInside(Point(that.p2.x.toInt(), that.p1.y.toInt()))) return true
        if (horizontalInside(Point(that.p2.x.toInt(), that.p2.y.toInt()))) return true
        if (this.p1.x <= that.p1.x && this.p2.x >= that.p2.x && this.p1.y >= that.p1.y && this.p2.y <= that.p2.y) return true
        if (this.p1.x >= that.p1.x && this.p2.x <= that.p2.x && this.p1.y <= that.p1.y && this.p2.y >= that.p2.y) return true
        return false
    }

    private fun horizontalInside(point: Point): Boolean {
        return point.x >= this.p1.x && point.x <= this.p2.x
                && point.y >= this.p1.y && point.y <= this.p2.y
    }

    fun moveDown(i: Int) {
        this.p1 = Point3D(p1.x, p1.y, p1.z - i)
        this.p2 = Point3D(p2.x, p2.y, p2.z - i)
    }

    fun safeToDisintegrate(blocks: Set<Block>): Boolean {
        for (block in above(blocks))
            if (block.below(blocks).size == 1)
                return false
        return true
    }
}

private fun parse(input: List<String>): Set<Block> {
    val blocks = ArrayList<Block>()
    for (line in input) {
        val p1 = Point3D.create(line.split("~").first())
        val p2 = Point3D.create(line.split("~").last())
        blocks.add(
            Block(
                Point3D(min(p1.x, p2.x), min(p1.y, p2.y), min(p1.z, p2.z)),
                Point3D(max(p1.x, p2.x), max(p1.y, p2.y), max(p1.z, p2.z))
            )
        )
    }

    blocks.sortBy { b -> b.p1.z }

    for (block in blocks) {
        block.calculateBelow(blocks.toSet())
        while (block.canFall(blocks.toSet())) {
            block.moveDown(1)
            block.calculateBelow(blocks.toSet())
        }
    }

    for (block in blocks) block.calculateAbove(blocks.toSet())
    for (block in blocks) block.calculateBelow(blocks.toSet())

    return blocks.toSet()
}