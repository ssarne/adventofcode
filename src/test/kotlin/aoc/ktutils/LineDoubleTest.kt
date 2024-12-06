package aoc.ktutils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class LineDoubleTest {

    @Test
    fun intersect() {
        Assertions.assertEquals(true, // x cross lines
            LineDouble(PointDouble(0.0,0.0), PointDouble(2.0, 2.0))
                .intersect(LineDouble(PointDouble(2.0,0.0), PointDouble(0.0, 2.0))))

        Assertions.assertEquals(true, // x cross lines
            LineDouble(PointDouble(2.0, 2.0), PointDouble(0.0,0.0))
                .intersect(LineDouble(PointDouble(2.0,0.0), PointDouble(0.0, 2.0))))

        Assertions.assertEquals(true, // l2 touch l1 in the middle
            LineDouble(PointDouble(0.0, 0.0), PointDouble(4.0, 0.0))
                .intersect(LineDouble(PointDouble(2.0,0.0), PointDouble(0.0, 2.0))))

        Assertions.assertEquals(false,  // two parallel lines
            LineDouble(PointDouble(2.0,.0), PointDouble(2.0, 4.0))
                .intersect(LineDouble(PointDouble(0.0, 2.0), PointDouble (0.0, 4.0))))

        Assertions.assertEquals(true,
            LineDouble(PointDouble(2.0,.0), PointDouble(2.0, 2.0))
                .intersect(LineDouble(PointDouble(2.0,0.0), PointDouble(0.0, 2.0)))) // meet at edge PointDouble

        Assertions.assertEquals(false,
            LineDouble(PointDouble(0.0, 0.0), PointDouble(4.0,0.0))
                .intersect(LineDouble(PointDouble(2.0, 1.0), PointDouble(2.0, 2.0)))
        )
        Assertions.assertEquals(false,
            LineDouble(PointDouble(0.0, 0.0), PointDouble(4.0,0.0))
                .intersect(LineDouble(PointDouble(0.0, 3.0), PointDouble(3.0, 1.0)))
        )

        Assertions.assertEquals(false,
            LineDouble(PointDouble(0.0,0.0), PointDouble(4.0,.0))
                .intersect(LineDouble(PointDouble(0.0, 1.0), PointDouble(4.0, 1.0)))
        )

        Assertions.assertEquals(false,
            LineDouble(PointDouble(0.0, 2.0), PointDouble(2.0, 2.0))
                .intersect(LineDouble(PointDouble(0.0,0.0), PointDouble(4.0,0.0)))
        )
    }
}