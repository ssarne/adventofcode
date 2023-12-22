package aoc.ktutils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PolygonTest {

    @Test
    fun inside() {
        assertEquals(true, Polygon.create(Point(0, 0), Point(4, 0), Point(4, 4), Point(0, 4), Point(0, 0)).inside(Point(2, 2)))
        assertEquals(false, Polygon.create(Point(0, 0), Point(4, 0), Point(4, 4), Point(0, 4), Point(0, 0)).inside(Point(5, 2)))
    }
}