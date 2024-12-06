package aoc.ktutils

import kotlin.math.*

data class Polygon(val points: MutableList<PointLong>) {

    companion object {
        fun create(vararg points: PointLong): Polygon {
            val polygon = Polygon(ArrayList())
            for (p in points) polygon.add(p)
            return polygon
        }
    }

    fun add(p: PointLong) = points.add(p)

    // Check how many times a ray for 0,y crosses non-horizontal lines
    // If odd, it is inside
    // Assume it is the complete round, i.e. p[0] == p[-1]
    // Assume all lines are horizontal or vertical
    fun inside(p: PointLong): Boolean {
        val minx = points.minBy { it.x }
        val base = Line(PointLong(minx.x - 1, p.y), p)
        var n = 0
        for ((p1, p2) in points.zipWithNext()) {
            if (p1.y == p2.y) continue // ignore horizontal lines
            if (p1.x != p2.x) throw RuntimeException("Assumes horizontal and vertical lines $p1 $p2")
            // TODO: Need to identify if prev and next together as a line continue or turn, count as one crossing
            if (p.y == p1.y) throw RuntimeException("CMH: Does not case when crossing end of line")
            if (max(p1.x, p2.x) < min(base.start.x, base.end.x)) continue
            if (min(p1.x, p2.x) > max(base.start.x, base.end.x)) continue
            if (max(p1.y, p2.y) < min(base.start.y, base.end.y)) continue
            if (min(p1.y, p2.y) > max(base.start.y, base.end.y)) continue
            n++
        }
        return n % 2 != 0
    }
}