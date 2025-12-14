package aoc.aoc2025

import aoc.ktutils.*
import com.microsoft.z3.Context
import com.microsoft.z3.IntExpr
import com.microsoft.z3.Status
import java.util.*

fun main() {
    execute1(readTestLines(1)).let { check(it, 7L) ; println("Test: $it") }
    execute1(readLines()).let { println(it) ; check(it, readAnswerAsLong(1)) }
    execute2(readTestLines(1)).let { check(it, 33L) ; println("Test: $it") }
    execute2(readLines()).let { println(it) ; check(it, readAnswerAsLong(2)) }
}

private data class Machine(val lights: String, val buttons: List<List<Int>>, val joltages: List<Int>)

private fun execute1(input: List<String>): Long {

    val machines = parseMachines(input)

    var sum = 0L
    for (machine in machines) {
        sum += getNumberOfPresses(machine)
    }

    return sum
}

private fun execute2(input: List<String>): Long {

    val machines = parseMachines(input)

    var sum = 0L
    for (machine in machines) {
        sum += getNumberOfPressesWithLinearEquations(machine)
    }

    return sum
}

private fun getNumberOfPresses(machine: Machine): Int {

    val queue = LinkedList<Pair<String, Int>>()
    queue.add(machine.lights.replace("#", ".") to 0)
    while (queue.isNotEmpty()) {
        val (lights, presses) = queue.remove()
        for (b in machine.buttons) {
            val next = lights.toCharArray()
            for (l in b) next[l] = if (next[l] == '#') '.' else '#'
            var nexts = ""
            for (c in next) nexts += c
            if (nexts == machine.lights)
                return presses + 1
            queue.add(nexts to presses + 1)
        }
    }
    return 0
}

/*
[.##.] (3) (1,3) (2) (2,3) (0,2) (0,1) {3,5,4,7}

   x0    x1    x2    x3    x4    x5
   (3)   (1,3) (2)   (2,3) (0,2) (0,1)
0:                         x4    x5    = 3
1:       x1                      x5    = 5
2:             x2    x3    x4          = 4
3: x0    x1          x3                = 7
*/
private fun getNumberOfPressesWithLinearEquations(machine: Machine): Int {

    val ctx = Context()
    val solver = ctx.mkOptimize()
    val xs = ArrayList<IntExpr>()

    // Create variables for each button
    for (i in machine.buttons.indices) xs.add(ctx.mkIntConst("x$i"))
    // All variables are at least 0 (at least 0 presses)
    for (x in xs) solver.Add(ctx.mkGe(x, ctx.mkInt(0)))
    // Add equations for sum of presses by button
    for (j in machine.joltages.withIndex()) {
        val factors = ArrayList<IntExpr>()
        for (button in machine.buttons.withIndex())
            if (button.value.contains(j.index))
                factors.add(xs[button.index])
        solver.Add(ctx.mkEq(ctx.mkAdd(*factors.toTypedArray()), ctx.mkInt(j.value)))
    }

    // And store all button presses as the sum of the xs
    val presses = ctx.mkIntConst("presses")
    solver.Add(ctx.mkEq(ctx.mkAdd(*xs.toTypedArray()), presses))

    // Minimize total number of presses
    solver.MkMinimize(ctx.mkAdd(presses))
    if (solver.Check() != Status.SATISFIABLE) throw RuntimeException("CMH")
    val sum = solver.model.evaluate(presses, false).toString().toInt()
    return sum
}


private fun parseMachines(input: List<String>): ArrayList<Machine> {
    val machines = ArrayList<Machine>()
    for (line in input) {
        val splits = line.split(" ")
        val lights = splits[0].substring(1, splits[0].length - 1)
        val buttons = ArrayList<List<Int>>()
        for (i in 1 until splits.size - 1)
            buttons.add(splits[i].replace("(", "").replace(")", "").split(",").map { it.toInt() }.toList())
        val joltages = splits.last().replace("{", "").replace("}", "").split(",").map { it.toInt() }.toList()
        machines.add(Machine(lights, buttons, joltages))
    }
    return machines
}
