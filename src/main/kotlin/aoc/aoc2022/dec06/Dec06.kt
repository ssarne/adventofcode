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
        if (line.subSequence(i, i+len).toSet().size == len)
            return i + len
    }
    return -1
}