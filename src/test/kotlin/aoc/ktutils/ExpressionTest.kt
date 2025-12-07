package aoc.ktutils

import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test

class ExpressionTest {

    @Test
    fun parseExpressionTest() {
        val e1 = parseExpression("(3*5)+[(3+5*2)*(2)]", "*+", '(' to ')', '[' to ']')
        assertEquals(15+26, evaluate(e1))

        val e2 = parseExpression("5+(3*5)", "*+", '(' to ')', '[' to ']')
        assertEquals(5+15, evaluate(e2))

        val e3 = parseExpression("((((((1))))))", "*+", '(' to ')', '[' to ']')
        assertEquals(1, evaluate(e3))
    }

    @Test
    fun matchFirstParenthesis() {
        assertEquals(0 to 4, matchFirstParenthesis("(3*5)", '(', ')'))
        assertEquals(0 to 3, matchFirstParenthesis("(())()", '(', ')'))
        assertEquals(1 to 2, matchFirstParenthesis("[()]", '(', ')'))
        assertEquals(0 to 3, matchFirstParenthesis("[()]", '[', ']'))
        val e1 = assertThrows(RuntimeException::class.java) {matchFirstParenthesis("[[]")}
        assertEquals("CMH failed to find matching [ ] in '[[]'", e1.message)
        val e2 = assertThrows(RuntimeException::class.java) {matchFirstParenthesis(")()")}
        assertEquals("CMH failed to find matching [ ] in ')()'", e2.message)
    }
}