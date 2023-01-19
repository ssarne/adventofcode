package aoc.aoc2017

import aoc.ktutils.*
import java.util.BitSet
import kotlin.math.*

fun main() {
    check(execute1(readTestLines()), 3)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }
}

private data class Operation(val write: Boolean, val move: Int, val state: Char)
private data class State(val id: Char, val op0: Operation, val op1: Operation)
private data class Tape(val left: BitSet, val right: BitSet) {
    fun get(pos: Int) = if (pos >= 0) right[pos] else left[Math.abs(pos)]
    fun set(pos: Int, value: Boolean) = if (pos >= 0) right[pos] = value else left[abs(pos)] = value
    fun size() = left.stream().count() + right.stream().count()
}

private fun execute1(input: List<String>): Long {

    val (start, check, states) = parse(input)
    val tape = Tape(BitSet(), BitSet())
    var state = states[start]!!
    var pos = 0

    for (i in 0 until check) {
        if (tape.get(pos)) {  // Value is 1
            tape.set(pos, state.op1.write)
            pos += state.op1.move
            state = states[state.op1.state]!!
        } else {
            tape.set(pos, state.op0.write)
            pos += state.op0.move
            state = states[state.op0.state]!!
        }
    }
    return tape.size()
}

private fun parse(input: List<String>): Triple<Char, Int, Map<Char, State>> {
    var start = '0'
    var check = 0
    var states = HashMap<Char, State>()
    var moves = mapOf("right." to 1, "left." to -1)

    var i = 0
    while (i < input.size) {
        val line = input[i++]
        if (line == "") continue
        if (line.startsWith("Begin in state"))
            start = line.replace(".", "").split(" ")[3].first()

        if (line.startsWith("Perform a diagnostic"))
            check = line.split(" ")[5].toInt()

        if (line.startsWith("In state ")) {
            val state = line.split(" ")[2].replace(":", "").first()
            val w0 = input[i+1].trim().split(" ")[4].replace(".", "").toInt()
            val m0 = moves[input[i+2].trim().split(" ")[6]]!!
            val s0 = input[i+3].trim().split(" ")[4].replace(".", "").first()
            val w1 = input[i+5].trim().split(" ")[4].replace(".", "").toInt()
            val m1 = moves[input[i+6].trim().split(" ")[6]]!!
            val s1 = input[i+7].trim().split(" ")[4].replace(".", "").first()
            states[state] = State(state, Operation(w0 == 1, m0, s0), Operation(w1 == 1, m1, s1))
            i += 8
        }
    }

    return Triple(start, check, states)
}