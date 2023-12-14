package aoc.ktutils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class AlgosKtTest {

    @Test
    fun testLcm() {
        assertEquals(6, lcm(3, 6))
        assertEquals(12, lcm(4, 3))
        assertEquals(18, lcm(6, 3, 9))
        assertEquals(60, lcm(3, 4, 5))
    }

    @Test
    fun testGcd() {
        assertEquals(5, gcd(10, 15))
        assertEquals(5, gcd(35, 10))
        assertEquals(1, gcd(31, 2))
        assertEquals(1, gcd(31, 3))
        assertEquals(3, gcd(21, 3))
    }

    @Test
    fun testAddUp() {
        assertEquals(8, addUp(2, 2, 12))
        assertEquals(8, addUp(3, 2, 12))
        assertEquals(6, addUp(4, 2, 12))
        assertEquals(999999980, addUp(13, 7, 1000000000))
        assertEquals(999999756, addUp(215, 108, 1000000000))
    }
}