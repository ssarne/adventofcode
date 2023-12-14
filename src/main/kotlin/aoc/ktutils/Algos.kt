package aoc.ktutils

import kotlin.math.abs

fun binarySearch(low: Long, high: Long, calculate: (Long) -> Long, analyze: (Long) -> Boolean): Long {

    var i = low
    var j = high
    while (abs(j-i) > 1) {
        val k = (i + j) / 2
        val n = calculate(k)
        if (analyze(n)) i = k else j = k
    }
    var res = i
    for (k in i..j) {
        val dist = calculate(k)
        if (analyze(dist)) res = k
    }
    return res
}

fun addUp(current: Int, cycle: Int, limit: Int): Int {
    if (current + cycle > limit)
        return 0
    var jump = addUp(current, 2 * cycle, limit)
    while (current + jump + cycle < limit)
        jump += cycle

    return jump
}

/**
 * Least common multiplier
 * lcm(a,b,c) = lcm(a,lcm(b,c))
 */
fun lcm(vararg numbers: Long): Long {
    var res = lcm(numbers[0], numbers[1])
    for (i in 2 until numbers.size)
        res = lcm(res, numbers[i])
    return res
}

/**
 * Least common multiplier
 */
fun lcm(number1: Long, number2: Long): Long {
    return number1 * number2 /  gcd (number1 , number2)
}

/**
 * The greatest common divisor (GCD) of two integers (numbers),
 * the largest number that divides them both without a remainder
 * Euclidian algorithm
 */
fun gcd(a: Long, b: Long): Long {
    return if (a == 0L) b else gcd(b % a, a)
}
