package aoc.aoc2021


import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 1588)
    execute1(readLines()).let { println(it); check(it, readAnswerAsInt(1)) }

    check(execute2(readTestLines(), 10), 1588L)
    execute2(readLines(), 40).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Int {

    var polymer = input.first()
    val templates = parseInput(input).first

    for (j in 0..9) {
        polymer = polymerize(polymer, templates)
    }

    val (min, max) = getMinMaxOccurence(polymer)
    return max - min
}

private fun polymerize(polymer: String, templates: HashMap<String, String>): String {
    var next = ""
    for (i in polymer.indices) {
        val pair = polymer.substring(i, min(i + 2, polymer.length))
        next += if (templates.containsKey(pair)) pair[0] + templates[pair]!! else pair[0]
    }
    return next
}

private fun parseInput(input: List<String>): Pair<HashMap<String, String>, Set<Char>> {
    val templates = HashMap<String, String>()
    val substances = HashSet<Char>()
    for (line in input) {
        if (line.contains("->")) {
            line.split(" -> ").let {
                templates[it[0]] = it[1]
                for (c in it[0]) substances.add(c)
                for (c in it[1]) substances.add(c)
            }
        } else {
            for (c in line) substances.add(c)
        }
    }
    return Pair(templates, substances)
}

private fun execute2(input: List<String>, loops: Int): Long {

    val (templates, substances) = parseInput(input)
    val polymer = input.first()
    var pairs = createPairs(polymer)

    for (i in 1..loops) {
        pairs = polymerizePairs(pairs, templates)
    }

    val (min, max) = getMinMaxSubstances(pairs, substances, polymer.last())
    return max - min
}

private fun polymerizePairs(pairs: HashMap<String, Long>, templates: HashMap<String, String>): HashMap<String, Long> {
    val next = HashMap<String, Long>()
    for (pair in pairs.keys) {
        if (!templates.containsKey(pair)) throw RuntimeException("wat")
        val p1 = "" + pair[0] + templates[pair]
        val p2 = "" + templates[pair] + pair[1]
        val cnt1 = next.getOrDefault(p1, 0) + pairs[pair]!!
        val cnt2 = next.getOrDefault(p2, 0) + pairs[pair]!!
        next[p1] = cnt1
        next[p2] = cnt2
    }
    return next
}

private fun createPairs(polymer: String): HashMap<String, Long> {
    val pairs = HashMap<String, Long>()
    for (i in 1 until polymer.length) {
        val key = "" + polymer[i - 1] + polymer[i]
        pairs[key] = pairs.getOrDefault(key, 0) + 1
    }
    return pairs
}

fun getMinMaxSubstances(pairs: HashMap<String, Long>, substances: Set<Char>, last: Char): Pair<Long, Long> {
    val counts = HashMap<Char, Long>()
    for (c in substances) {
        var count = 0L
        for (pair in pairs.keys) {
            if (pair[0] == c) {
                count += pairs[pair]!!
            }
        }
        counts[c] = if (c == last) count + 1 else count
    }

    var min = Long.MAX_VALUE
    var max = 0L
    for (i in counts.values) {
        if (min > i) min = i
        if (max < i) max = i
    }
    return Pair(min, max)
}
