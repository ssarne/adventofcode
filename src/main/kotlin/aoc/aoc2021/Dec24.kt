package aoc.aoc2021

import aoc.ktutils.*

fun main() {
    check(execute(readTestLines(1), "1", 'x'), -1)
    check(execute(readTestLines(2), "11", 'z'), 0)
    check(execute(readTestLines(2), "26", 'z'), 1)
    check(execute(readTestLines(3), "6", 'w'), 0)  //0x0110
    check(execute(readTestLines(3), "6", 'x'), 1)  //0x0110
    check(execute(readTestLines(3), "6", 'y'), 1)  //0x0110
    check(execute(readTestLines(3), "6", 'z'), 0)  //0x0110

    //check(execute(readLines(), "13579246899999", 'z'), 0)
    //check(execute(readLines(), "59996912981939", 'z'), 0)

    val (divZs, addXs, addYs) = analyze(readLines(), false)
    val result = crack(divZs, addXs, addYs)

    result.second.let { println(it); check(it, readAnswerAsLong(1)) }
    result.first.let { println(it); check(it, readAnswerAsLong(2)) }
}

// Look at the programs sections, there are 14 of them which are almost the same
fun analyze(program: List<String>, print: Boolean = false): Triple<IntArray, IntArray, IntArray> {
    val length = program.size / 14
    val matches = IntArray(length)
    val args = Array(length) { IntArray(14) }
    for (i in 0 until length) {
        val line = program[i]
        for (j in 0 until 14) if (line == program[j * length + i]) matches[i]++
    }
    for (i in 0 until length) {
        if (matches[i] < 14) {
            for (j in 0 until 14) {
                args[i][j] = program[j * length + i].split(" ")[2].toInt()
            }
        }
    }
    if (print) for (i in 0 until length) {
        if (matches[i] == 14) println("$i: " + program[i])
        else println("$i: " + program[i] + " " + args[i].toList())
    }
    return Triple(args[4], args[5], args[15])
}

// Brute force the keys, validate using handwritten program snippet
private fun crack(divZs: IntArray, addXs: IntArray, addYs: IntArray): Pair<Long, Long> {
    val cache = HashMap<String, Pair<Long, Long>>()
    val hits = IntArray(14)
    return cracker(LongArray(14), 0, 0L, divZs, addXs, addYs, cache, hits)
}

// Brute force the keys, validate using handwritten program snippet
// Generate all digits in the 14 positions
// Cache results based on z value and iteration
// The z value is the only one that carries overall other regs are
// The content of w, x and y regs are discarded/overwritten
fun cracker(
    input: LongArray,
    iteration: Int,
    z: Long,
    divZs: IntArray,
    addXs: IntArray,
    addYs: IntArray,
    cache: HashMap<String, Pair<Long, Long>>,
    hits: IntArray,
    print: Boolean = false
): Pair<Long, Long> {
    if (print && cache.size % 1000000 == 500000) println("wow - cache.size ${cache.size} - " + hits.asList())
    if (iteration < 14) {
        var high = 0L
        var low = Long.MAX_VALUE
        for (j in 1..9) {
            input[iteration] = j.toLong() // 10L - j.toLong() // 10 - j
            val zz = program(j, iteration, z, divZs, addXs, addYs)
            val key = "$iteration-$zz"
            var res = cache[key]
            if (res == null || res.second != 0L) {
                res = cracker(input, iteration + 1, zz, divZs, addXs, addYs, cache, hits)
                cache[key] = res
            } else {
                hits[iteration]++
            }
            if (res.first < low) low = res.first
            if (res.second > high) high = res.second
        }
        return low to high
    } else {
        if (z == 0L) {
            var res = input.first()
            for (i in 1 until input.size) res = res * 10 + input[i]
            if (print) println("valid: ($res): " + input.asList())
            return res to res
        }
        return Long.MAX_VALUE to 0L
    }
}

fun program(input: Int, iteration: Int, z: Long, divZs: IntArray, addXs: IntArray, addYs: IntArray): Long {
    return snippet(input, divZs[iteration], addXs[iteration], addYs[iteration], z)  // 14
}

// Run one snippet with parameters for what differs between them
fun snippet(input: Int, zDiv1: Int, xAdd2: Int, yAdd3: Int, Z: Long): Long {
    var z = Z                 // from last round
    val w = input.toLong()    // int w             (w is reset, i.e. set to input each round)
    var x = z                 // mul x 0, add x z  (x is reset to 0 each round)
    x %= 26                   // mod x 26
    z /= zDiv1                // div z 1     zDiv1
    x += xAdd2                // add x 11    xAdd2
    x = if (x == w) 1 else 0  // eql x w
    x = if (x == 0L) 1 else 0 // eql x 0
    var y = 25L               // mul y 0, y = 25   (y is reset to 25 each round)
    y *= x                    // mul y x
    y += 1                    // add y 1
    z *= y                    // mul z y
    y = w                     // mul y 0, add y w  (y is set to w)
    y += yAdd3                // add y 16    yAdd3
    y *= x                    // mul y x
    z += y                    // add z y
    return z
}

// Read and execute the program with interpreter
private fun execute(program: List<String>, input: String, output: Char): Long {

    val regs = HashMap<Char, Long>()
    var pos = 0
    var line = 0
    while (line < program.size) {
        line = executeOneInput(program, input[pos++], line, program.size, regs)
    }
    return regs[output]!!
}

// Execute one section of the program
private fun executeOneInput(
    program: List<String>,
    input: Char,
    start: Int,
    end: Int,
    regs: HashMap<Char, Long>,
    print: Boolean = false
): Int {

    var first = true
    for (line in start until end) {
        val instr = program[line].split(" ")
        val op = instr[0]
        val a = instr[1][0]
        val b = if (instr.size > 2) instr[2] else "0"
        val aval = regs.getOrDefault(a, 0)   // assume default value 0, it matters
        val bval = if (b.isSingleChar()) regs.getOrDefault(b[0], 0) else b.toLong()  // default value doesn't matter in task
        if (print) {
            if (op == "inp") println("################ $regs")
            if (op == "inp") print("%s %-2s      ||  %s %d    ".format(op, "" + a, op, asInt(input)))
            else print("%s %-2s %-3s  ||  %s %d %d  ".format(op, "" + a, "" + b, op, aval, bval))
        }
        when (op) {
            "inp" -> {
                if (first) {
                    regs[a] = Character.getNumericValue(input).toLong()
                    first = false
                } else {
                    return line
                }
            }
            "add" -> regs[a] = aval + bval
            "mul" -> regs[a] = aval * bval
            "div" -> regs[a] = aval / bval
            "mod" -> regs[a] = aval % bval
            "eql" -> regs[a] = if (aval == bval) 1L else 0L
        }
        if (print) println("   => $a=" + regs[a])
    }
    return end
}
