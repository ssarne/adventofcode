package aoc.ktutils


data class LineDouble(val start: PointDouble, val end: PointDouble) {

    override fun toString() = "($start->$end)"

    // Returns -1, 0 or 1 to indicate position of the point p relative to the line
    private fun side(p: PointDouble): Int {
        val d = (p.y - start.y) * (end.x - start.x) - (end.y - start.y) * (p.x - start.x)
        return if (d > 0) 1 else if (d < 0) -1 else 0
    }

    // Returns True if c is inside closed segment, False otherwise.
    private fun isPointInsideClosedSegment(p: PointDouble): Boolean {
        if (start.x < end.x)
            return start.x <= p.x && p.x <= end.x

        if (end.x < start.x)
            return end.x <= p.x && p.x <= start.x

        if (start.y < end.y)
            return start.y <= p.y && p.y <= end.y

        if (end.y < start.y)
            return end.y <= p.y && p.y <= start.y

        return start.x == p.x && start.y == p.y
    }

    // Returns true if the lines intersect
    // Credits to https://stackoverflow.com/questions/3838329/how-can-i-check-if-two-segments-intersect
    fun intersect(that: LineDouble): Boolean {

        if (this.start == this.end) return this.start == that.start || this.start == that.end
        if (that.start == that.end) return that.start == this.start || that.start == this.end

        var s1 = this.side(that.start)
        var s2 = this.side(that.end)

        if (s1 == 0 && s2 == 0)
            return isPointInsideClosedSegment(that.start)
                    || isPointInsideClosedSegment(that.end)
                    || that.isPointInsideClosedSegment(this.start)
                    || that.isPointInsideClosedSegment(this.end)

        // Not touching and on the same side
        if (s1 != 0 && s1 == s2)
            return false

        s1 = that.side(this.start)
        s2 = that.side(this.end)

        // Not touching and on the same side
        if (s1 != 0 && s1 == s2)
            return false

        return true
    }
}