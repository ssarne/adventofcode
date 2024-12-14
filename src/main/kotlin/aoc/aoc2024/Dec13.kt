package aoc.aoc2024

import aoc.ktutils.*
import java.math.BigInteger
import java.math.BigInteger.ZERO

fun main() {
    check(execute1(readTestLines(1)), 480L)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), "875318608908")
    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
}

private data class Machine(val a: PointLong, val b: PointLong, val p: PointLong)

private fun execute1(input: List<String>): Long {

    val machines = parse(input)

    var sum = 0L
    for (m in machines) {
        val best = combo(m)
        sum += best.second
    }

    return sum
}

private fun combo(m: Machine): Pair<Pair<Long, Long>, Long> {
    var best = (0L to 0L) to 0L
    for (a in 0..m.p.x / m.a.x) {
        for (b in 0..m.p.x / m.b.x) {
            if (a * m.a.x + b * m.b.x == m.p.x && a * m.a.y + b * m.b.y == m.p.y) {
                if (best.second == 0L || best.second > 3 * a + b) {
                    best = (a to b) to 3 * a + b
                }
            }
        }
    }
    return best
}

private fun execute2(input: List<String>): String {

    val machines = parse(input, 10000000000000L)

    var sum = ZERO
    for (m in machines) {
        val res = solve(m)
        sum += res.second
    }
    return sum.toString()
}

private fun solve(m: Machine): Pair<Pair<BigInteger, BigInteger>, BigInteger> {

    // ax * a + bx * b = px
    // ay * a + by * b = py
    //
    // a = (px - bx * b) / ax = px / ax - b * bx / ax
    //
    // ay * a + by * b = py
    // ay * (px / ax - b * bx / ax) + by * b = py
    // ay * px / ax - b * ay * bx / ax + by * b = py
    // b * by - b * ay * bx / ax = py - ay * px / ax
    // b * (by - ay * bx / ax) = py - ay * px / ax
    // b = (py - ay * px / ax) / (by - ay * bx / ax)

    val ax = m.a.x.toBigD()
    val ay = m.a.y.toBigD()
    val bx = m.b.x.toBigD()
    val by = m.b.y.toBigD()
    val px = m.p.x.toBigD()
    val py = m.p.y.toBigD()

    // b = (py - ay * px / ax) / (by - ay * bx / ax)
    // b = (py - f1) / (by - f2)
    // b = t1 / t2
    val f1 = ay * px / ax
    val t1 = py - f1
    val f2 = ay * bx / ax
    val t2 = by - f2
    val b = t1 / t2

    // a = (px - bx * b) / ax
    // a = t3 / ax
    val t3 = px - (bx * b)
    val a = t3 / ax

    val axl = ax.toBigI()
    val ayl = ay.toBigI()
    val bxl = bx.toBigI()
    val byl = by.toBigI()
    val pxl = px.toBigI()
    val pyl = py.toBigI()
    val al = a.toBigI()
    val bl = b.toBigI()

    if (axl * al + bxl * bl == pxl)
        if (ayl * al + byl * bl == pyl)
            return (al to bl) to al * BigInteger.valueOf(3) + bl

    return (ZERO to ZERO) to ZERO

}

private fun parse(input: List<String>, offset: Long = 0L): MutableList<Machine> {
    val machines = mutableListOf<Machine>()
    for (lines in asChunks(input)) {
        val p1 = parseLine(lines[0], "X+", "Y+")
        val p2 = parseLine(lines[1], "X+", "Y+")
        val p3 = parseLine(lines[2], "X=", "Y=")
        machines.add(Machine(p1, p2, PointLong(p3.x + offset, p3.y + offset)))
    }
    return machines
}

private fun parseLine(line: String, s1: String, s2: String): PointLong {
    val x = line.substring(line.indexOf(s1) + 2, line.indexOf(",")).toLong()
    val y = line.substring(line.indexOf(s2) + 2).toLong()
    return PointLong(x, y)
}
