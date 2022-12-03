package aoc.aoc2022.dec02

import aoc.ktutils.asChunks
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines


fun main() {
    aoc.ktutils.check(execute1(readTestLines()), 15)
    execute1(readLines()).let { println(it); aoc.ktutils.check(it, 13221) }

    aoc.ktutils.check(execute2(readTestLines()), 12)
    execute2(readLines()).let { println(it); aoc.ktutils.check(it, 13131) }
}

private fun execute1(lines: List<String>): Int {
    var score = 0
    for (line in lines) {
        var round = line.split(" ")

        if (round[0] == "A" && round[1] == "X") score += 1 + 3
        else if (round[0] == "A" && round[1] == "Y") score += 2 + 6
        else if (round[0] == "A" && round[1] == "Z") score += 3 + 0

        else if (round[0] == "B" && round[1] == "X") score += 1 + 0
        else if (round[0] == "B" && round[1] == "Y") score += 2 + 3
        else if (round[0] == "B" && round[1] == "Z") score += 3 + 6

        else if (round[0] == "C" && round[1] == "X") score += 1 + 6
        else if (round[0] == "C" && round[1] == "Y") score += 2 + 0
        else if (round[0] == "C" && round[1] == "Z") score += 3 + 3
    }
    return score
}

private fun execute2(lines: List<String>): Int {
    var score = 0
    for (line in lines) {
        var round = line.split(" ")

        if (round[0] == "A" && round[1] == "X") score += 3 + 0 // scissors
        else if (round[0] == "A" && round[1] == "Y") score += 1 + 3 // rock
        else if (round[0] == "A" && round[1] == "Z") score += 2 + 6 // paper

        else if (round[0] == "B" && round[1] == "X") score += 1 + 0 // rock
        else if (round[0] == "B" && round[1] == "Y") score += 2 + 3 // paper
        else if (round[0] == "B" && round[1] == "Z") score += 3 + 6 // scissors

        else if (round[0] == "C" && round[1] == "X") score += 2 + 0  // paper
        else if (round[0] == "C" && round[1] == "Y") score += 3 + 3  // scissors
        else if (round[0] == "C" && round[1] == "Z") score += 1 + 6  // rock
    }
    return score
}
