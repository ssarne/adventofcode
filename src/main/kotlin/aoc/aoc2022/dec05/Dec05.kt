package aoc.aoc2022.dec05

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), "CMZ")
    execute1(readLines()).let { println(it); check(it, readAnswer(1)) }

    check(execute2(readTestLines()), "MCD")
    execute2(readLines()).let { println(it); check(it, readAnswer(2)) }
}

private fun execute1(input: List<String>): String {
    val stacks = parseStacks(input)
    val moves = parseMoves(input)
    for ((size, from, to) in moves) {
        for (i in 0 until size) {
            stacks[to].add(stacks[from].removeAt(stacks[from].size - 1))
        }
    }
    return getTops(stacks)
}

private fun execute2(input: List<String>): String {
    val stacks = parseStacks(input)
    val moves = parseMoves(input)
    for ((size, from, to) in moves) {
        val pos = stacks[from].size - size
        for (i in 0 until size) {
            stacks[to].add(stacks[from].removeAt(pos))
        }
    }
    return getTops(stacks)
}

private fun getTops(stacks: ArrayList<ArrayList<Char>>): String {
    var res = ""
    for (stack in stacks)
        if (!stack.isEmpty())
            res += stack.last()

    return res
}

private fun parseMove(line: String): Triple<Int, Int, Int> {
    val tokens = line.split(" ")
    val size = tokens[1].toInt()
    val from = tokens[3].toInt()
    val to = tokens[5].toInt()
    return Triple(size, from, to)
}

fun parseMoves(input: List<String>): List<Triple<Int, Int, Int>> {
    val moves = ArrayList<Triple<Int, Int, Int>>()
    for (line in input) {
        if (line.startsWith("move")) {
            moves.add(parseMove(line))
        }
    }
    return moves
}

private fun parseStacks(input: List<String>): ArrayList<ArrayList<Char>> {

    var num = 0
    for (line in input)
        if (line.startsWith(" 1"))
            num = line.trim().last().toString().toInt()

    val stacks = ArrayList<ArrayList<Char>>()
    for (i in 0..num) stacks.add(ArrayList())

    for (line in input) {
        if (line == "") break
        for (i in 1..line.length step 4) {
            if (line[i] != ' ' && !line[i].isDigit()) {
                val stack = 1 + i / 4
                stacks[stack]!!.add(0, line[i])
            }
        }
    }
    return stacks
}
