package aoc.aoc2024

import aoc.ktutils.*

fun main() {

    executeProgram("2,6", 0, 0, 9).let { check(it.second.second, 1) }
    executeProgram(32916674).let { check(it.first.joinToString(","), "7,1,2,3,2,6,7,2,5") }

    execute1(readTestLines(1)).let { check(it, "4,6,3,5,6,3,5,2,1,0") }
    execute1(readLines()).let { println(it); check(it, readAnswer(1)) }

    execute2(readTestLines(2)).let { check(it, 117440) }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): String {

    val (program, registers) = parse(input)
    val (out, _) = executeProgram(program, registers.first, registers.second, registers.third)
    return out.joinToString(separator = ",")
}

private fun parse(input: List<String>): Pair<String, Triple<Long, Long, Long>> {

    val registers = longArrayOf(0, 0, 0)
    var program = ""

    for (line in input) {
        if (line == "") continue
        if (line.startsWith("Register A: "))
            registers[0] = line.substring("Register A: ".length).toLong()
        if (line.startsWith("Register B: "))
            registers[1] = line.substring("Register B: ".length).toLong()
        if (line.startsWith("Register C: "))
            registers[2] = line.substring("Register C: ".length).toLong()
        if (line.startsWith("Program: ")) {
            program = line.substring("Program: ".length)
        }
    }
    return program to Triple(registers[0], registers[1], registers[2])
}

private fun executeProgram(instructions: String, ai: Long, bi: Long, ci: Long): Pair<List<Long>, Triple<Long, Long, Long>> {
    val program = instructions.split(",").map { it.toInt() }.toList()
    return executeProgram(program, ai, bi, ci)
}

private fun executeProgram(program: List<Int>, ai: Long, bi: Long, ci: Long): Pair<List<Long>, Triple<Long, Long, Long>> {

    val out = mutableListOf<Long>()
    var a = ai
    var b = bi
    var c = ci
    var ip = 0

    while (ip < program.size) {
        val instr = program[ip]
        val literal = program[ip + 1]
        val combo = readCombo(a, b, c, program[ip + 1])
        // println("$ip: ${instr} $literal / $combo || [$a $b $c]")
        when (instr) {
            0 -> a = (a / (2L pow combo))
            1 -> b = (b xor literal.toLong())
            2 -> b = combo % 8
            3 -> if (a != 0L) ip = literal - 2
            4 -> b = b xor c
            5 -> out.add(combo % 8)
            6 -> b = (a / (2L pow combo))
            7 -> c = (a / (2L pow combo))
            else -> throw RuntimeException("CMH $instr")
        }
        ip += 2
    }
    return out to Triple(a, b, c)
}

private fun readCombo(a: Long, b: Long, c: Long, i: Int): Long {
    return when (i) {
        0 -> 0
        1 -> 1
        2 -> 2
        3 -> 3
        4 -> a
        5 -> b
        6 -> c
        7 -> throw RuntimeException("CMH $i")
        else -> throw RuntimeException("CMH $i")
    }
}

// Manual implementation of Program: 2,4,1,1,7,5,0,3,1,4,4,0,5,5,3,0
// --------------------------
// b = a % 8           // 2,4 -> b = combo(4) % 8
// b = b xor 1         // 1,1 - >b = b xor 1
// c = a / (2 pow b)   // 7,5 -> a / 2^combo(5)
// a = a / (2 pow 3)   // 0,3 -> shift 3 steps / div by 8
// b = b xor 4         // 1,4
// b = b xor 0         // 4,0
// out b % 8           // 5,5 -> out combo(5) % 8
// --------------------------
// if (a != 0) goto 0  // 3,0
// b depends on a
// c depends on a
// out depends on b and c
// a is shifted 3 steps
// so only a is carried
private fun executeProgram(input: Long): Pair<List<Long>, Triple<Long, Long, Long>> {

    val out = mutableListOf<Long>()
    var a = input
    var b = 0L
    var c = 0L
    do {
        b = a % 8           // 2,4 -> b = combo(4) % 8
        b = b xor 1         // 1,1 - >b = b xor 1
        c = a / (2L pow b)  // 7,5 -> a / 2^combo(5)
        a = a / (2L pow 3)  // 0,3 -> shift 3 steps / div by 8
        b = b xor 4L        // 1,4
        b = b xor c         // 4,0
        out.add(b % 8)      // 5,5 -> out combo(5) % 8
    } while (a != 0L)       // 3,0
    return out to Triple(a, b, c)
}

private fun execute2(input: List<String>): Long {

    val (program, _) = parse(input)
    val instructions = program.split(",").map { it.toLong() }.toList()

    var ais = mutableListOf(0L) // possible input values for register A
    for (i in (instructions.lastIndex downTo 0)) {
        val expected = instructions[i]
        val nextAis = mutableListOf<Long>()
        for (ai in ais) {
            for (n in 0 until 8) {
                val ain = ai * 8 + n
                val (out, _) = executeProgram(program, ain, 0, 0)
                val output = out.first()
                if (output == expected)
                    nextAis.add(ain)
            }
        }
        ais = nextAis
    }

    return ais.min()
}
