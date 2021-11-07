package aoc.aoc2017

import aoc.ktutils.check

fun main() {

    check(manhattan(getSpiralPosition(1)), 0)
    check(manhattan(getSpiralPosition(12)), 3)
    check(manhattan(getSpiralPosition(23)), 2)
    check(manhattan(getSpiralPosition(1024)), 31)
    println(manhattan(getSpiralPosition(368078))) // 371

    check(stressTest(1), 2)
    check(stressTest(2), 4)
    check(stressTest(60), 122)
    check(stressTest(750), 806)
    println(stressTest(368078)) // 369601
}

data class Pos(var x: Int, var y: Int)

fun getSpiralPosition(number: Int): Pos {

    var pos = Pos(0,0)
    var step = 1
    var n = 1

    if (n == number) return pos

    while (true) {
        // right
        for (x in 1 .. step) {
            pos.x += 1
            n++
            if (n == number) return pos
        }

        // up
        for (y in 1 .. step) {
            pos.y += 1
            n++
            if (n == number) return pos
        }

        step++ // increase spiral size

        // left
        for (x in 1 .. step) {
            pos.x -= 1
            n++
            if (n == number) return pos
        }

        // down
        for (y in 1 .. step) {
            pos.y -= 1
            n++
            if (n == number) return pos
        }

        step++ // increase spiral size
    }
}

fun manhattan(pos: Pos): Int {
    return kotlin.math.abs(pos.x) + kotlin.math.abs(pos.y)
}

fun printStore(store: Map<Pos, Int>) {
    for (y in -3 .. 3) {
        for (x in -3 .. 3) {
            var n: Int = store.getOrDefault(Pos(x,-1*y), 0)
            print(" $n")
        }
        println("")
    }
    println("-------")
}

fun stressTest(number: Int): Int {

    val store: MutableMap<Pos, Int> = HashMap()
    var pos = Pos(0,0)
    var step = 1
    store[Pos(pos.x, pos.y)] = 1

    while (true) {

//         printStore(store)

        // right
        for (x in 1 .. step) {
            pos.x += 1
            val sum = sumAdjecent(store, pos)
            store[Pos(pos.x, pos.y)] = sum
            if (sum > number) return sum
        }

        // up
        for (y in 1 .. step) {
            pos.y += 1
            val sum = sumAdjecent(store, pos)
            store[Pos(pos.x, pos.y)] = sum
            if (sum > number) return sum
        }

        step++ // increase spiral size

        // left
        for (x in 1 .. step) {
            pos.x -= 1
            val sum = sumAdjecent(store, pos)
            store[Pos(pos.x, pos.y)] = sum
            if (sum > number) return sum
        }

        // down
        for (y in 1 .. step) {
            pos.y -= 1
            val sum = sumAdjecent(store, pos)
            store[Pos(pos.x, pos.y)] = sum
            if (sum > number) return sum
        }

        step++ // increase spiral size
    }
}

private fun sumAdjecent(store: MutableMap<Pos, Int>, pos: Pos): Int {
    var sum = store.getOrDefault(Pos(pos.x - 1, pos.y - 1), 0)
    sum += store.getOrDefault(Pos(pos.x - 1, pos.y), 0)
    sum += store.getOrDefault(Pos(pos.x - 1, pos.y + 1), 0)
    sum += store.getOrDefault(Pos(pos.x, pos.y - 1), 0)
    sum += store.getOrDefault(Pos(pos.x, pos.y + 1), 0)
    sum += store.getOrDefault(Pos(pos.x + 1, pos.y - 1), 0)
    sum += store.getOrDefault(Pos(pos.x + 1, pos.y), 0)
    sum += store.getOrDefault(Pos(pos.x + 1, pos.y + 1), 0)
    return sum
}