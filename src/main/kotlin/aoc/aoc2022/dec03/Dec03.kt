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
        var first = line.subSequence(0, line.length / 2)
        var second = line.subSequence(line.length / 2, line.length)
        var m = '0'
        for (c in first)
            for (d in second)
                if (c == d)
                    m = c

        score += if (m.isLowerCase()) (m - 'a') + 1 else (m - 'A') + 27
    }
    return score
}

private fun execute2(lines: List<String>): Int {
    var score = 0
    var m = '0'
    for (i in lines.indices step 3) {
        for (c in lines[i])
            for (d in lines[i + 1])
                for (e in lines[i + 2])
                    if (c == d && d == e)
                        m = c

        score += if (m.isLowerCase()) (m - 'a') + 1 else (m - 'A') + 27
    }
    return score
}
