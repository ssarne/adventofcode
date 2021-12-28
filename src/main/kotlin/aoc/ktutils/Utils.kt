package aoc.ktutils

import aoc.utils.InputDownloader
import java.io.File
import java.lang.RuntimeException

fun readLines(): List<String> {
    val (year, day) = getYearAndDay()
    if (!InputDownloader.hasInputFile(year, day))
        InputDownloader.getInputFile(year, day)

    val fileName = getInputFilePath(year, day)
    return File(fileName).readLines(Charsets.UTF_8)
}

fun readTestLines(testIndex: Int = 0): List<String> {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day, true, testIndex)
    return File(fileName).readLines(Charsets.UTF_8)
}

fun readText(): String {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day)
    if (!InputDownloader.hasInputFile(year, day))
        InputDownloader.getInputFile(year, day)
    var text = File(fileName).readText(Charsets.UTF_8)
    if (text.endsWith("\n") || text.endsWith("\r")) text = text.substring(0, text.length - 1)
    return text
}

fun readTestText(testIndex: Int = 0): String {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day, true, testIndex)
    return File(fileName).readText(Charsets.UTF_8)
}

fun readInts(): IntArray {
    return asIntArray(readText())
}

fun readTestInts(): IntArray {
    return asIntArray(readTestText())
}

fun asInt(c: Char) = Character.getNumericValue(c)
fun asLong(c: Char) = Character.getNumericValue(c).toLong()

fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()
fun String.isSingleChar() = this.length == 1 && all(Char::isLetter)

fun asIntArray(text: String): IntArray {
    return text
        .replace("\n", " ")
        .split(",", " ", "\t")
        .filter{ t -> t != "" }
        .map { t -> t.toInt() }
        .toIntArray()
}

fun isLowerCase(text: String): Boolean {
    for (c in text) {
        if (c.toInt() < 'a'.toInt() || c.toInt() > 'z'.toInt()) {
            return false
        }
    }
    return true
}

fun hexToBinary0Padded(input: String): String {
    var res = ""
    for (c in input) {
        res += when (c) {
            '0' -> "0000"
            '1' -> "0001"
            '2' -> "0010"
            '3' -> "0011"
            '4' -> "0100"
            '5' -> "0101"
            '6' -> "0110"
            '7' -> "0111"
            '8' -> "1000"
            '9' -> "1001"
            'A' -> "1010"
            'B' -> "1011"
            'C' -> "1100"
            'D' -> "1101"
            'E' -> "1110"
            'F' -> "1111"
            else -> throw RuntimeException("wat: $c")
        }
    }
    return res
}


fun getMinMaxOccurence(text: String): Pair<Int, Int> {
    val counts = HashMap<Char, Int>()
    var min = Int.MAX_VALUE
    var max = 0
    for (c1 in text) {
        if (counts.containsKey(c1)) continue
        var count = 0
        for (c2 in text) {
            if (c1 == c2) count++
        }
        counts[c1] = count
        if (min > count) min = count
        if (max < count) max = count
    }
    return Pair(min, max)
}

private fun getInputFilePath(year: String, day: String, test: Boolean = false, testIndex: Int = 0): String {
    var suffix = (if (test) "_test" else "")
    suffix += if (testIndex > 0) "$testIndex" else ""
    return "src/main/resources/aoc$year/$day$suffix.txt"
}

private data class YearAndDay(var year: String, var day: String)

private fun getYearAndDay(): YearAndDay {
    val name = getCallerClass()
    val year = name.split(".").toTypedArray()[1].replace("aoc", "")
    val day = name.split(".").toTypedArray()[2].replace("Kt", "").toLowerCase()
    return YearAndDay(year, day)
}

private fun getCallerClass(): String {
    val stElements = Thread.currentThread().stackTrace
    for (i in 1 until stElements.size) {
        val ste = stElements[i]
        if (ste.className != "aoc.ktutils.UtilsKt"
            && ste.className != "aoc.utils.Utils"
            && ste.className.indexOf("java.lang.Thread") != 0
        ) {
            return ste.className
        }
    }
    return ""
}