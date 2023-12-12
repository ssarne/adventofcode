package aoc.ktutils

import kotlin.math.abs

data class Point(val x: Int, val y: Int) {

    companion object Factory {
        fun create(text: String): Point {
            return Point(text.split(",")[0].toInt(), text.split(",")[1].toInt())
        }
    }

    operator fun plus(that: Point) = Point(this.x + that.x, this.y + that.y)
    operator fun minus(that: Point) = Point(this.x - that.x, this.y - that.y)
    operator fun times(scale: Int) = Point(this.x * scale, this.y * scale)

    /** Calculate the Manhattan distance, delta-x + delta-y, between the two points */
    infix fun manhattan(other: Point) = abs(this.x - other.x) + abs(this.y - other.y)

    /** Enumerate the 4 directly adjacent coordinates to this Point */
    fun adjacent(): List<Point> {
        return listOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x, y - 1)
        )
    }

    /** Enumerate the 8 surrounding coordinates to this Point */
    fun surrounding(): List<Point> {
        val list = ArrayList<Point>(8)
        for (xi in -1..1)
            for (yi in -1..1)
                if (xi != 0 || yi != 0)
                    list.add(Point(x + xi, y + yi))
        return list
    }
}
