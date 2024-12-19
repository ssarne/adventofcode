package aoc.ktutils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LineTest {

    @Test
    fun intersect() {
        assertEquals(true, Line(PointLong(0, 0), PointLong(2, 2)).intersect(Line(PointLong(2, 0), PointLong(0, 2)))) // x cross lines
        assertEquals(true, Line(PointLong(2, 2), PointLong(0, 0)).intersect(Line(PointLong(2, 0), PointLong(0, 2)))) // x cross lines
        assertEquals(true, Line(PointLong(0, 0), PointLong(4, 0)).intersect(Line(PointLong(2, 0), PointLong(0, 2)))) // l2 touch l1 in the middle
        assertEquals(false, Line(PointLong(2, 0), PointLong(2, 4)).intersect(Line(PointLong(0, 2), PointLong(0, 4)))) // two parallel lines
        assertEquals(true, Line(PointLong(2, 0), PointLong(2, 2)).intersect(Line(PointLong(2, 0), PointLong(0, 2)))) // meet at edge point

        assertEquals(false, Line(PointLong(0, 0), PointLong(4, 0)).intersect(Line(PointLong(2, 1), PointLong(2, 2))))
        assertEquals(false, Line(PointLong(0, 0), PointLong(4, 0)).intersect(Line(PointLong(0, 3), PointLong(3, 1))))
        assertEquals(false, Line(PointLong(0, 0), PointLong(4, 0)).intersect(Line(PointLong(0, 1), PointLong(4, 1))))

        assertEquals(false, Line(PointLong(0, 2), PointLong(2, 2)).intersect(Line(PointLong(0, 0), PointLong(4,0))))
    }
}