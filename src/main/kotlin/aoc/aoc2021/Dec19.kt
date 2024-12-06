package aoc.aoc2021

import aoc.ktutils.*
import kotlin.collections.ArrayList
import kotlin.collections.HashSet

fun main() {
    execute(readTestLines(), readTestLines(3)).let {
        check(it.first, 79)
        check(it.second, 3621)
    }
    execute(readLines()).let {
        println(it.first); check(it.first, readAnswerAsInt(1))
        println(it.second); check(it.second, readAnswerAsInt(2))
    }
}

private data class Scanner(
    val id: Int,
    val beacons: HashSet<Point3D> = HashSet(),
    var rot: String? = null,
    var pos: Point3D? = null,
    var matchiest: Match = Match(0, "N0", Point3D(0, 0, 0))
) {
    override fun toString() = "S$id $pos $rot (${beacons.size})"
}

private data class Match(val count: Int, val rot: String, val pos: Point3D)

private fun execute(input: List<String>, answers: List<String>? = null, print: Boolean = false): Pair<Int, Int> {

    val scanners = parseInput(input)
    val beacons = HashSet<Point3D>()
    val rotations = Point3D.rotations()

    scanners.first().let { // lock one scanner down as reference
        it.pos = Point3D(0, 0, 0)
        it.rot = "N0"
        beacons.addAll(it.beacons)
    }

    do {
        var found = false // continue as long as new probe locations are found
        scanners@ for (s in scanners) {  // for all scanners
            if (s.pos != null) continue
            for (rot in rotations) {             // try to rotate the scanner in all directions
                for (raw_b in s.beacons) {       // for all beacons in this scanner
                    for (g_b in beacons) {       // combine it as the same as all beacons in global list
                        val rot_b = raw_b.rotate(rot)
                        val g_s = g_b - rot_b
                        var matches = s.beacons.stream()
                            .map { b -> g_s + b.rotate(rot) }  // rotate and rebase
                            .filter { b -> beacons.contains(b) } // match with existing
                            .count().toInt()
                        if (matches >= 12) {
                            if (print) println("Match ($matches)  ${s.id} $g_s $rot_b")
                            for (b in s.beacons) beacons.add(g_s + b.rotate(rot))
                            s.rot = rot
                            s.pos = g_s
                            found = true
                            continue@scanners
                        }
                        if (matches > s.matchiest.count) {
                            s.matchiest = Match(matches, rot, g_s)
                        }
                    }
                }
            }
        }
    } while (found)

    if (answers != null) {
        var expected = HashSet<Point3D>()
        for (line in answers) expected.add(Point3D.create(line))
        for (b in beacons) if (!expected.contains(b)) println("Missing: $b")
        if (print) for (b in beacons.toMutableList().sorted()) println("$b")
    }

    for (s in scanners) checkForNotNull(s.pos, "Warning ${s.id} (${s.matchiest.count})")

    var manhattan = 0L
    for (s1 in scanners)
        for (s2 in scanners)
            s1.pos!!.manhattan(s2.pos!!).let { if (it > manhattan) manhattan = it }

    return beacons.size to manhattan.toInt()
}

private fun parseInput(input: List<String>): List<Scanner> {
    var scanners = ArrayList<Scanner>()
    var scanner: Scanner? = null
    for (line in input) {
        if (line.isEmpty()) continue
        if (line.contains("scanner")) {
            scanner = Scanner(line.split(" ")[2].toInt())
            scanners.add(scanner)
            continue
        }
        scanner!!.beacons.add(Point3D.create(line))
    }

    return scanners
}