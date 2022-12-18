package aoc.aoc2022.dec18

import aoc.ktutils.*
import java.util.*

fun main() {
    check(execute1(readTestLines()), 64)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 58)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    val cubes = parse(input)
    var sum = 0
    for (cube in cubes.keys) {
        if (!cubes.contains(Point3D(cube.x + 1, cube.y, cube.z))) sum += 1
        if (!cubes.contains(Point3D(cube.x - 1, cube.y, cube.z))) sum += 1
        if (!cubes.contains(Point3D(cube.x, cube.y + 1, cube.z))) sum += 1
        if (!cubes.contains(Point3D(cube.x, cube.y - 1, cube.z))) sum += 1
        if (!cubes.contains(Point3D(cube.x, cube.y, cube.z + 1))) sum += 1
        if (!cubes.contains(Point3D(cube.x, cube.y, cube.z - 1))) sum += 1
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    val cubes = parse(input)
    val xmin = cubes.keys.map { it.x }.min()!! - 1
    val xmax = cubes.keys.map { it.x }.max()!! + 1
    val ymin = cubes.keys.map { it.y }.min()!! - 1
    val ymax = cubes.keys.map { it.y }.max()!! + 1
    val zmin = cubes.keys.map { it.z }.min()!! - 1
    val zmax = cubes.keys.map { it.z }.max()!! + 1

    fun enqueue(pot: Point3D, queue: LinkedList<Point3D>) {
        if (pot.x !in xmin..xmax) return
        if (pot.y !in ymin..ymax) return
        if (pot.z !in zmin..zmax) return
        if (cubes.contains(pot)) return
        queue.push(pot)
    }

    val queue = LinkedList<Point3D>()
    queue.add(Point3D(xmin, ymin, zmin))
    while (!queue.isEmpty()) {
        val pos = queue.pop()
        if (cubes.contains(pos)) continue // already visited
        cubes[pos] = 'W'
        enqueue(Point3D(pos.x + 1, pos.y, pos.z), queue)
        enqueue(Point3D(pos.x - 1, pos.y, pos.z), queue)
        enqueue(Point3D(pos.x, pos.y + 1, pos.z), queue)
        enqueue(Point3D(pos.x, pos.y - 1, pos.z), queue)
        enqueue(Point3D(pos.x, pos.y, pos.z + 1), queue)
        enqueue(Point3D(pos.x, pos.y, pos.z - 1), queue)
    }

    var sum = 0
    for ((pos, c) in cubes) {
        if (c == 'L') {
            Point3D(pos.x + 1, pos.y, pos.z).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
            Point3D(pos.x - 1, pos.y, pos.z).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
            Point3D(pos.x, pos.y + 1, pos.z).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
            Point3D(pos.x, pos.y - 1, pos.z).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
            Point3D(pos.x, pos.y, pos.z + 1).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
            Point3D(pos.x, pos.y, pos.z - 1).let { if (cubes.contains(it) && cubes[it] == 'W') sum += 1 }
        }
    }
    return sum
}

private fun parse(input: List<String>): MutableMap<Point3D, Char> {
    val cubes = HashMap<Point3D, Char>()
    for (line in input) {
        val ints = line.split(",")
        cubes.put(Point3D(ints[0].toInt(), ints[1].toInt(), ints[2].toInt()), 'L')
    }
    return cubes
}