package aoc.aoc2017

import aoc.ktutils.*
import kotlin.math.abs

fun main() {
    check(execute1(readTestLines()), 0)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 1)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {

    var particles = HashMap<Int, Particle>()
    for (i in input.indices) particles.put(i, Particle.create(i, input[i]))

    // Only care about the particles with the lowest acceleration
    val acc = particles.values.map { it.acceleration() }.min()
    var particles2 = particles.filter { it.value.acceleration() == acc }.toMap()

    do { // Play forward until all have passed origo (0,0,0)
        var passed = true
        for (p in particles2.values) {
            p.v += p.a
            p.p += p.v

            if (p.p.manhattan() >= (p.p + p.v).manhattan()) passed = false    // not going away from origo (yet)
            if (p.v.manhattan() >= (p.v + p.a).manhattan()) passed = false    // not accelerating away from origo (yet)
        }
    } while (!passed)

    val vel = particles2.values.map { it.velocity() }.min()
    val particles3 = particles2.filter { it.value.velocity() == vel }.toMap()
    val particle = particles3.values.minBy { it.distance() }
    return particle.id
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

        for (i in input.indices) { // remove duplicates
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

    fun acceleration() = a.manhattan()
    fun velocity() = v.manhattan()
    fun distance() = p.manhattan()

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