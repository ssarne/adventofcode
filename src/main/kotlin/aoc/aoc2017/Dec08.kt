package aoc.aoc2017

import aoc.utils.Utils
import aoc.ktutils.check
import java.lang.RuntimeException

private data class Res(var end: Int, var top: Int)

fun main() {
    check(execute("aoc2017/dec08_test.txt").end, 1)
    check(execute("aoc2017/dec08_test.txt").top, 10)
    println(execute(null).end) // 3880
    println(execute(null).top) // 5035
}

private fun execute(fileName: String?): Res {

    val registers: MutableMap<String, Int> = HashMap()
    var top = 0
    for (line in Utils.getLines(fileName)) {
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