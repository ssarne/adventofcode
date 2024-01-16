package aoc.aoc2023

import aoc.ktutils.*
import java.util.*
import kotlin.collections.HashMap


fun main() {
    check(execute(readTestLines(1), 1), 4 * 8)
    check(execute(readTestLines(2), 4), 187)
    check(execute(readTestLines(1), 1000), 32000000)
    check(execute(readTestLines(2), 1000), 11687500)
    execute(readLines(), 1000).let { println(it); check(it, readAnswerAsLong(1)) }

    execute(readLines(), null, "rx").let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute(input: List<String>, pushes: Long? = null, target: String? = null): Long {

    val modules = parse(input)
    val signals = LinkedList<Signal>()
    var highs = 0L
    var lows = 0L

    val source: ConjunctionModule? = modules.values.find { it.connections.contains(target) } as ConjunctionModule?
    val sourceSignals = HashMap<String, Long>()

    var push = 0L
    while (true) {

        // Part 1, push the button a certain amount of times
        if (pushes != null && push == pushes) break

        signals.add(Signal("low", "button", "broadcaster"))
        push++

        while (signals.isNotEmpty()) {

            val signal = signals.pop()
            // println("${signal.source} -${signal.type}-> ${signal.destination}")

            if (signal.type == "high") highs++
            if (signal.type == "low") lows++

            val module = modules[signal.destination]!!
            signals.addAll(module.receive(signal))

            // Part 2, target has low signal (not happening in reasonable time though)
            if (target != null && signal.destination == target && signal.type == "low")
                return push

            // Part 2, track signals to the source for the target (a conjunction)
            // Detect the interval how often they are sent
            // When the intervals are detected for all incoming signals to the source
            // Return the lowest common multiplier of the intervals
            if (source != null && source.name == signal.destination && signal.type == "high") {
                val current = sourceSignals[signal.source]
                if (current == null) {
                    sourceSignals[signal.source] = push
                } else {
                    sourceSignals[signal.source] = push
                    sourceSignals[signal.source + "-interval"] = push - current
                }

                if (sourceSignals.size == 2 * source.memory.size) {
                    var lcm = 1L
                    for (s in sourceSignals)
                        if (s.key.contains("interval"))
                            lcm *= s.value
                    return lcm
                }
            }
        }
    }

    return lows * highs
}

private fun parse(input: List<String>): Map<String, Module> {

    val modules = HashMap<String, Module>()
    for (line in input) {

        val identifier = line.split(" -> ")[0]
        val connections = line.split(" -> ")[1].split(", ")

        when (identifier[0]) {
            '%' -> {
                val name = identifier.substring(1)
                modules[name] = FliFlopModule(name, connections)
            }
            '&' -> {
                val name = identifier.substring(1)
                modules[name] = ConjunctionModule(name, connections)
            }
            'b' -> {
                val name = "broadcaster"
                modules[name] = BroadcastModule(name, connections)
            }
        }
    }

    modules["output"] = OutputModule("output", emptyList()) // Add the output module
    modules["rx"] = modules["output"]!! // Assume the undefined target "rx" is the output module

    // wire up conjunctions
    for (m in modules.values)
        if (m is ConjunctionModule)
            for (n in modules.values)
                if (m.name != n.name)
                    if (n.connections.contains(m.name))
                        m.memory[n.name] = "low"

    return modules
}

private data class Signal(val type: String, val source: String, val destination: String)

private abstract class Module() {
    abstract val name: String
    abstract val connections: List<String>
    abstract fun receive(signal: Signal): List<Signal>
}

private data class OutputModule(override val name: String, override val connections: List<String>) : Module() {

    var count = 0L
    override fun receive(signal: Signal): List<Signal> {
        count++
        return emptyList()
    }
}

private data class ConjunctionModule(override val name: String, override val connections: List<String>) : Module() {

    var memory = HashMap<String, String>()

    override fun receive(signal: Signal): List<Signal> {
        memory[signal.source] = signal.type
        var signalType = "low"
        for (s in memory.values) if (s == "low") signalType = "high"
        return connections.map { Signal(signalType, name, it) }.toList()
    }
}

private data class FliFlopModule(override val name: String, override val connections: List<String>) : Module() {

    var state = "off"

    override fun receive(signal: Signal): List<Signal> {
        if (signal.type == "low") {
            if (state == "off") {
                state = "on"
                return connections.map { Signal("high", name, it) }.toList()
            } else {
                state = "off"
                return connections.map { Signal("low", name, it) }.toList()
            }
        }
        return emptyList()
    }
}

private data class BroadcastModule(override val name: String, override val connections: List<String>) : Module() {

    override fun receive(signal: Signal): List<Signal> {
        return connections.map { Signal(signal.type, "broadcaster", it) }.toList()
    }
}
