package aoc.aoc2021

import aoc.ktutils.*
import java.util.*
import kotlin.collections.ArrayList

fun main() {
    check(execute1(readTestLines(1)), 10)
    check(execute1(readTestLines(2)), 19)
    check(execute1(readTestLines(3)), 226)
    println(execute1(readLines())) // 4691
    check(execute2(readTestLines(1)), 36)
    println(execute2(readLines())) // 140718
}

private data class Cave(val id: String, val next: MutableList<Cave>)

private fun execute1(input: List<String>): Int {

    var caves = parseCaves(input)
    var count = findPaths(caves["start"]!!, ArrayList(), false)
    return count
}

private fun execute2(input: List<String>): Int {

    var caves = parseCaves(input)
    var count = findPaths(caves["start"]!!, ArrayList(), true)
    return count
}

private fun parseCaves(input: List<String>): HashMap<String, Cave> {

    var caves = HashMap<String, Cave>()
    for (line in input) {
        var cave1 = line.split("-")[0].let { caves.getOrPut(it) { Cave(it, ArrayList<Cave>()) } }
        var cave2 = line.split("-")[1].let { caves.getOrPut(it) { Cave(it, ArrayList<Cave>()) } }
        cave1.next.add(cave2)
        cave2.next.add(cave1)
    }
    return caves
}

private fun findPaths(cave: Cave, path: MutableList<String>, allowDuplicate: Boolean): Int {

    if (cave.id == "start" && path.isNotEmpty()) return 0
    if (cave.id == "end") return 1

    var cc = allowDuplicate
    if (isLowerCase(cave.id) && path.contains(cave.id)) {
        if (allowDuplicate) {
            cc = false
        } else {
            return 0
        }
    }

    path.add(cave.id)
    var count = cave.next.stream()
        .mapToInt { findPaths(it, path, cc) }
        .sum()
    path.remove(cave.id)
    return count
}