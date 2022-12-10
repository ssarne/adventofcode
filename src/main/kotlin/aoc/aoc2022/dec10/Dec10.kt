package aoc.aoc2022.dec10

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 13140)
    execute1(readLines()).let { println(it); check(it, 16060) }

    var expected = "##..##..##..##..##..##..##..##..##..##.." +
                "###...###...###...###...###...###...###." +
                "####....####....####....####....####...." +
                "#####.....#####.....#####.....#####....." +
                "######......######......######......####" +
                "#######.......#######.......#######....."
    check(String(execute2(readTestLines())), expected)
    execute2(readLines()).let { printCRT(it, 40, true) }
}

private fun execute1(input: List<String>): Int {

    var intervals = intArrayOf(20, 60, 100, 140, 180, 220, Int.MAX_VALUE)
    var sum = 0  // result, the reg-x * cycles at above intervals
    var oi = 0

    var regX = 1
    var cycles = 1

    for (line in input) {
        if (line == "noop") {
            if (cycles++ == intervals[oi]) sum += regX * intervals[oi++]
        } else if (line.startsWith("addx")) {
            if (cycles++ == intervals[oi]) sum += regX * intervals[oi++]
            if (cycles++ == intervals[oi]) sum += regX * intervals[oi++]
            regX += line.split(" ")[1].toInt()
        }
    }
    return sum
}

private fun execute2(input: List<String>): CharArray {

    var crt = CharArray(240)

    var regX = 1
    var cycles = -1

    for (line in input) {
        if (line == "noop") {
            crt[++cycles] = if (cycles % 40 in regX - 1..regX + 1) '#' else '.'
        } else if (line.startsWith("addx")) {
            crt[++cycles] = if (cycles % 40 in regX - 1..regX + 1) '#' else '.'
            crt[++cycles] = if (cycles % 40 in regX - 1..regX + 1) '#' else '.'
            regX += line.split(" ")[1].toInt()
        }
    }

    return crt
}

private fun printCRT(crt: CharArray, width: Int, space: Boolean = false) {
    crt.forEachIndexed { i, c ->
        if (i % 40 == 0) println()
        if (space && i % 5 == 0) print("  ")
        print(c)
    }
    println()
}
