package aoc.aoc2022.dec03

import aoc.ktutils.*


fun main() {
    check(execute1(readTestLines()), 157)
    execute1(readLines()).let { println(it); check(it, 7878) }

    check(execute2(readTestLines()), 70)
    execute2(readLines()).let { println(it); check(it, 2760) }
}

private fun execute1(lines: List<String>): Int {
    var score = 0
    for (line in lines) {
        val first = line.subSequence(0, line.length / 2).toSet()
        val second = line.subSequence(line.length / 2, line.length).toSet()
        for (m in first intersect second)
            score += if (m.isLowerCase()) (m - 'a') + 1 else (m - 'A') + 27
    }
    return score
}

private fun execute2(lines: List<String>): Int {
    var score = 0
    for (i in lines.indices step 3) {
        val intersection = lines[i].toSet() intersect lines[i+1].toSet() intersect lines[i+2].toSet()
        for (m in intersection)
            score += if (m.isLowerCase()) (m - 'a') + 1 else (m - 'A') + 27
    }
    return score
}
