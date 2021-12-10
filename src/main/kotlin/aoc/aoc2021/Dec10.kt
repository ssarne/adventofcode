package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException
import java.util.*
import kotlin.streams.toList

fun main() {
    check(execute1(readTestLines()), 26397)
    println(execute1(readLines())) // 265527
    check(execute2(readTestLines()), 288957)
    println(execute2(readLines())) // 3969823589
}

private fun execute1(input: List<String>): Int {
    return input.stream()
        .mapToInt { errorScore(it) }
        .sum()
}

private fun execute2(input: List<String>): Long {
    var scores = input.stream()
        .filter { errorScore(it) == 0 }
        .mapToLong { incompleteScore(it) }
        .sorted()
        .toList()
    return scores[scores.size / 2]
}

fun errorScore(line: String): Int {
    var stack = LinkedList<Char>()
    for (c in line) {
        when (c) {
            '(','[','{','<' -> stack.push(c)
            ')' -> if (stack.pop() != '(') return 3
            ']' -> if (stack.pop() != '[') return 57
            '}' -> if (stack.pop() != '{') return 1197
            '>' -> if (stack.pop() != '<') return 25137
            else -> throw RuntimeException("wat")
        }
    }
    return 0
}

fun incompleteScore(line: String): Long {
    var stack = LinkedList<Char>()
    for (c in line) {
        when (c) {
            '(', '[', '{', '<' -> stack.push(c)
            ')', ']', '}', '>' -> stack.pop()
            else -> throw RuntimeException("wat")
        }
    }

    var score = 0L
    while (stack.size > 0) {
        var c = stack.pop()
        when (c) {
            '(' -> score = score * 5 + 1
            '[' -> score = score * 5 + 2
            '{' -> score = score * 5 + 3
            '<' -> score = score * 5 + 4
            else -> throw RuntimeException("wat $c $line")
        }
    }

    return score
}
