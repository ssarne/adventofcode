package aoc.aoc2023

import aoc.ktutils.*
import java.util.*


fun main() {

    test()

    check(execute1(readTestLines(1)), 6440)
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 5905)
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

enum class types { five_of_a_kind, four_of_a_kind, full_house, three_of_a_kind, two_pair, one_pair, high_card }

private data class Card(val card: Char, val frequency: Int)

private data class Camel(val cards: String, val bid: Int, val useJoker: Boolean) : Comparable<Camel> {

    val type = identifyCamelType(cards, useJoker)

    override fun compareTo(that: Camel): Int {
        if (this.type.compareTo(that.type) != 0)
            return this.type.compareTo(that.type)
        return strengthComparison(this.cards, that.cards)
    }

    private fun strengthComparison(thisCards: String, thatCards: String): Int {
        val strengths = if (useJoker)  "AKQT98765432J" else "AKQJT98765432"

        for (i in this.cards.indices)
            if (thisCards[i] != thatCards[i])
                return strengths.indexOf(thisCards[i]) - strengths.indexOf(thatCards[i])
        throw RuntimeException("CMH")
    }
}

private fun test() {
    check(identifyCamelType("AAAAA").name, types.five_of_a_kind.name)
    check(identifyCamelType("AA2AA").name, types.four_of_a_kind.name)
    check(identifyCamelType("K4K4K").name, types.full_house.name)
    check(identifyCamelType("K4K2K").name, types.three_of_a_kind.name)
    check(identifyCamelType("K4K42").name, types.two_pair.name)
    check(identifyCamelType("T2344").name, types.one_pair.name)
    check(identifyCamelType("T2345").name, types.high_card.name)
    check(Camel("AAAAA", 1, false).compareTo(Camel("AAAAK", 1, false)), -1)
    check(Camel("AA2AA", 1, false).compareTo(Camel("K4K4K", 1, false)), -1)
    check(Camel("K4K4K", 1, false).compareTo(Camel("K4K2K", 1, false)), -1)
    check(Camel("K4K2K", 1, false).compareTo(Camel("K4K42", 1, false)), -1)
    check(Camel("K4K42", 1, false).compareTo(Camel("T2344", 1, false)), -1)
    check(Camel("T2344", 1, false).compareTo(Camel("T2345", 1, false)), -1)
}

private fun execute1(input: List<String>): Long {

    val hands = ArrayList<Camel>()
    for (line in input) {
        val parts = line.split(" ")
        hands.add(Camel(parts[0], parts[1].toInt(), false))
    }

    hands.sort()
    hands.reverse()

    return hands
        .withIndex()
        .sumOf { 1L * (it.index + 1) * it.value.bid }
}

private fun execute2(input: List<String>): Long {
    val hands = ArrayList<Camel>()
    for (line in input) {
        val parts = line.split(" ")
        hands.add(Camel(parts[0], parts[1].toInt(), true))
    }
    hands.sort()
    hands.reverse()

    return hands
        .withIndex()
        .sumOf { 1L * (it.index + 1) * it.value.bid }
}

private fun identifyCamelType(cards: String, useJoker: Boolean): types {
    var best = identifyCamelType(cards)
    if (useJoker) {
        for (c in cards) { // same always better than not same (no straights)
            val adjusted = cards.replace("J", "" + c)
            val type = identifyCamelType(adjusted)
            if (type < best) best = type
        }
    }
    return best
}

private fun identifyCamelType(cards: String): types {
    val (card1, card2) = analyze(cards)
    if (card1.frequency == 5) return types.five_of_a_kind
    if (card1.frequency == 4) return types.four_of_a_kind
    if (card1.frequency == 3 && card2.frequency == 2) return types.full_house
    if (card1.frequency == 3) return types.three_of_a_kind
    if (card1.frequency == 2 && card2.frequency == 2) return types.two_pair
    if (card1.frequency == 2) return types.one_pair
    return types.high_card
}

private fun analyze(cards: String): Pair<Card, Card> {
    var count1 = 0
    var card1 = 'B'
    var count2 = 0
    var card2 = 'B'
    for (c in cards) {
        val n = cards.count { it == c }
        if (c == card1) continue
        if (c == card2) continue
        if (n > count1) {
            count2 = count1
            card2 = card1
            count1 = n
            card1 = c
        } else if (n > count2) {
            count2 = n
            card2 = c
        }
    }
    return Card(card1, count1) to Card(card2, count2)
}