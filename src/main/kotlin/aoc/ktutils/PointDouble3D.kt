package aoc.ktutils

import kotlin.math.abs

data class PointDouble3D(val x: Double, val y: Double, val z: Double) {

    fun move(time: Double, velocity: PointDouble3D) =
        PointDouble3D(x + time * velocity.x, y + time * velocity.y, z + time * velocity.z)

    operator fun minus(that: PointDouble3D): PointDouble3D {
        return PointDouble3D(this.x - that.x, this.y - that.y, this.z - that.z)
    }

    operator fun plus(that: PointDouble3D): PointDouble3D {
        return PointDouble3D(this.x + that.x, this.y + that.y, this.z + that.z)
    }

    fun manhattan(that: PointDouble3D) = abs(this.x - that.x) + abs(this.y - that.y) + abs(this.z - that.z)

    override fun toString() = "<$x,$y,$z>"

    companion object Factory {
        fun create(text: String): PointDouble3D {
            val coordinates = asLongArray(text)
            return PointDouble3D(coordinates[0].toDouble(), coordinates[1].toDouble(), coordinates[2].toDouble())
        }
    }
}