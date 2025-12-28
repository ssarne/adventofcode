package aoc.aoc2016

import aoc.ktutils.*
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8

fun main() {

    execute1(testLines().first()).let { println("Test:   $it") ; check(it, "18f47a30") }
    execute1(readLines().first()).let { println("Result: $it") ; check(it, answerS(1)) }

    execute2(testLines().first()).let { println("Test:   $it") ; check(it, "05ace8e3") }
    execute2(readLines().first()).let { println("Result: $it") ; check(it, answerS(2)) }
}

private fun execute1(input: String): String {

    val md = MessageDigest.getInstance("MD5")
    var code = ""

    for (i in 0..1000000000) {
        val seed = input + i.toString()
        val hash =  md.digest(seed.toByteArray(UTF_8)).toHex()
        if (hash.startsWith("00000")) {
            code += hash[5]
        }
        if (code.length == 8) break
    }
    return code
}


private fun execute2(input: String): String {

    val md = MessageDigest.getInstance("MD5")
    val code = "________".toCharArray()

    for (i in 0..1000000000) {
        val seed = input + i.toString()
        val hash =  md.digest(seed.toByteArray(UTF_8)).toHex()
        if (hash.startsWith("00000")) {
            if (!hash[5].isDigit()) continue
            val pos = hash[5].toString().toInt()
            val num = hash[6]
            if (pos >= 8) continue
            if (code[pos] != '_') continue
            code[pos] = num
        }
        if (!code.contains('_')) break
    }
    return String(code)
}
