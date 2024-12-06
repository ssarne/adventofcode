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
    for (cube in cubes.keys)
        for (c in cube.adjacent())
            if (cubes[c] == null) sum += 1

    return sum
}

private fun execute2(input: List<String>): Int {
    val cubes = parse(input)

    val dx = cubes.keys.map { it.x }.min()!! - 1 .. cubes.keys.map { it.x }.max()!! + 1
    val dy = cubes.keys.map { it.y }.min()!! - 1 .. cubes.keys.map { it.y }.max()!! + 1
    val dz = cubes.keys.map { it.z }.min()!! - 1 .. cubes.keys.map { it.z }.max()!! + 1

    // BFS - flood the cube
    val queue = LinkedList<Point3D>()
    queue.add(Point3D(dx.first, dy.first, dz.first))
    while (!queue.isEmpty()) {
        val pos = queue.pop()
        if (cubes.contains(pos)) continue // already visited
        cubes[pos] = 'W'
        for (c in pos.adjacent()) {
            if (c.x !in dx) continue
            if (c.y !in dy) continue
            if (c.z !in dz) continue
            if (cubes.contains(c)) continue
            queue.push(c)
        }
    }

    var sum = 0
    for ((pos, c) in cubes)
        if (c == 'L')
            for (ci in pos.adjacent())
                if (cubes[ci] == 'W') sum += 1

    return sum
}

private fun parse(input: List<String>): MutableMap<Point3D, Char> {
    val cubes = HashMap<Point3D, Char>()
    for (line in input) {
        val ints = line.split(",")
        cubes.put(Point3D(ints[0].toLong(), ints[1].toLong(), ints[2].toLong()), 'L')
    }
    return cubes
}