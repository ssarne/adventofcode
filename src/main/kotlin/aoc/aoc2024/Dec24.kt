package aoc.aoc2024

import aoc.ktutils.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 4L) }    // ; println("Test: $it") }
    execute1(readTestLines(2)).let { check(it, 2024L) } //  ; println("Test: $it") }
    execute1(readLines()).let { println(it) } // ; check(it, readAnswerAsLong(1)) }

    execute2(readLines()).let { println(it) ; check(it, readAnswer(2)) }
}

private data class Gate(val id: String, val in1: String, val in2: String, val out: String, val type: String)

private fun execute1(input: List<String>): Long {

    val (signals, gates) = parse(input)

    do {
        var changed = false
        for (gate in gates) {
            val s1 = signals[gate.in1]
            val s2 = signals[gate.in2]
            if (s1 != null && s2 != null) {
                val out = when (gate.type) {
                    "AND" -> s1 == 1 && s2 == 1
                    "OR" -> s1 == 1 || s2 == 1
                    "XOR" -> (s1 == 0 && s2 == 1) || (s1 == 1 && s2 == 0)
                    else -> throw RuntimeException("CMH ${gate.type}")
                }
                if (!signals.containsKey(gate.out)) changed = true
                signals[gate.out] = if (out) 1 else 0
                // println("${gate.in1} ${gate.type} ${gate.in2} -> ${gate.out} ${signals.get(gate.out)}")
            }
        }
    } while (changed)

    val zs = signals
        .map { it.key }
        .filter { it.startsWith("z") }
        .sorted()

    return zs.withIndex()
        .sumOf { (i, it) -> signals[it]?.times((2L pow i.toLong())) ?: 0 }
}

private fun parse(input: List<String>): Pair<MutableMap<String, Int>, MutableSet<Gate>> {
    val signals = mutableMapOf<String, Int>()
    val gates = mutableSetOf<Gate>()

    for ((i, line) in input.withIndex()) {
        if (line.contains(":")) {
            val signal = line.substring(0, line.indexOf(":"))
            val value = line.substring(line.indexOf(": ") + 2, line.length).toInt()
            if (signals.contains(signal)) throw RuntimeException("CMH $signal")
            signals.put(signal, value)
        }
        if (line.contains("->")) {
            val tokens = line.split(" ")
            gates.add(Gate("gate $i", tokens[0], tokens[2], tokens[4], tokens[1]))
        }
    }
    return Pair(signals, gates)
}

// Adder gate:
// xn XOR yn -> mn        mn XOR nn   -> zn
// xn AND yn -> nn        mn AND cn-1 -> rn       nn  OR cn-1 -> cn
//
// x13 XOR y13 -> kvr     hgw XOR kvr -> _npf_
// x13 AND y13 -> fmh     hgw AND kvr -> tqs      fmh OR tqs -> _z13_
// swap npf/z13
//
// x19 XOR y19 -> fnq     rsm XOR fnq -> _cph_
// y19 AND x19 -> _z19_
// swap z19/cph
//
// x33 XOR y33 -> wtm     wtm XOR wgq -> _hgj_
// x33 AND y33 -> fvk     wgq AND wtm -> _z33_    fvk OR hgj -> wvn
// swap hgj/z33
//
// y09 XOR x09 -> _nnt_   pcd XOR gws -> z09
// x09 AND y09 -> _gws_   gws AND pcd -> tqw    nnt OR tqw -> hcb
// carry: pcd
//
private fun execute2(input: List<String>): String {

    val (_, gates) = parse(input)
    val swaps = mutableSetOf<String>()

    swapWires(gates, swaps,
        getGate(gates, "y09", "XOR", "x09", "nnt"),
        getGate(gates, "x09", "AND", "y09", "gws")
    )

    swapWires(gates, swaps,
        getGate(gates, "hgw", "XOR", "kvr", "npf"),
        getGate(gates, "fmh", "OR", "tqs", "z13")
    )

    swapWires(gates, swaps,
        getGate(gates, "rsm", "XOR", "fnq", "cph"),
        getGate(gates, "y19", "AND", "x19", "z19")
    )

    swapWires(gates, swaps,
        getGate(gates, "wtm", "XOR", "wgq", "hgj"),
        getGate(gates, "wgq", "AND", "wtm", "z33")
    )

    // Match adder gate logic
    var carry: String? = null
    for (i in 0..44) { // skip end adder (no carry)
        val xn = "x" + if (i < 10) "0" + i else i    // input x
        val yn = "y" + if (i < 10) "0" + i else i    // input y
        val mn: String? = find(gates, "m$i", xn, yn, "XOR")
        val nn: String? = find(gates, "n$i", xn, yn, "AND")
        val zn: String? = if (i == 0) mn else find(gates, "z$i", carry, mn, "XOR") // no carry gates for first adder
        val rn: String? = if (i == 0) mn else find(gates, "r$i", carry, mn, "AND")
        carry = if (i == 0) nn else find(gates, "c$i", rn, nn, "OR")
        if (zn == null || !zn.startsWith("z")) throw RuntimeException("Failed to find z$i")
        if (carry == null) throw RuntimeException("Failed to find carry for $i")
    }

    return swaps.toList().sorted().joinToString(",")
}

private fun swapWires(gates: MutableSet<Gate>, swaps: MutableSet<String>, gate1: Gate, gate2: Gate) {
    gates.remove(gate1)
    gates.remove(gate2)
    gates.add(Gate(gate1.id, gate1.in1, gate1.in2, gate2.out, gate1.type))
    gates.add(Gate(gate2.id, gate2.in1, gate2.in2, gate1.out, gate2.type))
    swaps.add(gate1.out)
    swaps.add(gate2.out)
}

private fun getGate(gates: MutableSet<Gate>, in1: String, op: String, in2: String, out: String): Gate {
    for (gate in gates) {
        if (gate.in1 == in1 && gate.in2 == in2 && gate.type == op && gate.out == out)
            return gate
        if (gate.in1 == in2 && gate.in2 == in1 && gate.type == op && gate.out == out)
            return gate
    }
    throw RuntimeException("Fail to find gate $in1 $op $in2 -> $out")
}

private fun find(gates: MutableSet<Gate>, label: String, in1: String?, in2: String?, op: String): String? {

    var signal: String? = null

    for (gate in gates) {
        if (((gate.in1 == in1 && gate.in2 == in2) || (gate.in2 == in1 && gate.in1 == in2)) && gate.type == op)
            if (signal != null)
                println("Multiple candidates for $label: $signal ${gate.out}")
            else
                signal = gate.out
    }

    if (signal == null)
        println("Missing signal for $label")

    return signal
}