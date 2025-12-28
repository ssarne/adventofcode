package aoc.aoc2016

import aoc.ktutils.*
import java.security.MessageDigest

fun main() {
    execute1(testLines()).let { println("Test:   $it"); check(it, 22728L) }
    execute1(readLines()).let { println("Result: $it") ; check(it, answer(1)) }

    execute2(testLines()).let { println("Test:   $it") ; check(it, 22551L) }
    execute2(readLines()).let { println("Result: $it") ; check(it, answer(2)) }
}

private fun execute1(input: List<String>): Long {
    val salt = input.first()
    return execute(salt,  { str ->
        val bytes = MessageDigest.getInstance("MD5").digest(str.toByteArray())
        bytes.joinToString("") { "%02x".format(it) }
    })
}

private fun execute2(input: List<String>): Long {
    val salt = input.first()
    return execute(salt, { str ->
        val hasher = MessageDigest.getInstance("MD5")
        var hash = str
        for (i in 0..2016) {
            val bytes = hasher.digest(hash.toByteArray())
            hash = bytes.joinToString("") { "%02x".format(it) }
        }
        hash
    })
}

private fun execute(salt: String, hashFn: (String) -> String): Long {

    val keys = ArrayList<Int>()
    val triples = HashMap<Int, Char>() // only take the first triple in the hash
    val fives = HashMap<Int, HashSet<Char>>()
    var populated = 0

    for (i in generateSequence(0) { it + 1 }.takeWhile { keys.size < 64 }) {
        if (i + 1000 > populated)
            populated = prepare(salt, populated, populated + 10000, triples, fives, hashFn)

        if (triples.contains(i)) {
            for (j in i + 1..i + 1000) {
                if (fives.contains(j)) {
                    val c = triples[i]!!
                    if (fives[j]!!.contains(c)) {
                        if (keys.isEmpty() || keys.last() != i) {
                            keys.add(i)
                        }
                    }
                }
            }
        }
    }
    return keys.last().toLong()
}

private fun prepare(
    salt: String,
    from: Int,
    to: Int,
    triples: HashMap<Int, Char>,
    quintets: HashMap<Int, HashSet<Char>>,
    hashFn: (String) -> String
): Int {
    for (i in from..to) {
        val hash = hashFn(salt + i)
        val ts = triples(hash)
        if (ts.isNotEmpty())
            triples.put(i, ts.first())
        val qs = quintets(hash)
        if (qs.isNotEmpty())
            quintets.put(i, qs)
    }
    return to
}

private fun triples(str: String): HashSet<Char> {
    val result = HashSet<Char>()
    for (i in 0..str.length - 3) {
        if (str[i] == str[i + 1] && str[i] == str[i + 2]) {
            result.add(str[i])
            break // only first triple is used
        }
    }
    return result
}

private fun quintets(str: String): HashSet<Char> {
    val result = HashSet<Char>()
    for (i in 0..str.length - 5)
        if (str[i] == str[i + 1] && str[i] == str[i + 2] && str[i] == str[i + 3] && str[i] == str[i + 4])
            result.add(str[i])
    return result
}

