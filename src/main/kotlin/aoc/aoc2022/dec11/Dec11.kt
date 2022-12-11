package aoc.aoc2022.dec11

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 10605L)
    execute1(readLines()).let { println(it); check(it, 90294L) }

    check(execute2(readTestLines(), 20), 99 * 103L)
    check(execute2(readTestLines(), 10000), 2713310158L)
    execute2(readLines(), 10000).let { println(it); check(it, 18170818354L) }
}

data class Monkey(
    val name: Int,
    val items: ArrayList<Long>,
    val operation: List<String>,
    val test: Long,
    val pass: Int,
    val fail: Int,
) {
    var inspections: Int = 0
}

private fun execute1(input: List<String>): Long {
    val monkeys = parse(input)
    execute(monkeys, 20, 0L)
    val sorted = monkeys.sortedBy { it.inspections }.reversed()
    return sorted[0].inspections.toLong() * sorted[1].inspections.toLong()
}

private fun execute2(input: List<String>, rounds: Int): Long {
    val monkeys = parse(input)
    var divisor: Long = 1
    for (monkey in monkeys) divisor *= monkey.test
    execute(monkeys, rounds, divisor)
    val sorted = monkeys.sortedBy { it.inspections }.reversed()
    return sorted[0].inspections.toLong() * sorted[1].inspections.toLong()
}

private fun execute(monkeys: ArrayList<Monkey>, rounds: Int, gcd: Long) {

    for (round in 1..rounds) {
        for (monkey in monkeys) {
            for (item in monkey.items) {
                var v1 = if (monkey.operation[0] == "old") item else monkey.operation[0].toLong()
                var v2 = if (monkey.operation[2] == "old") item else monkey.operation[2].toLong()
                var new: Long = when (monkey.operation[1]) {
                    "+" -> v1 + v2
                    "-" -> v1 - v2
                    "*" -> v1 * v2
                    else -> -1
                }

                new = if (gcd == 0L) new / 3 // part 1
                else new % gcd // part 2

                if (new % monkey.test == 0L) {
                    monkeys[monkey.pass].items.add(new)
                } else {
                    monkeys[monkey.fail].items.add(new)
                }
                monkey.inspections++
            }
            monkey.items.clear()
        }
    }
}

private fun parse(input: List<String>): ArrayList<Monkey> {

    val monkeys = ArrayList<Monkey>()

    for (chunk in asChunks(input)) {
        val name = chunk[0].replace("Monkey ", "").replace(":", "").toInt()
        val items = ArrayList<Long>()
        val ii = chunk[1].replace("  Starting items: ", "").split(", ").map { it.toLong() }.toLongArray()
        for (i in ii) items.add(i)
        val operation = chunk[2].replace("  Operation: new = ", "").split(" ").toList()
        val test = chunk[3].replace("  Test: ", "").split(" ")[2].toLong()
        val tt = chunk[4].replace("    If true: throw to monkey", "").trim().toInt()
        val tf = chunk[5].replace("    If false: throw to monkey", "").trim().toInt()
        monkeys.add(Monkey(name, items, operation, test, tt, tf))
    }
    return monkeys
}