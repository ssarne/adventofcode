package aoc.aoc2022.dec13

import aoc.ktutils.*
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 13)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines()), 140)
    execute2(readLines()).let { println(it); check(it, readAnswerAsInt(2)) }
}

private fun execute1(input: List<String>): Int {
    var sum = 0
    for ((i, chunk) in asChunks(input).withIndex())
        if (compare(chunk[0], chunk[1]) <= 0)
            sum += i + 1

    return sum
}

private fun execute2(input: List<String>): Int {

    var input2 = input.filter { it != "" }.toMutableList()
    input2.add("[[2]]")
    input2.add("[[6]]")

    var sorted = input2.sortedWith { a, b -> compare(a, b) }
    var key1 = sorted.indexOf("[[2]]") + 1
    var key2 = sorted.indexOf("[[6]]") + 1
    return key1 * key2
}

// sub-zero, left is smaller, 0 equals, above-zero right is smaller
fun compare(left: String, right: String): Int {

    if (left.isEmpty() && right.isEmpty()) return 0
    if (left.isEmpty()) return -1
    if (right.isEmpty()) return 1
    if (left == "[]" && right == "[]") return 0
    if (left == "[]") return -1
    if (right == "[]") return 1

    if (left[0] == ',' && right[0] == ',')
        return compare(left.substring(1), right.substring(1))

    if (left[0].isDigit() && right[0].isDigit()) {
        val firstLeft =
            if (left.contains(",")) left.substring(0, left.indexOf(",")).toInt()
            else left.toInt()
        val firstRight =
            if (right.contains(",")) right.substring(0, right.indexOf(",")).toInt()
            else right.toInt()

        if (firstLeft != firstRight)
            return (firstLeft - firstRight)

        val restLeft =
            if (left.contains(",")) left.substring(left.indexOf(",") + 1)
            else ""
        val restRight =
            if (right.contains(",")) right.substring(right.indexOf(",") + 1)
            else ""
        return compare(restLeft, restRight)

    } else if (left[0] == '[' && right[0].isDigit()) {
        val i = right.indexOf(',')
        val patched =
            if (i == -1) "[$right]"
            else "[" + right.substring(0, i) + "]" + right.substring(i)
        return compare(left, patched)

    } else if (left[0].isDigit() && right[0] == '[') {
        val i = left.indexOf(',')
        val patched =
            if (i == -1) "[$left]"
            else "[" + left.substring(0, i) + "]" + left.substring(i)
        return compare(patched, right)

    } else if (left[0] == '[' && right[0] == '[') {
        val (_, li) = matchFirstParenthesis(left)
        val (_, ri) = matchFirstParenthesis(right)

        val firstLeft = left.substring(1, li)
        val restLeft = left.substring(li + 1)
        val firstRight = right.substring(1, ri)
        val restRight = right.substring(ri + 1)
        val first = compare(firstLeft, firstRight)
        if (first != 0) return first
        return compare(restLeft, restRight)

    } else throw RuntimeException("CMH left=$left   right=$right")
}
