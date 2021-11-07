package aoc.ktutils

import java.io.File

fun readText(fileName: String): String
        = File("src/main/resources/" + fileName).readText(Charsets.UTF_8)

fun check(actual: Boolean, expected: Boolean) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}

fun check(actual: Int, expected: Int) {
    if (actual != expected) {
        System.err.println("Failure: actual=$actual  expected=$expected")
    }
}