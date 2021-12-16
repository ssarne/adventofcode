package aoc.aoc2021

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute1("D2FE28"), 6)
    check(execute1("38006F45291200"), 9)
    check(execute1("EE00D40C823060"), 14)
    check(execute1("8A004A801A8002F478"), 16)
    check(execute1("620080001611562C8802118E34"), 12)
    check(execute1("C0015000016115A2E0802F182340"), 23)
    execute1(readText()).let { println(it); check(it, 986) }

    check(execute2("C200B40A82"), 3L)
    check(execute2("04005AC33890"), 54L)
    check(execute2("880086C3E88112"), 7L)
    check(execute2("CE00C43D881120"), 9L)
    check(execute2("D8005AC2A8F0"), 1L)
    check(execute2("F600BC2D8F"), 0L)
    check(execute2("9C005AC2F8F0"), 0L)
    check(execute2("9C0141080250320F1802104A08"), 1L)
    execute2(readText()).let { println(it); check(it, 18234816469452L) }
}

private fun execute1(input: String): Int {
    val message = hexToBinary0Padded(input)
    val (packet, _) = readPacket(message, 0)
    return packet.versionSum()
}

private fun execute2(input: String): Long {
    val message = hexToBinary0Padded(input)
    val (packet, _) = readPacket(message, 0)
    return packet.number
}

private data class Packet(
    val version: Int,
    val type: Int,
    val pos: Int,
    val subpackets: MutableList<Packet>,
    var number: Long = 0
) {
    fun versionSum(): Int {
        var sum = this.version
        for (packet in this.subpackets) {
            sum += packet.versionSum()
        }
        return sum
    }
}

private fun readPacket(message: String, pos: Int): Pair<Packet, Int> {

    var packet = Packet(
        message.substring(pos, pos + 3).toInt(2),
        message.substring(pos + 3, pos + 6).toInt(2),
        pos, ArrayList<Packet>()
    )
    var i = pos + 6

    if (packet.type == 4) { // literal package, tuple of 5, until leading bit is 0
        var literal = ""
        do {
            var more = message[i++]
            literal += message.substring(i, i + 4)
            i += 4
        } while (more == '1')
        packet.number = literal.toLong(2)
        return Pair(packet, i)
    }

    var lengthType = message[i++]
    if (lengthType == '0') {
        var length = message.substring(i, i + 15).toInt(2)
        i += 15
        var end = i + length

        do {
            val res = readPacket(message, i)
            packet.subpackets.add(res.first)
            i = res.second
        } while (i < end)

    } else { // lengthtype '1'
        var packets = message.substring(i, i + 11).toInt(2)
        i += 11
        for (j in 1..packets) {
            val res = readPacket(message, i)
            packet.subpackets.add(res.first)
            i = res.second
        }
    }

    when (packet.type) {
        0 -> packet.number = packet.subpackets.map { it.number }.sum()
        1 -> packet.number = packet.subpackets.map { it.number }.reduce {p1, p2 -> p1 * p2}
        2 -> packet.number = packet.subpackets.map { it.number }.min()!!
        3 -> packet.number = packet.subpackets.map { it.number }.max()!!
        5 -> packet.number = packet.subpackets.map { it.number }.reduce {l1, l2 -> if (l1 > l2) 1L else 0L }
        6 -> packet.number = packet.subpackets.map { it.number }.reduce {l1, l2 -> if (l1 < l2) 1L else 0L }
        7 -> packet.number = packet.subpackets.map { it.number }.reduce {l1, l2 -> if (l1 == l2) 1L else 0L }
        else -> throw RuntimeException("wat " + packet.type)
    }

    return Pair(packet, i)
}