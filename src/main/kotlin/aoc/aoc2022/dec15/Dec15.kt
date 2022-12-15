package aoc.aoc2022.dec15

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines(), 10), 26)
    execute1(readLines(), 2000000).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(), 20, 20), 56000011)
    execute2(readLines(), 4000000, 4000000).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>, row: Int): Int {

    val (sensors, beacons) = parse(input)
    val map = HashMap<Point, Char>()
    sensors.keys.map { map.put(it, 'S') }
    beacons.map { map.put(it, 'B') }
    // printSparseMatrix(map, true)

    val cave = HashSet<Point>()
    for ((sensor, beacon) in sensors) {
        val range = sensor.manhattan(beacon)
        val dist = abs(sensor.y - row)
        val rest = range - dist
        for (i in -rest..rest) {
            Point(sensor.x + i, row).let { p ->
                if (!map.containsKey(p) || map[p] == 'S') {
                    map.put(p, '#')
                    cave.add(p)
                }
            }
        }
    }

    // printSparseMatrix(map, true)
    return cave.size
}

private fun execute2(input: List<String>, maxX: Int, maxY: Int): Long {
    val (sensors, beacons) = parse(input)
    for ((sensor, beacon) in sensors) {
        val outside = sensor.manhattan(beacon) + 1
        for (dx in 0..outside) {
            Point(sensor.x + dx, sensor.y + (outside - dx)).let { p ->
                if (p.x in 0..maxX &&
                    p.y in 0..maxY &&
                    !beacons.contains(p) &&
                    notInRange(p, sensors))
                    return p.x * 4000000L + p.y
            }
            Point(sensor.x - dx, sensor.y + (outside - dx)).let { p ->
                if (p.x in 0..maxX &&
                    p.y in 0..maxY &&
                    !beacons.contains(p) &&
                    notInRange(p, sensors))
                    return p.x * 4000000L + p.y
            }
            Point(sensor.x + dx, sensor.y - (outside - dx)).let { p ->
                if (p.x in 0..maxX &&
                    p.y in 0..maxY &&
                    !beacons.contains(p) &&
                    notInRange(p, sensors))
                    return p.x * 4000000L + p.y
            }
            Point(sensor.x - dx, sensor.y - (outside - dx)).let { p ->
                if (p.x in 0..maxX &&
                    p.y in 0..maxY &&
                    !beacons.contains(p) &&
                    notInRange(p, sensors))
                    return p.x * 4000000L + p.y
            }
        }
    }
    throw RuntimeException("Unique point not found.")
}

fun notInRange(point: Point, sensors: Map<Point, Point>): Boolean {
    for (sensor in sensors.keys) {
        val beacon = sensors[sensor]!!
        var range = Math.abs(sensor.x - beacon.x) + Math.abs(sensor.y - beacon.y)
        var dist = Math.abs(sensor.x - point.x) + Math.abs(sensor.y - point.y)
        if (dist <= range) return false
    }
    return true
}

private fun parse(input: List<String>): Pair<Map<Point, Point>, Set<Point>> {
    val sensors = HashMap<Point, Point>()
    val beacons = HashSet<Point>()
    for (line in input) {
        val sections = line
            .replace(",", "")
            .replace(":", "")
            .replace("=", " ")
            .split(" ")
        val sensor = Point(sections[3].toInt(), sections[5].toInt())
        val beacon = Point(sections[11].toInt(), sections[13].toInt())
        sensors[sensor] = beacon
    }
    for (beacon in sensors.values) {
        beacons.add(beacon)
    }
    return Pair(sensors, beacons)
}