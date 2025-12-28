package aoc.aoc2016

import aoc.ktutils.*

fun main() {

    execute1(testLines()).let { println("Test:   $it") ; check(it, 42L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }

    execute2(testLines()).let { println("Test: $it") ; check(it, 42L) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): Long {
    val regs = mutableMapOf('a' to 0L, 'b' to 0L, 'c' to 0L, 'd' to 0L)
    return execute(input, regs)
}

private fun execute2(input: List<String>): Long {
    val regs = mutableMapOf('a' to 0L, 'b' to 0L, 'c' to 1L, 'd' to 0L)
    return execute(input, regs)
}

private fun regOrVal(str: String, regs: MutableMap<Char, Long>): Long {
    if ("abcd".contains(str.first()))
        return regs[str.first()]!!
    return str.toLong()
}

private fun execute(input: List<String>, regs: MutableMap<Char, Long>): Long {

    var ip = 0
    do {
        val instr = input[ip++].split(" ")
        when(instr[0]) {
            "cpy" -> {
                val v = regOrVal(instr[1], regs)
                regs[instr[2].first()] = v
            }
            "inc" -> {
                val reg = instr[1].first()
                regs[reg] = regs[reg]!!.inc()
            }
            "dec" -> {
                val reg = instr[1].first()
                regs[reg] = regs[reg]!!.dec()
            }
            "jnz" -> {
                val v = regOrVal(instr[1], regs)
                if (v != 0L)
                    ip = ip + instr[2].toInt() - 1 // already incremented one at read
            }
        }
    } while(ip < input.size)
    return regs['a']!!.toLong()
}
