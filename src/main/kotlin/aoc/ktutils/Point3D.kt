package aoc.ktutils

import java.lang.RuntimeException
import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) : Comparable<Point3D> {

    operator fun plus(that: Point3D): Point3D {
        return Point3D(this.x + that.x, this.y + that.y, this.z + that.z)
    }

    operator fun minus(that: Point3D): Point3D {
        return Point3D(this.x - that.x, this.y - that.y, this.z - that.z)
    }

    fun equals(that: Point3D): Boolean {
        return this.x == that.x && this.y == that.y && this.z == that.z
    }

    fun manhattan(): Int {
        return manhattan(Point3D(0, 0, 0))
    }

    fun manhattan(that: Point3D): Int {
        return abs(this.x - that.x) + abs(this.y - that.y) + abs(this.z - that.z)
    }

    override fun toString() = "<$x,$y,$z>"

    override fun compareTo(that: Point3D): Int {
        if (this.x != that.x) return this.x - that.x
        if (this.y != that.y) return this.y - that.y
        return this.z - that.z
    }

    // Rotation matrix's for counterclockwise in 3D, S for sinus, C for co-sinus
    // sin(90)=1, cos(90)=0, sin(0)=0, cos(0)=1
    //
    //        [ 1       ]       [ C     S ]       [ C -S    ]
    //   R(x)=[    C -S ]  R(y)=[    1    ]  R(z)=[ S  C    ]
    //        [    S  C ]       [-S     C ]       [       1 ]
    //
    fun rotate(rot: String): Point3D {
        return when (rot) {
            "N0" -> return Point3D(x, y, z)
            "N1" -> return Point3D(x, z, -y)
            "N2" -> return Point3D(x, -y, -z)
            "N3" -> return Point3D(x, -z, y)

            "W0" -> return Point3D(y, -x, z)
            "W1" -> return Point3D(z, -x, -y)
            "W2" -> return Point3D(-y, -x, -z)
            "W3" -> return Point3D(-z, -x, y)

            "S0" -> return Point3D(-x, -y, z)
            "S1" -> return Point3D(-x, -z, -y)
            "S2" -> return Point3D(-x, y, -z)
            "S3" -> return Point3D(-x, z, y)

            "E0" -> return Point3D(-y, x, z)
            "E1" -> return Point3D(-z, x, -y)
            "E2" -> return Point3D(y, x, -z)
            "E3" -> return Point3D(z, x, y)

            "U0" -> return Point3D(-z, y, x)
            "U1" -> return Point3D(-y, -z, x)
            "U2" -> return Point3D(z, -y, x)
            "U3" -> return Point3D(y, z, x)

            "D0" -> return Point3D(z, y, -x)
            "D1" -> return Point3D(-y, z, -x)
            "D2" -> return Point3D(-z, -y, -x)
            "D3" -> return Point3D(y, -z, -x)

            else -> throw RuntimeException("Unsupported rotation: $rot")
        }
    }

    companion object Factory {
        fun create(text: String): Point3D {
            var coords = asIntArray(text)
            return Point3D(coords[0], coords[1], coords[2])
        }

        fun rotations(): List<String> { // North, East, South, West, Down, Up x 0, 1, 2, 3
            val rotations = ArrayList<String>()
            for (face in "NWSEDU")
                for (r in 0..3)
                    rotations.add("" + face + r)
            return rotations
        }
    }
}

private fun testRotation() {
    var results = HashSet<Point3D>()
    val p = Point3D(2, 3, 5)
    for (rot in Point3D.rotations()) results.add(p.rotate(rot))
    check(results.size, 24)
}

fun main() {
    testRotation()
}