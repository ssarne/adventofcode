package aoc.ktutils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class PolygonTest {

    @Test
    fun inside() {
        assertEquals(true, Polygon.create(PointLong(0, 0), PointLong(4, 0), PointLong(4, 4), PointLong(0, 4), PointLong(0, 0)).inside(PointLong(2, 2)))
        assertEquals(false, Polygon.create(PointLong(0, 0), PointLong(4, 0), PointLong(4, 4), PointLong(0, 4), PointLong(0, 0)).inside(PointLong(5, 2)))
    }
}