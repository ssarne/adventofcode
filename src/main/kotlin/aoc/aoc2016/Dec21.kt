package aoc.aoc2016

import aoc.ktutils.*
import kotlin.text.toInt

fun main() {
    execute1(testLines(), "abcde").let { println("Test:   $it"); check(it, "decab") }
    execute1(readLines(), "abcdefgh").let { println("Result: $it") ; check(it, answerS(1)) }

    // execute2(testLines()).let { println("Test:   $it") ; check(it, -1L) }
    execute2(readLines(), "fbgdceah").let { println("Result: $it") ; check(it, answerS(2)) }
}

val swapPositions = """swap position (\d+) with position (\d+)""".toRegex()
val swapLetter = """swap letter ([a-z]) with letter ([a-z])""".toRegex()
val reversePositions = """reverse positions (\d+) through (\d+)""".toRegex()
val rotateLeft = """rotate left (\d+) step(s)*""".toRegex()
val rotateRight = """rotate right (\d+) step(s)*""".toRegex()
val rotateBased = """rotate based on position of letter ([a-z])""".toRegex()
val movePosition = """move position (\d+) to position (\d+)""".toRegex()

private fun execute1(instructions: List<String>, input: String): String {

    var result = input

    for (instruction in instructions) {

        if (swapPositions.matches(instruction)) {
            val (a, b) = swapPositions.find(instruction)!!.destructured
            val arr = result.toCharArray()
            val tmp = arr[a.toInt()]
            arr[a.toInt()] = arr[b.toInt()]
            arr[b.toInt()] = tmp
            result = String(arr)
        } else if (swapLetter.matches(instruction)) {
            val (a, b) = swapLetter.find(instruction)!!.destructured
            result = result.replace(a, "_")
            result = result.replace(b, a)
            result = result.replace("_", b)
        } else if (reversePositions.matches(instruction)) {
            val (a, b) = reversePositions.find(instruction)!!.destructured
            val s1 = result.substring(0, a.toInt())
            val s2 = result.substring(a.toInt(), b.toInt() + 1).reversed()
            val s3 = result.substring(b.toInt() + 1)
            result = s1 + s2 + s3
        } else if (rotateLeft.matches(instruction)) {
            val (i) = rotateLeft.find(instruction)!!.destructured
            result = result.substring(i.toInt()) + result.substring(0, i.toInt())
        } else if (rotateRight.matches(instruction)) {
            val (i) = rotateRight.find(instruction)!!.destructured
            val p = result.length - i.toInt()
            result = result.substring(p) + result.substring(0, p)
        } else if (rotateBased.matches(instruction)) {
            val (c) = rotateBased.find(instruction)!!.destructured
            val i = result.indexOf(c)
            val s = if (i >= 4) i + 1 + 1 else i + 1
            val t = s % result.length
            val p = result.length - t
            result = result.substring(p) + result.substring(0, p)
        } else if (movePosition.matches(instruction)) {
            val (a, b) = movePosition.find(instruction)!!.destructured
            val c = result[a.toInt()]
            result = result.substring(0, a.toInt()) + result.substring(a.toInt() + 1)
            result = result.substring(0, b.toInt()) + c + result.substring(b.toInt())
        } else throw RuntimeException("CMH: $instruction")
    }
    return result
}

private fun execute2(instructions: List<String>, input: String): String {

    var result = input

    for (instruction in instructions.reversed()) {

        if (swapPositions.matches(instruction)) {
            val (a, b) = swapPositions.find(instruction)!!.destructured
            val arr = result.toCharArray()
            val tmp = arr[a.toInt()]
            arr[a.toInt()] = arr[b.toInt()]
            arr[b.toInt()] = tmp
            result = String(arr)
        } else if (swapLetter.matches(instruction)) {
            val (a, b) = swapLetter.find(instruction)!!.destructured
            result = result.replace(a, "_")
            result = result.replace(b, a)
            result = result.replace("_", b)
        } else if (reversePositions.matches(instruction)) {
            val (a, b) = reversePositions.find(instruction)!!.destructured
            val s1 = result.substring(0, a.toInt())
            val s2 = result.substring(a.toInt(), b.toInt() + 1).reversed()
            val s3 = result.substring(b.toInt() + 1)
            result = s1 + s2 + s3
        } else if (rotateLeft.matches(instruction)) {
            val (i) = rotateLeft.find(instruction)!!.destructured
            val p = result.length - i.toInt()
            result = result.substring(p) + result.substring(0, p)
        } else if (rotateRight.matches(instruction)) {
            val (i) = rotateRight.find(instruction)!!.destructured
            result = result.substring(i.toInt()) + result.substring(0, i.toInt())
        } else if (rotateBased.matches(instruction)) {
            val (c) = rotateBased.find(instruction)!!.destructured
            val i = result.indexOf(c)
            val m = mapOf( // reverse engineer for length 8
                1 to 0, // 0 -> 0+0+1 = 1
                3 to 1, // 1 -> 1+1+1 = 3
                5 to 2, // 2 -> 2+2+1 = 5
                7 to 3, // 3 -> 3+3+1 = 7
                2 to 4, // 4 -> 4+4+2 = 10 % 8 = 2
                4 to 5, // 5 -> 5+5+2 = 12 % 8 = 4
                6 to 6, // 6 -> 6+6+2 = 14 % 8 = 6
                0 to 7) // 7 -> 7+7+2 = 16 % 8 = 0
            val s = m[i]!! - i
            if (s < 0)
                result = result.substring(Math.abs(s)) + result.substring(0, Math.abs(s))
            else
                result = result.substring(result.length - s) + result.substring(0, result.length - s)
        } else if (movePosition.matches(instruction)) {
            val (a, b) = movePosition.find(instruction)!!.destructured
            val c = result[b.toInt()]
            result = result.substring(0, b.toInt()) + result.substring(b.toInt() + 1)
            result = result.substring(0, a.toInt()) + c + result.substring(a.toInt())
        } else throw RuntimeException("CMH: $instruction")
    }
    return result
}