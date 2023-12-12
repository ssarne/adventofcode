package aoc.ktutils

import java.lang.RuntimeException

interface Expression
data class ExpressionValue(val value: String) : Expression
data class ExpressionOperation(val left: Expression, val operator: Char, val right: Expression) : Expression

fun parseExpression(expression: String, operators: String, vararg parenthesis: Pair<Char, Char>): Expression {
    var opens = ""
    for (p in parenthesis) opens += p.first
    // FIXME
    return ExpressionValue(expression) // parseExpression(expression, operators, opens, parenthesis)
}

fun parseExpression(expression: String, operators: String, opens: String, parenthesis: Array<Pair<Char, Char>>): Expression {
    if (opens.contains(expression.first())) {
        val pair = parenthesis.find {it.first == expression.first()}
        val close = matchFirstParenthesis(expression)
    }
        return ExpressionValue(expression)
}


/* Match the first/outer parenthesis pair of open and close, e.g. [ and ] */
fun matchFirstParenthesis(text: String, open: Char = '[', close: Char = ']'): Pair<Int, Int> {
    var start = -1
    var lb = 0
    var rb = 0
    for ((i, c) in text.withIndex()) {
        if (c == open) lb++
        if (lb == 1) start = i
        if (c == close) rb++
        if (lb > 0 && lb == rb) return Pair(start, i)
    }
    throw RuntimeException("CMH failed to find matching $open $close in '$text'")
}
