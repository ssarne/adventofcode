package aoc.aoc2022.dec21

import aoc.ktutils.*
import kotlin.RuntimeException

fun main() {
    check(execute1(readTestLines()), 152L)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines()), 301L)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    val ops = parse(input)
    return calc1("root", ops)
}

fun calc1(label: String, ops: HashMap<String, List<String>>): Long {
    val op = ops[label]!!
    if (op.size == 1)
        return if (op.first().isDigitsOnly()) op.first().toLong()
        else calc1(op.first(), ops)

    when (op[1]) {
        "+" -> return calc1(op[0], ops) + calc1(op[2], ops)
        "-" -> return calc1(op[0], ops) - calc1(op[2], ops)
        "*" -> return calc1(op[0], ops) * calc1(op[2], ops)
        "/" -> return calc1(op[0], ops) / calc1(op[2], ops)
    }
    throw RuntimeException("CMH ${op[1]}")
}

private fun execute2(input: List<String>): Long {
    val ops = parse(input)
    val op = ops["root"]!!
    val exp1 = calc2(op[0], ops)
    val exp2 = calc2(op[2], ops)
    if (exp1 == null && exp2 != null) return solve(op[0], exp2, ops)
    if (exp1 != null && exp2 == null) return solve(op[2], exp1, ops)
    throw RuntimeException("CMH: $exp1 $exp2")
}

fun calc2(label: String, ops: HashMap<String, List<String>>): Long? {

    if (label == "humn")
        return null

    val op = ops[label]!!
    if (op.size == 1)
        return if (op.first().isDigitsOnly()) op.first().toLong()
        else calc1(op.first(), ops)

    val op1 = calc2(op[0], ops)
    val op2 = calc2(op[2], ops)
    if (op1 == null || op2 == null) return null

    when (op[1]) {
        "+" -> return op1 + op2
        "-" -> return op1 - op2
        "*" -> return op1 * op2
        "/" -> return op1 / op2
    }
    throw RuntimeException("CMH ${op[1]}")
}

fun solve(label: String, other: Long, ops: HashMap<String, List<String>>): Long {

    if (label == "humn")
        return other

    val op = ops[label]!!
    val exp1 = calc2(op[0], ops)
    val exp2 = calc2(op[2], ops)

    if (exp1 == null && exp2 == null) throw RuntimeException("CMH")
    if (exp1 != null && exp2 != null) throw RuntimeException("CMH")

    if (exp1 == null) {
        val value = exp2!!
        return when (op[1]) {
            "+" -> solve(op[0], other - value, ops)
            "-" -> solve(op[0], other + value, ops)
            "*" -> solve(op[0], other / value, ops)
            "/" -> solve(op[0], other * value, ops)
            else -> throw RuntimeException("CMH: $op -> $exp1 ${op[1]} $exp2")
        }
    } else {
        val value = exp1
        return when (op[1]) {
            "+" -> solve(op[2], other - value, ops)
            "-" -> solve(op[2], value - other, ops)
            "*" -> solve(op[2], other / value, ops)
            "/" -> solve(op[2], value / other, ops)
            else -> throw RuntimeException("CMH: $op -> $exp1 ${op[1]} $exp2")
        }
    }
}

private fun parse(input: List<String>): HashMap<String, List<String>> {
    val data = HashMap<String, List<String>>()
    for (line in input) {
        val parts = line.split(": ")
        data[parts[0]] = parts[1].trim().split(" ")
    }
    return data
}