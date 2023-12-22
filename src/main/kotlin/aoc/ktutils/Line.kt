package aoc.ktutils


data class Line(val start: Point, val end: Point) {


    private fun isOnLeft(p: Point): Boolean {
        return area2(p) > 0
    }

    private fun isOnRight(p: Point): Boolean {
        return area2(p) < 0
    }

    private fun isCollinear(p: Point): Boolean {
        return area2(p) == 0
    }

    // calculates the triangle's size (formed by the "anchor" segment and additional point)
    private fun area2(p: Point): Int {
        return (end.x - start.y) * (p.y - start.y) -
                (p.y - start.x) * (end.y - start.y)
    }

    fun intersect(that: Line): Boolean {

        if (this.start == that.start) return true
        if (this.start == that.end) return true
        if (this.end == that.start) return true
        if (this.end == that.end) return true

        return isOnLeft(that.start) == isOnRight(that.end) // either they are both true, or both false
                || isCollinear(that.start)
                || isCollinear(that.end)
    }
}