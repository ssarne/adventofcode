package aoc.aoc2016

import aoc.ktutils.*
import java.security.MessageDigest
import kotlin.text.Charsets.UTF_8
import kotlin.text.toByteArray

fun main() {
    execute1(testLines(1)).let { println("Test:   $it") ; check(it, "DDRRRD") }
    execute1(testLines(2)).let { println("Test:   $it") ; check(it, "DDUDRLRRUDRD") }
    execute1(testLines(3)).let { println("Test:   $it") ; check(it, "DRURDRUDDLLDLUURRDULRLDUUDDDRR") }
    execute1(readLines()).let { println("Result: $it") ; check(it, answerS(1)) }

    execute2(testLines(1)).let { println("Test:   $it") ; check(it, 370L) }
    execute2(testLines(2)).let { println("Test:   $it") ; check(it, 492L) }
    execute2(testLines(3)).let { println("Test:   $it") ; check(it, 830L) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): String {
    val map = parseCharacterGridToMap(testLines(0))
    val salt = input.first()
    val md = MessageDigest.getInstance("MD5")
    val start = findFirst(map, 'S')
    val target = findFirst(map, 'V')
    val queue = mutableListOf(start to "")

    while (queue.isNotEmpty()) {
        val pos = queue.removeAt(0)
        if (pos.first == target) return pos.second
        val hash =  md.digest((salt + pos.second).toByteArray(UTF_8)).toHex()
        if (map[Point(pos.first.x, pos.first.y - 1)] == '-' && "bcdef".contains(hash[0]))
            queue.add(Point(pos.first.x, pos.first.y - 2) to pos.second + "U")
        if (map[Point(pos.first.x, pos.first.y + 1)] == '-' && "bcdef".contains(hash[1]))
            queue.add(Point(pos.first.x, pos.first.y + 2) to pos.second + "D")
        if (map[Point(pos.first.x - 1, pos.first.y)] == '|' && "bcdef".contains(hash[2]))
            queue.add(Point(pos.first.x - 2, pos.first.y) to pos.second + "L")
        if (map[Point(pos.first.x + 1, pos.first.y)] == '|' && "bcdef".contains(hash[3]))
            queue.add(Point(pos.first.x + 2, pos.first.y) to pos.second + "R")
    }
    return "#fail"
}

private fun execute2(input: List<String>): Long {
    val map = parseCharacterGridToMap(testLines(0))
    val salt = input.first()
    val md = MessageDigest.getInstance("MD5")
    val start = findFirst(map, 'S')
    val target = findFirst(map, 'V')
    val queue = mutableListOf(start to "")

    var max = 0L

    while (queue.isNotEmpty()) {
        val pos = queue.removeAt(0)
        if (pos.first == target) {
            max = pos.second.length.toLong()
            continue
        }
        val hash =  md.digest((salt + pos.second).toByteArray(UTF_8)).toHex()
        if (map[Point(pos.first.x, pos.first.y - 1)] == '-' && "bcdef".contains(hash[0]))
            queue.add(Point(pos.first.x, pos.first.y - 2) to pos.second + "U")
        if (map[Point(pos.first.x, pos.first.y + 1)] == '-' && "bcdef".contains(hash[1]))
            queue.add(Point(pos.first.x, pos.first.y + 2) to pos.second + "D")
        if (map[Point(pos.first.x - 1, pos.first.y)] == '|' && "bcdef".contains(hash[2]))
            queue.add(Point(pos.first.x - 2, pos.first.y) to pos.second + "L")
        if (map[Point(pos.first.x + 1, pos.first.y)] == '|' && "bcdef".contains(hash[3]))
            queue.add(Point(pos.first.x + 2, pos.first.y) to pos.second + "R")
    }
    return max
}
