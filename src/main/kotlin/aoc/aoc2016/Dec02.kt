package aoc.aoc2016

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), "1985")
    execute1(readLines()).let { println(it) ; check(it, readAnswer(1)) }

    check(execute2(readTestLines(1)), "5DB3")
    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
}

private fun execute1(input: List<String>): String {
    var code = ""
    var pos = 5
    for (line in input) {
        for (c in line) {
            pos = when (c) {
                'U' -> if (pos > 3) pos - 3 else pos
                'D' -> if (pos < 7) pos + 3 else pos
                'L' -> if (pos % 3 != 1) pos - 1 else pos
                'R' -> if (pos % 3 != 0) pos + 1 else pos
                else -> throw RuntimeException("CMH $line $c")
            }
        }
        code += pos
    }
    return code
}

private fun execute2(input: List<String>): String {
    var code = ""
    var pos = '5'
    for (line in input) {
        for (c in line) {
            when (c) {
                'U' -> when (pos) {
                    'D' -> pos = 'B'
                    'A' -> pos = '6'
                    'B' -> pos = '7'
                    'C' -> pos = '8'
                    '6' -> pos = '2'
                    '7' -> pos = '3'
                    '8' -> pos = '4'
                    '3' -> pos = '1'
                }

                'D' -> when (pos) {
                    '1' -> pos = '3'
                    '2' -> pos = '6'
                    '3' -> pos = '7'
                    '4' -> pos = '8'
                    '6' -> pos = 'A'
                    '7' -> pos = 'B'
                    '8' -> pos = 'C'
                    'B' -> pos = 'D'
                }

                'R' -> when (pos) {
                    '5' -> pos = '6'
                    '2' -> pos = '3'
                    '6' -> pos = '7'
                    'A' -> pos = 'B'
                    '3' -> pos = '4'
                    '7' -> pos = '8'
                    'B' -> pos = 'C'
                    '8' -> pos = '9'
                }

                'L' -> when (pos) {
                    '9' -> pos = '8'
                    '4' -> pos = '3'
                    '8' -> pos = '7'
                    'C' -> pos = 'B'
                    '3' -> pos = '2'
                    '7' -> pos = '6'
                    'B' -> pos = 'A'
                    '6' -> pos = '5'
                }

                else -> throw RuntimeException("CMH $line $c")
            }
        }
        code += pos
    }
    return code
}