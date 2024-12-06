package aoc.ktutils

import kotlin.math.abs
import kotlin.math.sqrt


data class LineDouble3D(val p1: PointDouble3D, val p2: PointDouble3D) {

    override fun toString() = "($p1->$p2)"

    companion object {
        const val eps = 0.1 // epsilon value, i.e. when the "match"
        val zp = PointDouble3D(0.0, 0.0, 0.0)
        val zl = LineDouble3D(zp, zp)
    }

    // Returns the shortest line between this and the other line
    // or false if there is no line which is orthogonal
    // Calculate the line segment PaPb that is the shortest route between
    // two lines P1P2 and P3P4. Calculate also the values of mua and mub where
    // Pa = P1 + mua (P2 - P1)
    // Pb = P3 + mub (P4 - P3)
    // Credits to https://paulbourke.net/geometry/pointlineplane/
    fun intersect(that: LineDouble3D): Pair<Boolean, LineDouble3D> {
        val (found, p1, p2) = intersect(this.p1, this.p2, that.p1, that.p2)
        return found to LineDouble3D(p1, p2)
    }

    private fun intersect(p1: PointDouble3D, p2: PointDouble3D, p3: PointDouble3D, p4: PointDouble3D): Triple<Boolean, PointDouble3D, PointDouble3D> {

        val p13 = p1 - p3
        val p43 = p4 - p3
        val p21 = p2 - p1

        if (abs(p43.x) < eps && abs(p43.y) < eps && abs(p43.z) < eps) Triple(false, zp, zp)
        if (abs(p21.x) < eps && abs(p21.y) < eps && abs(p21.z) < eps) Triple(false, zp, zp)

        val d1343 = p13.x * p43.x + p13.y * p43.y + p13.z * p43.z
        val d4321 = p43.x * p21.x + p43.y * p21.y + p43.z * p21.z
        val d1321 = p13.x * p21.x + p13.y * p21.y + p13.z * p21.z
        val d4343 = p43.x * p43.x + p43.y * p43.y + p43.z * p43.z
        val d2121 = p21.x * p21.x + p21.y * p21.y + p21.z * p21.z

        val denominator = d2121 * d4343 - d4321 * d4321
        val numerator = d1343 * d4321 - d1321 * d4343

        if (abs(denominator) < eps) Triple(false, zp, zp)

        val mua = numerator / denominator
        val mub = (d1343 + d4321 * mua) / d4343

        val pa = PointDouble3D(p1.x + mua * p21.x, p1.y + mua * p21.y, p1.z + mua * p21.z)
        val pb = PointDouble3D(p3.x + mub * p43.x, p3.y + mub * p43.y, p3.z + mub * p43.z)
        return Triple(true, pa, pb)
    }

    fun length(): Double {
        val dx = abs(p1.x - p2.x)
        val dy = abs(p1.y - p2.y)
        val dz = abs(p1.z - p2.z)
        val squares = dx * dx + dy * dy + dz * dz
        return sqrt(squares)
    }
}