package aoc.ktutils

import org.junit.Test

import org.junit.Assert.*

class IntMatrixTest {

    @Test
    fun inc() {
        val m = IntMatrix.create(3, 3)
        assertEquals(m.get(1, 1), 0)
        assertEquals(m.inc(1, 1), 0)
        assertEquals(m.inc(1, 1), 1)
        assertEquals(m.get(1, 1), 2)
    }

    @Test
    fun times() {
        val m1 = IntMatrix.create(listOf("123", "342", "321"))
        val m2 = IntMatrix.create(listOf("111", "342", "321"))
        assertEquals(m1 * m2, IntMatrix.create(listOf(
            intArrayOf(16, 15, 8),
            intArrayOf(21, 23, 13),
            intArrayOf(12, 13, 8))))
    }
}