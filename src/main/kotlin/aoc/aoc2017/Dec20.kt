package aoc.aoc2017

import aoc.ktutils.*

fun main() {
    check(execute(readTestLines()), 1)
    println(execute(readLines())) //

}

private fun execute(input: List<String>): Int {
    var particles = HashMap<Int, Particle>()
    for (i in input.indices) particles.put(i, Particle.create(input[i]))
    var match: Particle? = null
    do {
        for (particle in particles) {
//            particle.v += particle.a
//            particle.p += particle.v
        }
    } while (match == null)
    return 0
}

private data class Particle(var p: Point3D, var v: Point3D, var a: Point3D) {
    companion object Factory {
        fun create(text: String): Particle {
            var parts = text.split(", ")

            return Particle(
                Point3D.create(parts[0].replace("p=<", "").replace(">", "")),
                Point3D.create(parts[1].replace("v=<", "").replace(">", "")),
                Point3D.create(parts[2].replace("a=<", "").replace(">", ""))
            )
        }
    }
}


private data class Point3D(val x: Int, val y: Int, val z: Int) {
    companion object Factory {
        fun create(text: String): Point3D {
            var coords = asIntArray(text)
            return Point3D(coords[0], coords[1], coords[2])
        }
    }

    operator fun plus(that: Point3D): Point3D {
        return Point3D(this.x + that.x, this.y + that.y, this.z + that.z)
    }
}