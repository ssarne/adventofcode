package aoc.aoc2023

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 21)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 525152)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val springs = line.split(" ")[0]
        val numbers = asIntArray(line.split(" ")[1])
        val layouts = permutate(springs, numbers, 0, 0, HashMap())
        sum += layouts
    }
    return sum
}

private fun execute2(input: List<String>): Long {
    var sum = 0L
    for (line in input) {
        val first = line.split(" ")[0]
        val second = line.split(" ")[1]
        val springs = "$first?$first?$first?$first?$first"
        val numbers = asIntArray("$second,$second,$second,$second,$second")
        val layouts = permutate(springs, numbers, 0, 0, HashMap())
        sum += layouts
    }
    return sum
}


fun permutate(springs: String, numbers: IntArray, si: Int, ni: Int, cache: HashMap<Point, Long>): Long {

    val key = Point(si, ni)
    if (cache.contains(key)) return cache[key]!!

    if (ni == numbers.size) {
        val count = count(springs, '#', si until springs.length)
        return if (count == 0) 1 else 0
    }

    if (si >= springs.length) return 0

    if (springs[si] == '.') {
        val result = permutate(springs, numbers, si + 1, ni, cache)
        cache[key] = result
        return result
    }

    val match = match(springs, numbers, si, ni)
    if (springs[si] == '#') {
        val result = if (match) permutate(springs, numbers, si + numbers[ni] + 1, ni + 1, cache) else 0
        cache[key] = result
        return result
    }

    if (springs[si] == '?') {
        val use = if (match) permutate(springs, numbers, si + numbers[ni] + 1, ni + 1, cache) else 0
        val skip = permutate(springs, numbers, si + 1, ni, cache)
        val result = use + skip
        cache[key] = result
        return result
    }

    throw RuntimeException("CMH")

}

private fun match(springs: String, numbers: IntArray, si: Int, ni: Int): Boolean {
    if (si + numbers[ni] > springs.length)
        return false

    if (count(springs, "#?", si until si + numbers[ni]) < numbers[ni])
        return false

    if (si + numbers[ni] < springs.length && springs[si + numbers[ni]] == '#')
        return false

    return true
}