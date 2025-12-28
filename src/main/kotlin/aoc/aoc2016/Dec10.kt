package aoc.aoc2016

import aoc.ktutils.*
import java.util.LinkedList

fun main() {
    execute(testLines()).let {
        // check(it.first, 0L)
        check(it.second, 30L)
        println("Test:   ${it}")
    }
    execute(readLines()).let {
        println("Result: ${it.first}")
        check(it.first, readAnswerAsLong(1))
        println("Result: ${it.second}")
        check(it.second, readAnswerAsLong(2))
    }
}

private interface Receiver { fun receive(token: Int) }
private data class Bot(val id: Int, val tokens: HashSet<Int>) : Receiver {
    override fun receive(token: Int) {
        if (tokens.size >= 2) throw RuntimeException("CMH")
        tokens.add(token)
    }
}
private data class Bin(val id: Int, val tokens: HashSet<Int>) : Receiver {
    override fun receive(token: Int) {
        tokens.add(token)
    }
}
private data class Rule(val id: Int, val minReceiver: Receiver, val maxReceiver: Receiver)

private fun execute(input: List<String>): Pair<Long, Long> {

    val bots = HashMap<Int, Bot>()
    val bins = HashMap<Int, Bin>()
    val rules = HashMap<Int, Rule>()  // id, low-to, high-to
    val actions = LinkedList<Bot>()
    var comparer = 0

    for (line in input) {
        if (line.startsWith("bot ")) { // bot 2 gives low to bot 1 and high to bot 0
            val tokens = line.split(" ")
            val botId = tokens[1].toInt()
            val minRcv = if (tokens[5].equals("bot")) getBot(bots, tokens[6].toInt()) else getBin(bins, tokens[6].toInt())
            val maxRcv = if (tokens[10].equals("bot")) getBot(bots, tokens[11].toInt()) else getBin(bins, tokens[11].toInt())
            rules.put(botId, Rule(botId, minRcv, maxRcv))
        }
        else if (line.startsWith("value ")) { // value 5 goes to bot 2
            val tokens = line.split(" ")
            val bot = getBot(bots, tokens[5].toInt())
            bot.tokens.add(tokens[1].toInt())
            if (bot.tokens.size == 2)
                actions.addLast(bot)
        }
        else throw RuntimeException("Unhandled line: $line")
    }

    while (!actions.isEmpty()) {
        val bot = actions.removeAt(0)
        val rule = rules.get(bot.id)!!
        val min = bot.tokens.min()
        rule.minReceiver.receive(min)
        if (rule.minReceiver is Bot && rule.minReceiver.tokens.size == 2)
            actions.addLast(rule.minReceiver)
        val max = bot.tokens.max()
        rule.maxReceiver.receive(max)
        if (rule.maxReceiver is Bot && rule.maxReceiver.tokens.size == 2)
            actions.addLast(rule.maxReceiver)
        bot.tokens.clear()

        // println("Bot ${bot.id} --($min)-> ${rule.minReceiver}    --($max)-> $${rule.maxReceiver}")

        if (min == 17 && max == 61)
            comparer = bot.id
    }

    return comparer.toLong() to 1L * bins[0]!!.tokens.first() * bins[1]!!.tokens.first() * bins[2]!!.tokens.first()
}

private fun getBot(bots: HashMap<Int, Bot>, id: Int): Bot {
    var bot = bots.get(id)
    if (bot == null) {
        bot = Bot(id, HashSet())
        bots.put(id, bot)
    }
    return bot
}

private fun getBin(bins: HashMap<Int, Bin>, id: Int): Bin {
    var bin = bins.get(id)
    if (bin == null) {
        bin = Bin(id, HashSet())
        bins.put(id, bin)
    }
    return bin
}