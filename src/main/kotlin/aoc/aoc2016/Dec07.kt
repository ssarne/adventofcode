package aoc.aoc2016

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    check(execute1(readTestLines(1)), 3)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(2)), 3)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        var expr = line
        var ins = false
        var out = false
        while (expr.length > 0) {
            val open = expr.indexOf('[')
            val close = expr.indexOf(']')
            val outside = if (open != -1) expr.substring(0, open) else expr
            val inside = if (open != -1) expr.substring(open + 1, close) else ""
            out = out || hasABBA(outside)
            ins = ins || hasABBA(inside)
            expr = if (open != -1) expr.substring(close + 1) else ""
        }

        if (out && !ins) {
            sum++
        }
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        val insides = mutableSetOf<String>()
        val outsides = mutableSetOf<String>()
        var open = -1
        var close = -1
        var out = true
        while (open < line.length) {
            if (out) {
                open = line.indexOf("[", close)
                if (open == -1) open = line.length
                outsides.add(line.substring(close + 1, open))
                out = false
            } else {
                close = line.indexOf("]", open)
                insides.add(line.substring(open + 1, close))
                out = true
            }
        }

        val abasOutside = mutableSetOf<String>()
        for (str in outsides)
            abasOutside.addAll(findABAs(str))
        val babsInside = mutableSetOf<String>()
        for (aba in abasOutside)
            babsInside.add("" + aba[1] + aba[0] + aba[1])

        var match = false
        for (str in insides)
            for (bab in babsInside)
                if (str.contains(bab))
                    match = true

        if (match)
            sum++
    }
    return sum
}

private fun hasABBA(str: String): Boolean {
    for (i in 0..str.length - 4)
        if (str[0 + i] == str[3 + i] && str[1 + i] == str[2 + i] && str[0 + i] != str[1 + i])
            return true
    return false
}

private fun findABAs(str: String): Set<String> {
    val abas = mutableSetOf<String>()
    for (i in 0..str.length - 3)
        if (str[0 + i] == str[2 + i] && str[0 + i] != str[1 + i])
            abas.add(str.substring(i, i+3))
    return abas
}
