package aoc.ktutils

data class PointDouble(val x: Double, val y: Double) {

    companion object Factory {
        fun create(text: String): PointDouble {
            return PointDouble(text.split(",")[0].toDouble(), text.split(",")[1].toDouble())
        }
    }

    operator fun plus(that: PointDouble) = PointDouble(this.x + that.x, this.y + that.y)
    operator fun minus(that: PointDouble) = PointDouble(this.x - that.x, this.y - that.y)
    operator fun times(scale: Double) = PointDouble(this.x * scale, this.y * scale)

    override fun toString() = "<$x,$y>"
}