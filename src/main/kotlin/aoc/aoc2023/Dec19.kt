package aoc.aoc2023


import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap

fun main() {
    check(execute1(readTestLines(1)), 19114)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(1)), 167409079868000L)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {
    val (rules, parts) = parse(input)
    return parts.sumOf { part -> evaluate1("in", part, rules) }
}

private fun execute2(input: List<String>): Long {
    val (rules, _) = parse(input)
    return evaluate2("in", mapOf('x' to 1..4000, 'm' to 1..4000, 'a' to 1..4000, 's' to 1..4000), rules)
}

fun evaluate1(expr: String, part: Map<Char, Int>, rules: Map<String, String>): Int {

    //println("evaluate1: $exprs $part")

    if (expr.first() == 'A') return part.values.sum()
    if (expr.first() == 'R') return 0

    if (expr[1] != '<' && expr[1] != '>') { // hdj,R   pv   px,qqz
        val rule = if (expr.indexOf(",") == -1) expr else expr.substring(0, expr.indexOf(","))
        return evaluate1(rules[rule]!!, part, rules)
    }

    val p = expr[0]
    val n = part[p]!!
    val op = expr[1]
    val cmp = expr.substring(2, expr.indexOf(":")).toInt()
    val act = expr.substring(expr.indexOf(":") + 1, expr.indexOf(","))
    val rest = expr.substring(expr.indexOf(",") + 1)

    return when (op) { // a>1716:R,A
        '>' -> if (n > cmp) evaluate1(act, part, rules) else evaluate1(rest, part, rules)
        '<' -> if (n < cmp) evaluate1(act, part, rules) else evaluate1(rest, part, rules)
        else -> throw RuntimeException("CMH $op $expr")
    }

}

fun evaluate2(expr: String, parts: Map<Char, IntRange>, rules: Map<String, String>): Long {

    //println("evalute2: $exprs $part")

    if (expr.first() == 'A')
        return parts.values.fold(1L) {acc, range -> acc * (range.last - range.first + 1)}

    if (expr.first() == 'R')
        return 0L

    if (expr[1] != '<' && expr[1] != '>') { // hdj,R   pv   px,qqz
        val rule = if (expr.indexOf(",") == -1) expr else expr.substring(0, expr.indexOf(","))
        return evaluate2(rules[rule]!!, parts, rules)
    }

    val p = expr[0]
    val op = expr[1]
    val cmp = expr.substring(2, expr.indexOf(":")).toInt()
    val act = expr.substring(expr.indexOf(":") + 1, expr.indexOf(","))
    val rest = expr.substring(expr.indexOf(",") + 1)
    val range = parts[p]!!

    when (op) {
        '>' -> {
            return if (cmp in range) {
                val pass = parts.toMutableMap()
                val fail = parts.toMutableMap()
                pass[p] = cmp + 1..parts[p]!!.last
                fail[p] = parts[p]!!.first..cmp
                evaluate2(act, pass, rules) + evaluate2(rest, fail, rules)
            } else if (cmp < range.first) { // all pass
                evaluate2(act, parts, rules)
            } else if (cmp > range.last) { // all fail
                evaluate2(rest, parts, rules)
            } else {
                throw RuntimeException("CMH")
            }
        }
        '<' -> {
            return if (cmp in range) {
                val pass = parts.toMutableMap()
                val fail = parts.toMutableMap()
                pass[p] = parts[p]!!.first until cmp
                fail[p] = cmp..parts[p]!!.last
                evaluate2(act, pass, rules) + evaluate2(rest, fail, rules)
            } else if (cmp > range.first) { // all fail
                evaluate2(rest, parts, rules)
            } else if (cmp < range.first) { // all pass
                evaluate2(act, parts, rules)
            } else {
                throw RuntimeException("CMH")
            }
        }
        else -> {
            throw RuntimeException("CMH $expr")
        }
    }
}

private fun parse(input: List<String>): Pair<Map<String, String>, List<Map<Char, Int>>> {

    val rules = HashMap<String, String>()
    val parts = LinkedList<HashMap<Char, Int>>()

    for (line in input) {

        if (line.isEmpty()) continue

        // {x=787,m=2655,a=1222,s=2876}
        if (line.startsWith('{')) {
            val ps = line.replace("{", "").replace("}", "").split(",")
            val part = HashMap<Char, Int>()
            for (p in ps) {
                val key = p.split("=")[0].first()
                val num = p.split("=")[1].toInt()
                part[key] = num
            }
            parts.add(part)
            continue
        }

        // px{a<2006:qkq,m>2090:A,rfg}
        val name = line.split("{")[0]
        val expr = line.split("{")[1].replace("}", "")
        rules[name] = expr
    }
    return rules to parts
}
