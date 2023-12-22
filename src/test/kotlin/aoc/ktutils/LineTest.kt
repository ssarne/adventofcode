package aoc.ktutils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class LineTest {

    @Test
    fun intersect() {
        assertEquals(true, Line(Point(0, 0), Point(2, 2)).intersect(Line(Point(2, 0), Point(0, 2))))
        assertEquals(true, Line(Point(0, 0), Point(4, 0)).intersect(Line(Point(2, 0), Point(0, 2))))
        assertEquals(true, Line(Point(2, 0), Point(2, 4)).intersect(Line(Point(0, 2), Point(0, 4))))
        assertEquals(true, Line(Point(2, 0), Point(2, 2)).intersect(Line(Point(2, 0), Point(0, 2))))
        assertEquals(true, Line(Point(2, 2), Point(0, 0)).intersect(Line(Point(2, 0), Point(0, 2))))

        assertEquals(false, Line(Point(0, 0), Point(4, 0)).intersect(Line(Point(2, 1), Point(2, 2))))
        assertEquals(false, Line(Point(0, 0), Point(4, 0)).intersect(Line(Point(0, 3), Point(3, 1))))
        assertEquals(false, Line(Point(0, 0), Point(4, 0)).intersect(Line(Point(0, 1), Point(4, 1))))

        assertEquals(false, Line(Point(0, 2), Point(2, 2)).intersect(Line(Point(0, 0), Point(4,0))))
    }
}