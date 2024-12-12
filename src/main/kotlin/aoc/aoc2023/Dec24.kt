package aoc.aoc2023

import aoc.ktutils.*
//import com.microsoft.z3.Context
//import com.microsoft.z3.Expr
//import com.microsoft.z3.RealSort
//import com.microsoft.z3.Status

fun main() {

    check(execute1(readTestLines(1), 7.0..27.0), 2)
    execute1(readLines(), 200000000000000.0..400000000000000.0).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), "47")
    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
}


private data class HailStone(val s: Point3D, val v: Point3D) {

    fun xyIntersection(that: HailStone): Pair<Double, Double>? {
        val c1: Double = (this.v.y * this.s.x - this.v.x * this.s.y).toDouble()
        val c2: Double = (that.v.y * that.s.x - that.v.x * that.s.y).toDouble()

        val slopeDiff = this.v.y * -that.v.x - that.v.y * -this.v.x
        if (slopeDiff == 0L) return null

        val x = (c1 * -that.v.x - c2 * -this.v.x) / slopeDiff
        val y = (c2 * this.v.y - c1 * that.v.y) / slopeDiff

        val intersectsInFuture = listOf(
            (x - this.s.x < 0) == (this.v.x < 0),
            (y - this.s.y < 0) == (this.v.y < 0),
            (x - that.s.x < 0) == (that.v.x < 0),
            (y - that.s.y < 0) == (that.v.y < 0),
        ).all { it }

        return (x to y).takeIf { intersectsInFuture }
    }
}

private fun execute1(input: List<String>, area: ClosedFloatingPointRange<Double>): Int {

    val stones = parse(input)
    var count = 0
    for (i in stones.indices) {
        for (j in i + 1 until stones.size) {
            val hs1 = stones[i]
            val hs2 = stones[j]
            val intersection = hs1.xyIntersection(hs2)
            if (intersection != null)
                if (intersection.first in area && intersection.second in area)
                    count++
        }
    }
    return count
}

operator fun <T> List<T>.component6(): T = get(5)

private fun execute2(input: List<String>): String {

    val stones = parse(input)
    val result = ""

    /*
    lateinit var result: Expr<RealSort>
    val ctx = Context()
    val solver = ctx.mkSolver()
    val (x, y, z, vx, vy, vz) = listOf("x", "y", "z", "vx", "vy", "vz").map { ctx.mkRealConst(it) }

    (0..2).forEach { idx ->
        val h = stones[idx]
        val t = ctx.mkRealConst("t$idx")
        solver.add(
            ctx.mkEq(
                ctx.mkAdd(x, ctx.mkMul(vx, t)),
                ctx.mkAdd(ctx.mkReal(h.s.x), ctx.mkMul(ctx.mkReal(h.v.x), t))
            )
        )
        solver.add(
            ctx.mkEq(
                ctx.mkAdd(y, ctx.mkMul(vy, t)),
                ctx.mkAdd(ctx.mkReal(h.s.y), ctx.mkMul(ctx.mkReal(h.v.y), t))
            )
        )
        solver.add(
            ctx.mkEq(
                ctx.mkAdd(z, ctx.mkMul(vz, t)),
                ctx.mkAdd(ctx.mkReal(h.s.z), ctx.mkMul(ctx.mkReal(h.v.z), t))
            )
        )
    }

    if (solver.check() == Status.SATISFIABLE) {
        result = solver.model.eval(ctx.mkAdd(x, ctx.mkAdd(y, z)), false)
    }
    */

    return result.toString() // x + y + z
}


private fun parse(input: List<String>): List<HailStone> {
    val stones = ArrayList<HailStone>()
    for (line in input) {

        val position = Point3D.create(line.split(" @ ")[0])
        val velocity = Point3D.create(line.split(" @ ")[1])
        stones.add(HailStone(position, velocity))
    }
    return stones
}
