package aoc.aoc2022.dec01

import aoc.ktutils.readLines
import aoc.ktutils.readTestLines

fun main() {
    aoc.ktutils.check(execute1(readTestLines()), 24000)
    execute1(readLines()).let { println(it) ; aoc.ktutils.check(it, 70720) }

    aoc.ktutils.check(execute2(readTestLines()), 45000)
    execute2(readLines()).let { println(it) ; aoc.ktutils.check(it, 207148) }
}

private fun execute1(lines: List<String>): Int {
    val elfes = readElfes(lines)
    return elfes.max()!!
}

private fun readElfes(lines: List<String>): ArrayList<Int> {
    var elfes = ArrayList<Int>()
    var elf = 0
    for (line in lines) {
        if (line == "") {
            elfes.add(elf)
            elf = 0
            continue
        }
        elf += line.toInt()
    }
    elfes.add(elf)
    return elfes
}

private fun execute2(lines: List<String>): Int {
    val elfes = readElfes(lines)
    elfes.sort()
    elfes.reverse()
    return elfes[0] + elfes[1] + elfes[2]
}
