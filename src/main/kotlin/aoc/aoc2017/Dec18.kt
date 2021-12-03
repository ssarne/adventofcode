package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.util.*

fun main() {
    check(execute1(readTestLines()), 4L)
    println(execute1(readLines())) // 4601

    check(execute2(readTestLines(2)), 3L)
    println(execute2(readLines())) // 6858
}

private fun execute1(program: List<String>): Long {

    var line = 0
    var regs = HashMap<Char, Long>()
    var sound = 0L
    while (true) {

        var instr = program[line++].split(" ")
        var op = instr[0]
        var reg = instr[1].first()
        var rv1 = if (instr.size > 1) instr[1].toLongOrNull() ?: regs.getOrDefault(reg, 0L) else 0L
        var rv2 = if (instr.size > 2) instr[2].toLongOrNull() ?: regs.getOrDefault(instr[2].first(), 0L) else 0L

        when (op) {
            "snd" -> {sound = rv1}
            "set" -> {regs[reg] = rv2}
            "add" -> {regs[reg] = rv1 + rv2}
            "mul" -> {regs[reg] = rv1 * rv2}
            "mod" -> {regs[reg] = rv1 % rv2}
            "rcv" -> {if (sound != 0L) return sound}
            "jgz" -> {if (rv1 > 0) line = (line + rv2 - 1).toInt()}
        }
    }
}

private fun execute2(program: List<String>): Long {
    var program0 = Program(0L, program, LinkedList<Long>(), LinkedList<Long>())
    var program1 = Program(1L, program, program0.output, program0.input)
    do {
        program0.execute()
        program1.execute()
    } while (program0.input.size > 0 || program1.input.size > 0)

    return program1.snds
}

private class Program(val id: Long, val program: List<String>, val input: Queue<Long>, val output: Queue<Long>) {

    var regs = HashMap<Char, Long>()
    var snds = 0L
    var rcvs = 0L
    var pc = 0

    init {regs['p'] = id}

    fun execute() {

        //println("[$id] starting pc=$pc")

        while (true) {
            var instr = this.program[pc++].split(" ")
            var op = instr[0]
            var reg = instr[1].first()
            var rv1 = if (instr.size > 1) instr[1].toLongOrNull() ?: regs.getOrDefault(reg, 0L) else 0L
            var rv2 = if (instr.size > 2) instr[2].toLongOrNull() ?: regs.getOrDefault(instr[2].first(), 0L) else 0L

            // println("[$id][$pc] $op $reg ($rv1) $rv2")
            when (op) {
                "set" -> { regs[reg] = rv2 }
                "add" -> { regs[reg] = rv1 + rv2 }
                "mul" -> { regs[reg] = rv1 * rv2 }
                "mod" -> { regs[reg] = rv1 % rv2 }
                "jgz" -> { if (rv1 > 0) pc = (pc + rv2 - 1).toInt() }
                "snd" -> { snds++; output.add(rv1) } // ;println("[$id] send $rv1")}
                "rcv" -> {
                    if (input.size > 0) { regs[reg] = input.remove() ; rcvs++ }
                    else { pc-- ; break }
                }
            }
        }
    }
}
