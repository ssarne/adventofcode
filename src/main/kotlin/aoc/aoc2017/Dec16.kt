package aoc.aoc2017

import aoc.ktutils.*

fun main() {
    check(dance("abcde", input(readTestText())), "baedc")
    dance("abcdefghijklmnop", input(readText())).let { println(it) ; check(it, readAnswer(1)) }
    dances("abcdefghijklmnop", input(readText()), 1000000000L).let { println(it) ; check(it, readAnswer(2)) }
}

private fun dances(line : String, instructions : List<String>, iters : Long) : String {
    var next = line
    var i = 0L
    do {
        next = dance(next, instructions, i++)
        if (next == line) {
            var j = i
            while (i + j < iters) i += j
        }
    } while (i < iters)
    return next
}

private fun dance(line : String, instructions : List<String>, iter : Long = 0) : String {
    var next = line
    for (move in instructions) {
        when (move[0]) {
            's' -> { // sX
                var i = move.substring(1).toInt()
                var j = next.length - i
                next = next.substring(j) + next.substring(0, j)
            }
            'x' -> { // xA/B
                var a = move.substring(1, move.indexOf('/')).toInt()
                var b = move.substring(move.indexOf('/') + 1).toInt()
                var chars = next.toCharArray()
                var tmp : Char = chars[a]
                chars[a] = chars[b]
                chars[b] = tmp
                next = String(chars)
            }
            'p' -> { // pA/B
                var a = move[1]
                var b = move[3]
                var ap = next.indexOf(a)
                var bp = next.indexOf(b)
                var chars = next.toCharArray()
                var tmp : Char = chars[ap]
                chars[ap] = chars[bp]
                chars[bp] = tmp
                next = String(chars)
            }
        }
    }
    return next
}

private fun input(text: String) : List<String> {
    return text.split(",")
}