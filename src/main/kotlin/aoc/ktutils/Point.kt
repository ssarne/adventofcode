package aoc.ktutils

data class Point(val x: Int, val y: Int) {
    companion object Factory {
        fun create(text: String): Point {
            return Point(text.split(",")[0].toInt(), text.split(",")[1].toInt())
        }
    }

    operator fun plus(that: Point): Point {
        return Point(this.x + that.x, this.y + that.y)
    }

    operator fun minus(that: Point): Point {
        return Point(this.x - that.x, this.y - that.y)
    }

    operator fun times(scale: Int): Point {
        return Point(this.x * scale, this.y * scale)
    }

    fun manhattan(other: Point): Int {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y)
    }
}
