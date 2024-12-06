package aoc.ktutils

import kotlin.math.abs

data class PointLong(val x: Long, val y: Long) {

    companion object Factory {
        fun create(text: String): PointLong {
            return PointLong(text.split(",")[0].toLong(), text.split(",")[1].toLong())
        }
    }

    operator fun plus(that: PointLong) = PointLong(this.x + that.x, this.y + that.y)
    operator fun minus(that: PointLong) = PointLong(this.x - that.x, this.y - that.y)
    operator fun times(scale: Long) = PointLong(this.x * scale, this.y * scale)

    /** Calculate the Manhattan distance, delta-x + delta-y, between the two points */
    infix fun manhattan(other: PointLong) = abs(this.x - other.x) + abs(this.y - other.y)

    override fun toString() = "<$x,$y>"

    /** Enumerate the 4 directly adjacent coordinates to this Point */
    fun adjacent(): List<PointLong> {
        return listOf(
            PointLong(x + 1, y),
            PointLong(x - 1, y),
            PointLong(x, y + 1),
            PointLong(x, y - 1)
        )
    }

    /** Enumerate the 8 surrounding coordinates to this Point */
    fun surrounding(): List<PointLong> {
        val list = ArrayList<PointLong>(8)
        for (xi in -1..1)
            for (yi in -1..1)
                if (xi != 0 || yi != 0)
                    list.add(PointLong(x + xi, y + yi))
        return list
    }
}
