package aoc.ktutils

import kotlin.math.abs

data class Point3D(val x: Int, val y: Int, val z: Int) : Comparable<Point3D> {
    companion object Factory {
        fun create(text: String): Point3D {
            var coords = asIntArray(text)
            return Point3D(coords[0], coords[1], coords[2])
        }
    }

    operator fun plus(that: Point3D): Point3D {
        return Point3D(this.x + that.x, this.y + that.y, this.z + that.z)
    }

    operator fun minus(that: Point3D): Point3D {
        return Point3D(this.x - that.x, this.y - that.y, this.z - that.z)
    }

    fun equals(that: Point3D): Boolean {
        return this.x == that.x && this.y == that.y && this.z == that.z
    }

    override fun compareTo(that: Point3D): Int {
        return this.manhattan() - that.manhattan()
    }

    fun manhattan(): Int {
        return manhattan(Point3D(0, 0, 0))
    }

    fun manhattan(that: Point3D): Int {
        return abs(this.x - that.x) + abs(this.y - that.y) + abs(this.z - that.z)
    }
}