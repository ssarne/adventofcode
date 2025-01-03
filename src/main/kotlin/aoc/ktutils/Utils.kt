package aoc.ktutils

import aoc.utils.InputDownloader
import java.io.File
import java.math.BigDecimal
import java.math.BigInteger
import java.math.MathContext
import java.util.*

fun readLines(): List<String> {
    val (year, day) = getYearAndDay()
    return readLines(year, day)
}

fun readLines(year: String, day: String): List<String> {
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

fun hasTestFile(testIndex: Int = 0): Boolean {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day, true, testIndex)
    return File(fileName).exists()
}

fun asChunks(lines: List<String>): List<List<String>> {
    val chunks = ArrayList<List<String>>()
    var chunk = ArrayList<String>()
    chunks.add(chunk)
    for (line in lines) {
        if (line == "") {
            chunk = ArrayList<String>()
            chunks.add(chunk)
            continue
        }
        chunk.add(line)
    }
    return chunks
}

fun readText(): String {
    val (year, day) = getYearAndDay()
    return readText(year, day)
}

 fun readText(year: String, day: String): String {

    val fileName = getInputFilePath(year, day)
    if (!InputDownloader.hasInputFile(year, day))
        InputDownloader.getInputFile(year, day)
    var text = File(fileName).readText(Charsets.UTF_8)
    if (text.endsWith("\n") || text.endsWith("\r")) text = text.substring(0, text.length - 1)
    return text
}

fun readAnswer(part: Int = 0): String {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day)
        .replace(".txt", ".out")
    if (!File(fileName).exists())
        return "No answers file at $fileName"
    var text = File(fileName).readText(Charsets.UTF_8)
    if (text.endsWith("\n") || text.endsWith("\r"))
        text = text.substring(0, text.length - 1)
    if (part > 0)
        text = text.split("\n")[part - 1]
    return text
}

fun readAnswerAsInt(part: Int = 0) = readAnswer(part).toInt()
fun readAnswerAsLong(part: Int = 0) = readAnswer(part).toLong()

fun readTestText(testIndex: Int = 0): String {
    val (year, day) = getYearAndDay()
    val fileName = getInputFilePath(year, day, true, testIndex)
    return File(fileName).readText(Charsets.UTF_8)
}

fun readInts() = asIntArray(readText())
fun readTestInts() = asIntArray(readTestText())

fun asInt(c: Char) = Character.getNumericValue(c)
fun asLong(c: Char) = Character.getNumericValue(c).toLong()

fun String.isDigitsOnly() = all(Char::isDigit) && isNotEmpty()
fun String.isSingleChar() = this.length == 1 && all(Char::isLetter)

fun asIntArray(text: String): IntArray {
    return text
        .replace("\n", " ")
        .split(",", " ", "\t")
        .filter { t -> t != "" }
        .map { t -> t.toInt() }
        .toIntArray()
}


fun asLongArray(text: String): LongArray {
    return text
        .replace("\n", " ")
        .split(",", " ", "\t")
        .filter { t -> t != "" }
        .map { t -> t.toLong() }
        .toLongArray()
}

fun all(values: IntArray, value: Int) = (values.size == values.count() { it == value })
fun all(values: LongArray, value: Long) = (values.size == values.count() { it == value })

fun count(text: String, c: Char, range: IntRange): Int {
    var count = 0
    for (i in range)
        if (text[i] == c)
            count++
    return count
}

fun count(text: String, matches: String, range: IntRange): Int {
    var count = 0
    for (i in range)
        if (matches.contains(text[i]))
            count++
    return count
}


fun isLowerCase(text: String): Boolean {
    for (c in text) {
        if (c.toInt() < 'a'.toInt() || c.toInt() > 'z'.toInt()) {
            return false
        }
    }
    return true
}

// Use for counting occurrences, add increment number of key
fun MutableMap<Long, Long>.addOrPut(key: Long, inc: Long) {
    this[key] = this.getOrDefault(key, 0L) + inc
}

fun MutableMap<Char, Long>.addOrPut(key: Char, inc: Long) {
    this[key] = this.getOrDefault(key, 0L) + inc
}

fun ByteArray.toHex() = joinToString(separator = "") { byte -> "%02x".format(byte) }

fun Long.toBigD() = this.toBigDecimal().setScale(64)
fun BigDecimal.toBigI(): BigInteger = this.round(MathContext.DECIMAL128).toBigInteger()

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

fun dotty(edges: HashSet<Pair<String, String>>) {
    val yd = getYearAndDay()
    val file = File("target/aoc${yd.year}_${yd.day}.dot")
    println("Printing graph to file: " + file.absolutePath)
    file.printWriter().use {
        it.println("graph {")
        for (edge in edges)
            it.println("  ${edge.first} -- ${edge.second}[label=\"${edge.first}--${edge.second}\"];")
        it.println("}")
    }
}

private fun getInputFilePath(year: String, day: String, test: Boolean = false, testIndex: Int = 0): String {
    val base = if (test) "src/main/resources" else "input"
    var suffix = if (test) "_test" else ""
    if (testIndex > 0) suffix += "$testIndex"
    return "$base/aoc$year/$day$suffix.txt"
}

private data class YearAndDay(var year: String, var day: String)

private fun getYearAndDay(): YearAndDay {
    val name = getCallerClass()
    val year = name.split(".").toTypedArray()[1].replace("aoc", "")
    val day = name.split(".").toTypedArray()[2].replace("Kt", "").lowercase(Locale.getDefault())
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