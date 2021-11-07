package aoc.aoc2017

import aoc.ktutils.check
import aoc.utils.Utils

fun main() {
    check(countValidPassPhrases("aoc2017/dec04_test.txt"), 2)
    println(countValidPassPhrases(null))
    check(countValidPassPhrasesWithoutAnagrams("aoc2017/dec04_test2.txt"), 3)
    println(countValidPassPhrasesWithoutAnagrams(null))
}

fun countValidPassPhrases(fileName: String?): Int {
    var sum = 0
    for (line in Utils.getLines(fileName)) {
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

fun countValidPassPhrasesWithoutAnagrams(fileName: String?): Int {
    var sum = 0
    for (line in Utils.getLines(fileName)) {
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
