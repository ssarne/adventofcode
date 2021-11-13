package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readText

fun main() {
    check(parse("{}").score, 1)
    check(parse("{{{}}}").score, 6)
    check(parse("{{},{}}").score, 5)
    check(parse("{{{},{},{{}}}}").score, 16)
    check(parse("{<a>,<a>,<a>,<a>}").score, 1)
    check(parse("{{<ab>},{<ab>},{<ab>},{<ab>}}").score, 9)
    check(parse("{{<!!>},{<!!>},{<!!>},{<!!>}}").score, 9)
    check(parse("{{<a!>},{<a!>},{<a!>},{<ab>}}").score, 3)
    println(parse(readText()).score) // 17537
    check(parse("<>").garbage, 0)
    check(parse("<random characters>").garbage, 17)
    check(parse("<<<<>").garbage, 3)
    check(parse("<{!>}>").garbage, 2)
    check(parse("<!!>").garbage, 0)
    check(parse("<!!!>>").garbage, 0)
    check(parse("<{o\"i!a,<{i<a>").garbage, 10)
    println(parse(readText()).garbage) // 7539
}

private data class Res09(var score: Int, var garbage: Int)

private fun parse(input: String): Res09 {
    var stream = input.toCharArray()
    var garbage = clean(stream)
    var sum = count(stream, 0, stream.size - 1)
    return Res09(sum, garbage)
}

private fun clean(stream: CharArray): Int {

    var garbage = false
    var sum = 0
    for (i in stream.indices) {
        when (stream[i]) {
            '<' -> {
                if (!garbage) {
                    garbage = true
                    continue
                }
            }
            '!' -> {
                stream[i + 1] = '_'
                sum -= 2
            }
            '>' -> {
                garbage = false
                continue
            }
        }
        if (garbage) {
            stream[i] = '_'
            sum++
        }
    }
    return sum
}

private fun count(stream: CharArray, start: Int, end: Int): Int {
    if (start >= stream.size || start == end)
        return 0

    var depth = 0
    var sum = 0
    var s = -1
    for (i in start..end) {
        when (stream[i]) {
            '{' -> {
                if (depth == 0) s = i
                depth++
                sum += 1
            }
            '}' -> {
                if (depth == 1) sum += count(stream, s + 1, i - 1)
                depth--
            }
        }
    }
    return sum
}