package aoc.aoc2017
import aoc.ktutils.check
import java.lang.RuntimeException
import java.util.*

fun main() {
    check(after(spinlock(9, 3), 9), 5)
    check(after(spinlock(2017, 3), 2017), 638)
    println(after(spinlock(2017, 366), 2017)) // 1025
    check(spinlockNextArray(9, 3)[9], 5)
    check(spinlockNextArray(2017, 3)[2017], 638)
    println(spinlockNextArray(50000000, 366)[0]) // 37803463
}

/**
 * Create the spinlock with a linked list
 * Values in the positions they are inserted
 */
fun spinlock(size: Int, step: Int): List<Int> {
    var spin = LinkedList<Int>()
    var i = 0
    spin.add(0)
    for (n in 1 .. size) {
        i = (i + step) % spin.size
        spin.add(++i, n)
        // println("[$i] $spin")
    }
    return spin;
}

fun after(spin: List<Int>, value: Int): Int {
    for (i in spin.indices)
        if (spin[i] == value)
            return spin[(i+1) % spin.size]
    throw RuntimeException("CMH")
}

/**
 * Create the spinlock with an int array
 * Each value is stored as index, pointing to it's next
 * 0 -> 2 -> 1 as [2, 0, 1]
 */
fun spinlockNextArray(size: Int, step: Int): IntArray {
    var spin = IntArray(size + 1)
    spin[0] = 0
    var i = 0
    for (n in 1 .. size) {
        for (s in 1 .. step)
            i = spin[i]!!
        var tmp = spin[i]!!
        spin[i] = n
        spin[n] = tmp
        i = n
        // println("[$i] " + spinArrayToString(spin))
        // if (n % 100000 == 0) println(n)
    }
    return spin;
}

fun spinArrayToString(spin: IntArray): String {
    var s = "0"
    var i = 0
    for (n in 1 until spin.size) {
        i = spin[i]!!
        s += " $i"
    }
    return s
}
