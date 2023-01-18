package aoc.aoc2017

import aoc.ktutils.*
import kotlin.math.*

fun main() {
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }
    // execute2_1(1).let { println(it) }
    // execute2_2(1).let { println(it) }
    execute2_3().let { println(it); check(it, readAnswerAsInt(2)) }

}

private fun execute1(input: List<String>): Int {

    val regs = IntArray('h' - 'a' + 1)
    var count = 0
    var instr = 0

    while (instr in 0 until input.size) {

        val line = input[instr++]

        val (op, xs, ys) = line.trim().split(" ")
        if (op == "mul") count++

        val x = if (xs.isSingleChar()) regs[xs.first() - 'a']!! else ys.toInt()
        val y = if (ys.isSingleChar()) regs[ys.first() - 'a']!! else ys.toInt()

        // println(line)
        when (op) {
            "set" -> regs[xs.first() - 'a'] = y
            "sub" -> regs[xs.first() - 'a'] = regs[xs.first() - 'a']!! - y
            "mul" -> regs[xs.first() - 'a'] = regs[xs.first() - 'a']!! * y
            "jnz" -> if (x != 0) instr = instr - 1 + y
        }
    }
    return count
}

private fun execute2_1(input: Int): Int {

    var a = input
    var b = 0
    var c = 0
    var d = 0
    var e = 0
    var f = 0
    var g = 0
    var h = 0

    a = input
    b = 67 // set b 67
    c = b  // set c b

    // jnz a 2
    // jnz 1 5
    if (a != 0) {
        b *= 100     // mul b 100
        b -= -100000 // sub b -100000
        c = b        // set c b
        c -= -17000  // sub c -17000
    }

    while (true) {
        println(">>> [$a $b $c $d $e $f $g $h]")
        f = 1                // set f 1
        d = 2                // set d 2
        do {
            // println("[$a $b $c $d $e $f $g $h]")
            e = 2            // set e 2
            do {
                g = d        // set g d
                g *= e       // mul g e
                g -= b       // sub g b
                if (g == 0)  // jnz g 2
                    f = 0    // set f 0
                e -= -1      // sub e -1
                g = e        // set g e
                g -= b       // sub g b
            } while (g != 0) // jnz g -8
            d -= -1          // sub d -1
            g = d            // set g d
            g -= b           // sub g b
        } while (g != 0)     // jnz g -13
        if (f == 0)          // jnz f 2
            h -= -1          // sub h -1
        g = b                // set g b
        g -= c               // sub g c
        if (g == 0)          // jnz g 2
            break            // jnz 1 3
        b -= 17              // sub b -17
    }                        // jnz 1 -23

    return h
}

private fun execute2_2(): Int {

    var b = 67 * 100 + 100000
    var c = b + 17000
    var h = 0

    for (i in b..c step 17) {
        var f = true // set f 1
        for (d in 2..b) {
            for (e in 2..b) {
                if (d * e == b) {
                    f = false
                }
            }
        }
        if (!f) h++
    }

    return h
}

private fun execute2_3(): Int {

    var b = 67 * 100 + 100000
    var c = b + 17000 // set c b
    var h = 0

    val sieve = BooleanArray(c + 1) { true }
    for (i in 2..c)
        for (j in 2 * i..c step i)
            sieve[j] = false // mark as non prime

    // check range b-c
    for (i in b..c step 17)
        if (!sieve[i]) h++

    return h
}

