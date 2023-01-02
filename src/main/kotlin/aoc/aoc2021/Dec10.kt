package aoc.aoc2021

import aoc.ktutils.*
import java.util.*

fun main() {
    check(execute1(readTestLines()), 26397)
    execute1(readLines()).let { println(it); check(it, 265527) }
    check(execute2(readTestLines()), 288957)
    execute2(readLines()).let { println(it); check(it, 3969823589) }
}

private fun execute1(input: List<String>): Int {
    return input.stream()
        .mapToInt { errorScore(it) }
        .sum()
}

private fun execute2(input: List<String>): Long {
    val scores = input.stream()
        .filter { errorScore(it) == 0 }
        .mapToLong { incompleteScore(it) }
        .sorted()
        .toArray()
    return scores[scores.size / 2]
}

fun errorScore(line: String): Int {
    val stack = LinkedList<Char>()
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
    val stack = LinkedList<Char>()
    for (c in line) {
        when (c) {
            '(', '[', '{', '<' -> stack.push(c)
            ')', ']', '}', '>' -> stack.pop()
            else -> throw RuntimeException("wat")
        }
    }

    var score = 0L
    while (stack.size > 0) {
        val c = stack.pop()
        score = when (c) {
            '(' -> score * 5 + 1
            '[' -> score * 5 + 2
            '{' -> score * 5 + 3
            '<' -> score * 5 + 4
            else -> throw RuntimeException("wat $c $line")
        }
    }

    return score
}
