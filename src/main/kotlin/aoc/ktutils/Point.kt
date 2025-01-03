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

    override fun toString() = "<$x,$y>"

    /** Enumerate the 4 directly adjacent coordinates to this Point */
    fun adjacent(): List<Point> {
        return listOf(
            Point(x + 1, y),
            Point(x - 1, y),
            Point(x, y + 1),
            Point(x, y - 1)
        )
    }

    fun north() = this + Point(0, -1)
    fun south() = this + Point(0, 1)
    fun west() = this + Point(-1, 0)
    fun east() = this + Point(1, 0)
    fun nw() = this + Point(-1, -1)
    fun ne() = this + Point(1, -1)
    fun sw() = this + Point(-1, 1)
    fun se() = this + Point(1, 1)

    /** Enumerate the 8 surrounding coordinates to this Point */
    fun surrounding(): List<Point> {
        val list = ArrayList<Point>(8)
        for (xi in -1..1)
            for (yi in -1..1)
                if (xi != 0 || yi != 0)
                    list.add(Point(x + xi, y + yi))
        return list
    }

    infix fun inside(square: Pair<Point, Point>): Boolean {
        return this.x in square.first.x .. square.second.x
                && this.y in square.first.y .. square.second.y
    }

    fun withinManhattanDistance(distance: Int): List<Point> {
        val list = ArrayList<Point>(8)
        for (x in this.x - distance..this.x + distance)
            for (y in this.y - distance..this.y + distance)
                Point(x, y).let {
                    if (distance >= this.manhattan(it))
                        list.add(it)
                }
        return list
    }
}
