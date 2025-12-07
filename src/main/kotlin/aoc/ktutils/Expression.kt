package aoc.ktutils

import java.lang.RuntimeException

interface Expression
data class ExpressionValue(val value: String) : Expression
data class ExpressionOperation(val left: Expression, val operator: Char, val right: Expression) : Expression

fun evaluate(e: Expression): Int {
    return when (e) {
        is ExpressionValue -> e.value.toInt()
        is ExpressionOperation -> when (e.operator) {
            '*' -> evaluate(e.left) * evaluate(e.right)
            '/' -> evaluate(e.left) / evaluate(e.right)
            '+' -> evaluate(e.left) + evaluate(e.right)
            '-' -> evaluate(e.left) - evaluate(e.right)
            else -> throw RuntimeException("Missing operator for $e")
        }
        else -> throw RuntimeException("Missing expression type for $e")
    }
}

fun parseExpression(expression: String, operators: String, vararg parenthesis: Pair<Char, Char>): Expression {
    val opens = String(parenthesis
        .map { p -> p.first }
        .toCharArray())

    return parseExpression(expression, operators, opens, arrayOf(*parenthesis))
}

fun parseExpression(expression: String, operators: String, opens: String, parenthesis: Array<Pair<Char, Char>>): Expression {

    val expr = expression.trim()

    if (expr.isEmpty()) {
        throw RuntimeException("Empty expression")
    }
    
    // Handle parentheses first
    if (opens.contains(expr.first())) {
        val pair = parenthesis.find { it.first == expr.first() }!!
        val (start, end) = matchFirstParenthesis(expr, pair.first, pair.second)
        val innerExpr = expr.substring(start + 1, end)
        val parsed = parseExpression(innerExpr, operators, opens, parenthesis)
        
        // Check if there's more after the parentheses
        if (end + 1 < expr.length) {
            val remaining = expr.substring(end + 1).trim()
            if (remaining.isNotEmpty() && operators.contains(remaining.first())) {
                val op = remaining.first()
                val rightExpr = remaining.substring(1).trim()
                val right = parseExpression(rightExpr, operators, opens, parenthesis)
                return ExpressionOperation(parsed, op, right)
            }
        }
        return parsed
    }
    
    // Find lowest precedence operator (rightmost in operators string)
    // Operators appearing first in the string have higher precedence
    var opIndex = -1
    var opChar = ' '
    var opPosition = -1
    
    for (i in expr.indices) {
        val c = expr[i]
        if (operators.contains(c)) {
            val precedence = operators.indexOf(c)
            if (precedence >= opIndex) {
                opIndex = precedence
                opChar = c
                opPosition = i
            }
        }
    }
    
    // If no operator found, it's a value
    if (opPosition == -1) {
        return ExpressionValue(expr)
    }
    
    // Split at the operator
    val left = expr.substring(0, opPosition).trim()
    val right = expr.substring(opPosition + 1).trim()
    
    return ExpressionOperation(
        parseExpression(left, operators, opens, parenthesis),
        opChar,
        parseExpression(right, operators, opens, parenthesis)
    )
}

/* Match the first/outer parenthesis pair of open and close, e.g. [ and ] */
fun matchFirstParenthesis(text: String, open: Char = '[', close: Char = ']'): Pair<Int, Int> {
    val positions = ArrayList<Int>()
    for ((i, c) in text.withIndex()) {
        if (c == open) positions.add(i)
        if (c == close && positions.size == 1) return positions.first() to i
        if (c == close && positions.size > 1) positions.removeLast()
    }
    throw RuntimeException("CMH failed to find matching $open $close in '$text'")
}
