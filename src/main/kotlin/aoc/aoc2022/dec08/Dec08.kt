package aoc.aoc2022.dec08

import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines()), 21)
    execute1(readLines()).let { println(it); check(it, 0) }

    check(execute2(readTestLines()), 8)
    execute2(readLines()).let { println(it); check(it, 440640) }
}

private fun execute1(input: List<String>): Int {

    var num = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            var visible1 = true
            for (i in 0 until x) {
               if (input[y][i] >= input[y][x]) visible1 = false
            }
            var visible2 = true
            for (i in x+1 until input[y].length) {
                if (input[y][i] >= input[y][x]) visible2 = false
            }

            var visible3 = true
            for (i in 0 until y) {
                if (input[i][x] >= input[y][x]) visible3 = false
            }
            var visible4 = true
            for (i in y+1 until input.size) {
                if (input[i][x] >= input[y][x]) visible4 = false
            }
            if (visible1 || visible2 || visible3 || visible4) num++
        }
    }
    return num
}

private fun execute2(input: List<String>): Int {

    var max = 0
    for (y in input.indices) {
        for (x in input[y].indices) {
            var num1 = 0
            for (i in 0 until x) {
                num1++
                if (input[y][x-i-1] >= input[y][x]) break
            }

            var num2 = 0
            for (i in x+1 until input[y].length) {
                num2++
                if (input[y][i] >= input[y][x]) break
            }

            var num3 = 0
            for (i in 0 until y) {
                num3++
                if (input[y-i-1][x] >= input[y][x]) break
            }

            var num4 = 0
            for (i in y+1 until input.size) {
                num4++
                if (input[i][x] >= input[y][x]) break
            }

            var num = num1 * num2 * num3 * num4
            if (num > max) {
                max = num
            }
        }
    }
    return max
}

