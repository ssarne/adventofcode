package aoc.aoc2016

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 1514)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsInt(1)) }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsInt(2)) }
}

private data class Letter(val symbol: Char, var count: Int)

private fun execute1(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        val (name, id, checksum) = parse(line)
        val pass = validate(name, checksum)
        if (pass) sum += id.toInt()
    }
    return sum
}

private fun execute2(input: List<String>): Int {
    var result = 0
    for (line in input) {
        val (name, id, checksum) = parse(line)
        if (!validate(name, checksum)) continue

        var decrypted = ""
        for (c in name) {
            if (c == '-') decrypted += " "
            var a: Int = c.code - 'a'.code
            a = (a + id.toInt()) % 26
            a += 'a'.code
            decrypted += a.toChar()
        }
        if (decrypted.contains("northpole")) {
            // println(decrypted)
            result = id.toInt()
        }
    }
    return result
}

private fun validate(name: String, checksum: String): Boolean {

    val map = HashMap<Char, Letter>()
    for (c in name) {
        if (c == '-') continue
        val letter = map[c] ?: Letter(c, 0)
        letter.count++
        map[c] = letter
    }

    val letters = map.values.toList()
        .sortedWith(compareBy({ -it.count }, { it.symbol }))

    var pass = true
    for (i in checksum.indices)
        if (checksum[i] != letters[i].symbol)
            pass = false

    return pass
}

private fun parse(line: String): Triple<String, String, String> {
    val room = line.split("[")[0]
    val name = room.substring(0, line.lastIndexOf("-"))
    val id = room.substring(line.lastIndexOf("-") + 1)
    val checksum = line.split("[")[1].replace("]", "")
    return Triple(name, id, checksum)
}
