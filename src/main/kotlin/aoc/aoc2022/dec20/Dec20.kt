package aoc.aoc2022.dec20

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 3L)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines()), 1623178306L)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class LL(val v: Long) {
    var value = v
    lateinit var left: LL
    lateinit var right: LL
}

private fun execute1(input: List<String>): Long {

    val pos = parse(input)
    var first = mixit(pos, pos.size, pos[0])
    return result(first)
}

private fun result(first: LL): Long {
    var next = first
    while (next.value != 0L) next = next.right
    for (i in 0 until 1000) next = next.right
    val n1 = next.value
    for (i in 0 until 1000) next = next.right
    val n2 = next.value
    for (i in 0 until 1000) next = next.right
    val n3 = next.value

    return n1 + n2 + n3
}

private fun printLL(ll: LL, msg: String) {
    var next = ll
    do {
        print("${next.value} ")
        next = next.right
    } while (next != ll)
    println(msg)
}

private fun execute2(input: List<String>): Long {

    val pos = parse(input)
    val key = 811589153L
    for (i in pos.indices)
        pos[i].value = pos[i].value * key

    var first = pos[0]
    for (r in 1..10) {
        first = mixit(pos, pos.size, first)
    }

    return result(first)
}

private fun mixit(pos: ArrayList<LL>, size: Int, first: LL): LL {

    var head = first

    for (i in pos.indices) {
        val node = pos[i]
        val steps = Math.floorMod(node.value, size - 1)

        //  printLL(head, "  $steps")
        if (node == head) head = node.right

        for (j in 0 until steps) {
            val l1 = node.left
            val r1 = node.right
            val r2 = node.right.right

            l1.right = r1
            r1.left = l1

            r1.right = node
            node.left = r1
            r2.left = node
            node.right = r2
        }
    }
    //printLL(head, "  ")
    return head
}

/** Create a linked list, store the nodes in an array, at their initial position */
private fun parse(input: List<String>): ArrayList<LL> {
    val numbers = ArrayList<Int>()
    for (line in input) {
        numbers.add(line.toInt())
    }

    val size = numbers.size
    val positions = ArrayList<LL>()
    for (i in 0 until size) {
        positions.add(LL(numbers[i].toLong()))
    }
    for (i in 0 until size) {
        positions[i].left = positions[(size + i - 1) % size]
        positions[i].right = positions[(i + 1) % size]
    }
    return positions
}