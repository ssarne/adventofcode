package aoc.aoc2022.dec06

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute("bvwbjplbgvbhsrlpgdmjqwftvncz", 4), 5)
    check(execute("nppdvjthqldpwncqszvftbrmjlhg", 4), 6)
    execute(readText(), 4).let { println(it); check(it, 1757) }

    check(execute("mjqjpqmgbljsphdztnvjfqwrcgsmlb", 14), 19)
    execute(readText(), 14).let { println(it); check(it, 2950) }
}

private fun execute(line: String, len: Int): Int {
    for (i in 0 until line.length - len) {
        var match = false
        for (j in i until i + len) {
            for (k in j + 1 until i + len) {
                if (line[j] == line[k]) {
                    match = true
                }
            }
        }
        if (!match) {
            return i + len
        }
    }
    return -1
}