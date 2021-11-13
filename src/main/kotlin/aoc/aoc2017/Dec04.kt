package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import aoc.utils.Utils

fun main() {
    check(countValidPassPhrases(readTestLines()), 2)
    println(countValidPassPhrases(readLines()))
    check(countValidPassPhrasesWithoutAnagrams(readTestLines(2)), 3)
    println(countValidPassPhrasesWithoutAnagrams(readLines()))
}

private fun countValidPassPhrases(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        var words = HashSet<String>()
        var pass = true
        for (word in line.split(" ", "\t")) {
            if (words.contains(word)) pass = false
            words.add(word)
        }
        if (pass) sum++
    }
    return sum
}

private fun countValidPassPhrasesWithoutAnagrams(input: List<String>): Int {
    var sum = 0
    for (line in input) {
        var words = HashSet<String>()
        var pass = true
        for (word in line.split(" ", "\t")) {
            val sorted = word.toCharArray().sorted().joinToString("")
            if (words.contains(sorted)) pass = false
            words.add(sorted)
        }
        if (pass) sum++
    }
    return sum
}
