package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.lang.RuntimeException

private data class Res(var end: Int, var top: Int)

fun main() {
    check(execute(readTestLines()).end, 1)
    check(execute(readTestLines()).top, 10)
    println(execute(readLines()).end) // 3880
    println(execute(readLines()).top) // 5035
}

private fun execute(input: List<String>): Res {

    val registers: MutableMap<String, Int> = HashMap()
    var top = 0
    for (line in input) {
        var instr = line.split(" ")
        var cur = registers.getOrDefault(instr[0], 0)
        var ccur = registers.getOrDefault(instr[4], 0)
        var cval = instr[6].toInt()
        var inc = when (instr[1]) {
            "inc" -> instr[2].toInt()
            "dec" -> -1 * instr[2].toInt()
            else -> throw RuntimeException("CMH")
        }
        var res = when (instr[5]) {
            "<" -> if (ccur < cval) cur + inc else cur
            "<=" -> if (ccur <= cval) cur + inc else cur
            ">" -> if (ccur > cval) cur + inc else cur
            ">=" -> if (ccur >= cval) cur + inc else cur
            "==" -> if (ccur == cval) cur + inc else cur
            "!=" -> if (ccur != cval) cur + inc else cur
            else -> throw RuntimeException("CMH")
        }
        registers[instr[0]] = res
        if (res > top) top = res
    }

    var ret = registers.values.first()
    for (r in registers.values)
        if (r > ret)
            ret = r
    return Res(ret, top)
}