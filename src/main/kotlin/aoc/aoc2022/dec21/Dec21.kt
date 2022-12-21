package aoc.aoc2022.dec21

import aoc.ktutils.*
import kotlin.RuntimeException

fun main() {
    check(execute1(readTestLines()).toString(), "152")
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    execute2(readTestLines())
    execute2(readLines())
}

private fun execute1(input: List<String>): Long {
    val ops = parse(input)
    return calc("root", ops)
}

fun calc(label: String, ops: HashMap<String, List<String>>): Long {
    val op = ops[label] ?: throw RuntimeException("op?")
    if (op.size == 1)
        if (op.first().isDigitsOnly()) return op.first().toLong()
        else return calc(op.first(), ops)

    when (op[1]) {
        "+" -> return calc(op[0], ops) + calc(op[2], ops)
        "-" -> return calc(op[0], ops) - calc(op[2], ops)
        "*" -> return calc(op[0], ops) * calc(op[2], ops)
        "/" -> return calc(op[0], ops) / calc(op[2], ops)
    }
    throw RuntimeException("CMH ${op[1]}")
}

private fun execute2(input: List<String>): String {
    val ops = parse(input)
    try {
        return expr("root", ops)
    } catch (e: RuntimeException) {
        return "" + e.message
    }
}

fun expr(label: String, ops: HashMap<String, List<String>>): String {

    if (label == "root") throw RuntimeException(
        "" + (expr(ops[label]!![0], ops) + "=" + expr(ops[label]!![2], ops)))

    if (label == "humn") return "x"

    val op = ops[label] ?: throw RuntimeException("op?")
    if (op.size == 1)
        if (op.first().isDigitsOnly())
            return op.first()

    val op0 = expr(op[0], ops)
    val op2 = expr(op[2], ops)
    if (op0.isDigitsOnly() && op2.isDigitsOnly()) {
        when (op[1]) {
            "+" -> return (op0.toLong() + op2.toLong()).toString()
            "-" -> return (op0.toLong() - op2.toLong()).toString()
            "*" -> return (op0.toLong() * op2.toLong()).toString()
            "/" -> return (op0.toLong() / op2.toLong()).toString()
        }
    }
    return "($op0 ${op[1]} $op2)"
}

private fun parse(input: List<String>): HashMap<String, List<String>> {
    val data = HashMap<String, List<String>>()
    for (line in input) {
        val parts = line.split(": ")
        data.put(parts[0], parts[1].trim().split(" "))
    }

    return data
}