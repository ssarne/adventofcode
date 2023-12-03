package aoc.aoc2021


import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(parseAndResolve(readTestLines(1)).toString(), "[[[[1,1],[2,2]],[3,3]],[4,4]]")
    check(parseSNF("[[[[[9,8],1],2],3],4]").reduce().toString(), "[[[[0,9],2],3],4]")
    check(parseSNF("[7,[6,[5,[4,[3,2]]]]]").reduce().toString(), "[7,[6,[5,[7,0]]]]")
    check(parseSNF("[[6,[5,[4,[3,2]]]],1]").reduce().toString(), "[[6,[5,[7,0]]],3]")
    check(parseSNF("[[3,[2,[1,[7,3]]]],[6,[5,[4,[3,2]]]]]").reduce().toString(), "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    check(parseSNF("[[3,[2,[8,0]]],[9,[5,[4,[3,2]]]]]").reduce().toString(), "[[3,[2,[8,0]]],[9,[5,[7,0]]]]")
    check(parseAndResolve(readTestLines(7)).toString(), "[[[[3,0],[5,3]],[4,4]],[5,5]]")
    check(parseAndResolve(readTestLines(8)).toString(), "[[[[5,0],[7,4]],[5,5]],[6,6]]")
    check(parseAndResolve(readTestLines(9)).toString(), "[[[[4,0],[5,4]],[[7,7],[6,0]]],[[8,[7,7]],[[7,9],[5,0]]]]")
    check(parseAndResolve(readTestLines(10)).toString(), "[[[[8,7],[7,7]],[[8,6],[7,7]]],[[[0,7],[6,6]],[8,7]]]")
    check(parseSNF("[9,1]").magnitude(), 29)
    check(parseSNF("[1,9]").magnitude(), 21)
    check(parseSNF("[[9,1],[1,9]]").magnitude(), 129)
    check(parseSNF("[[1,2],[[3,4],5]]").magnitude(), 143)
    check(parseAndResolve(readTestLines(15)).magnitude(), 4140)

    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(15)), 3993)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    return parseAndResolve(input).magnitude()
}

private fun execute2(input: List<String>): Int {
    var largest = 0
    for (i in input.indices) {
        for (j in i + 1 until input.size) {
            val m1 = parseAndResolve(listOf(input[i], input[j])).magnitude()
            val m2 = parseAndResolve(listOf(input[j], input[i])).magnitude()
            largest = max(largest, max(m1, m2))
        }
    }
    return largest
}

private fun parseAndResolve(input: List<String>, trace: Boolean = false): SNF {
    var snf = parseSNF(input[0])
    snf.reduce(0, trace)
    for (i in 1 until input.size) {
        snf = SNFPair(snf, parseSNF(input[i]))
        snf.reduce(0, trace)
    }
    return snf
}

private sealed class SNF {
    fun reduce(depth: Int = 0, trace: Boolean = false): SNF {
        if (trace) println("Reduce: $this")
        do {
            var action = explode(depth)
            if (!action.changed) action = split()
            if (trace) println("$action   $this")
        } while (action.changed)
        return this
    }

    abstract fun explode(depth: Int): SNFAction
    abstract fun split(): SNFAction
    abstract fun incFromLeft(number: Int)
    abstract fun incFromRight(number: Int)
    abstract fun magnitude(): Int
}

private class SNFNum(var number: Int) : SNF() {

    override fun split(): SNFAction {
        if (number >= 10) {
            return SNFActionSplit(SNFPair(SNFNum(number / 2), SNFNum((number + 1) / 2)), number)
        }
        return SNFActionNone()
    }

    override fun explode(depth: Int) = SNFActionNone()
    override fun incFromLeft(number: Int) = run { this.number += number }
    override fun incFromRight(number: Int) = run { this.number += number }
    override fun magnitude() = number
    override fun toString() = "$number"
}

private class SNFPair(var left: SNF, var right: SNF) : SNF() {

    override fun split(): SNFAction {
        left.split().let {
            if (it is SNFActionSplit) {
                left = it.snf
                return SNFActionCompleted(it)
            }
            if (it.changed) {
                return it
            }
        }
        right.split().let {
            if (it is SNFActionSplit) {
                right = it.snf
                return SNFActionCompleted(it)
            }
            if (it.changed) {
                return it
            }
        }
        return SNFActionNone()
    }

    override fun explode(depth: Int): SNFAction {

        if (depth > 4) throw RuntimeException("wat $depth")
        if (depth == 4) return SNFActionExplode((left as SNFNum).number, (right as SNFNum).number)

        left.explode(depth + 1).let {
            when (it) {
                is SNFActionNone -> {}
                is SNFActionCompleted -> return it
                is SNFActionExplode -> {
                    left = SNFNum(0)
                    right.incFromLeft(it.right)
                    return SNFActionExplodeLeft(it.left, it)
                }
                is SNFActionExplodeLeft -> return it
                is SNFActionExplodeRight -> {
                    right.incFromLeft(it.right)
                    return SNFActionCompleted(it)
                }
                else -> throw RuntimeException("wat $it")
            }
        }

        right.explode(depth + 1).let {
            when (it) {
                is SNFActionNone -> {}
                is SNFActionCompleted -> return it
                is SNFActionExplode -> {
                    right = SNFNum(0)
                    left.incFromRight(it.left)
                    return SNFActionExplodeRight(it.right, it)
                }
                is SNFActionExplodeRight -> return it
                is SNFActionExplodeLeft -> {
                    left.incFromRight(it.left)
                    return SNFActionCompleted(it)
                }
                else -> throw RuntimeException("wat $it")
            }
        }
        return SNFActionNone()
    }

    override fun incFromLeft(number: Int) = run { left.incFromLeft(number) }
    override fun incFromRight(number: Int) = run { right.incFromRight(number) }
    override fun magnitude() = 3 * left.magnitude() + 2 * right.magnitude()
    override fun toString() = "[$left,$right]"
}

private sealed class SNFAction(val changed: Boolean)
private class SNFActionNone : SNFAction(false) {
    override fun toString() = "No action"
}

private data class SNFActionExplode(val left: Int, val right: Int) : SNFAction(true) {
    override fun toString() = "Explode  [$left,$right]"
}

private data class SNFActionExplodeLeft(val left: Int, val origin: SNFActionExplode) : SNFAction(true) {
    override fun toString() = "$origin"
}

private data class SNFActionExplodeRight(val right: Int, val origin: SNFActionExplode) : SNFAction(true) {
    override fun toString() = "$origin"
}

private data class SNFActionSplit(val snf: SNFPair, val number: Int) : SNFAction(true) {
    override fun toString() = "Split $number [${snf.left},${snf.right}]"
}

private data class SNFActionCompleted(val action: SNFAction) : SNFAction(true) {
    override fun toString() = "$action"
}

private fun parseSNF(input: String): SNF {
    if (input.isDigitsOnly()) return SNFNum(input.toInt())
    check(input.first(), '[')
    check(input.last(), ']')
    var depth = 0
    for (i in input.indices) {
        when (input[i]) {
            '[' -> depth++
            ']' -> depth--
            ',' -> if (depth == 1) return SNFPair(
                parseSNF(input.substring(1, i)),
                parseSNF(input.substring(i + 1, input.length - 1))
            )
        }
    }
    throw RuntimeException("wat")
}