package aoc.ktutils

import org.junit.Test

import org.junit.Assert.*

class Point3DTest {

    @Test
    fun compareTo() {
        assertEquals(Point3D(1, 2, 3).compareTo(Point3D(2, 5, 5)), -1)
    }
}