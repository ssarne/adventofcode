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
}