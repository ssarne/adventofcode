package aoc.aoc2016

import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    execute1(testLines()).let { println("Test:   $it") ; check(it, 3L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }

    execute2(testLines()).let { println("Test:   $it") ; check(it, 2L) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private data class Elf(val id: Int, var left: Elf?, var right: Elf?) {
    override fun toString() = "Elf $id"
}

private fun execute1(input: List<String>): Long {
    val n = input.first().toInt()
    val elves = LinkedList<Int>()
    for (i in 1 .. n) elves.add(i)
    var skip = false
    while (elves.size > 1) {
        val iter = elves.iterator()
        while (iter.hasNext()) {
            iter.next()
            if (skip) iter.remove()
            skip = !skip
        }
    }
    return elves.first().toLong()
}


private fun execute2(input: List<String>): Long {

    // Solve for first couple of 100s to identify pattern
    // Start at 10:1
    // Inc 1 until winner == n/2
    // Inc 2 until winner == n
    // Restart at 1

    val tests = mutableListOf(0)
    for (i in 1 .. 250) tests.add(execute2(i).toInt())
    // for ((i, w) in tests.withIndex()) println("$i: $w")

    val n = input.first().toInt()
    if (n < tests.size) return tests[n].toLong()
    var w = 0
    for (i in 10 .. n) {
        w = if (2 * w < i - 1) w + 1
        else if (w < i - 1) w + 2
        else 1

        if (i < tests.size) check(w, tests[i])
    }
    return w.toLong()
}

private fun execute2(input: Int): Long {

    var n = input
    val elves = ArrayList<Elf>()
    for (i in 1 .. n) elves.add(Elf(i, null, null))
    for (i in elves.indices) {
        elves[i].left = if (i + 1 == elves.size) elves[0] else elves[i+1]
        elves[i].right = if (i - 1 < 0) elves.last() else elves[i-1]
    }

    var current = elves[0]
    while (current.left != current) {
        if (n % 1000 == 0) println("elves=${n}")
        val steps = n / 2
        var target = current.left
        for (i in 1 until steps) target = target!!.left
        target!!.left!!.right = target.right
        target.right!!.left = target.left
        n--
        current = current.left!!
    }
    return current.id.toLong()
}