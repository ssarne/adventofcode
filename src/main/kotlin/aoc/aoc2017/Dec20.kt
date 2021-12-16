package aoc.aoc2017

import aoc.ktutils.*
import kotlin.math.abs

fun main() {
    check(execute1(readTestLines()), 0)
    println(execute1(readLines()))

    check(execute2(readTestLines(2)), 1)
    println(execute2(readLines()))

}

private fun execute1(input: List<String>): Int {
    var particles = HashMap<Int, Particle>()
    for (i in input.indices) particles.put(i, Particle.create(i, input[i]))
    var match: Particle? = null
    do {
        for (p in particles.values) {
            p.v += p.a
            p.p += p.v
        }

        val pp = particles.values.reduce { p1, p2 -> if (p1.p.manhattan() < p2.p.manhattan()) p1 else p2 }
        val pv = particles.values.reduce { p1, p2 -> if (p1.v.manhattan() < p2.v.manhattan()) p1 else p2 }
        val pa = particles.values.reduce { p1, p2 -> if (p1.a.manhattan() < p2.a.manhattan()) p1 else p2 }
        if (pp == pv && pp == pa) match = pp
    } while (match == null)
    return match.id
}

private fun execute2(input: List<String>): Int {
    var particles = HashMap<Int, Particle>()
    for (i in input.indices) particles.put(i, Particle.create(i, input[i]))
    var iter = 0
    var changed = 0
    do {
        for (p in particles.values) {
            p.v += p.a
            p.p += p.v
        }

        for (i in 0 until input.size) { // remove duplicates
            if (!particles.containsKey(i)) continue
            val p1 = particles[i]
            for (j in i + 1 until input.size) {
                if (!particles.containsKey(j)) continue
                val p2 = particles[j]
                if (p1!!.p == p2!!.p) {
                    if (particles.containsKey(p1.id)) particles.remove(p1.id)
                    if (particles.containsKey(p2.id)) particles.remove(p2.id)
                    changed = iter
                }
            }
        }
    } while (particles.size > 1 && ++iter < changed + 100)
    return particles.size
}

private data class Particle(var id: Int, var p: Point3D, var v: Point3D, var a: Point3D) {
    companion object Factory {
        fun create(id: Int, text: String): Particle {
            var parts = text.split(", ")

            return Particle(
                id,
                Point3D.create(parts[0].replace("p=<", "").replace(">", "")),
                Point3D.create(parts[1].replace("v=<", "").replace(">", "")),
                Point3D.create(parts[2].replace("a=<", "").replace(">", ""))
            )
        }
    }
}