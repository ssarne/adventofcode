package aoc.aoc2017

import aoc.ktutils.check
import aoc.ktutils.readLines
import aoc.ktutils.readTestLines
import java.lang.RuntimeException
import java.util.*

private data class Tree(var label: String, var weight: Int, var tot: Int = 0, var children: MutableSet<Tree> = HashSet())
private data class Input(
    var nodes: MutableMap<String, Tree>,  // label -> node
    var parents: MutableMap<String, Tree>,  // label -> parent node
    var children: MutableMap<String, MutableSet<String>> // label -> set of children
)

private data class BalanceResult(var result: Boolean = true, var node: Tree? = null, var adjusted: Int = 0)

fun main() {
    check(findRoot(parse(readTestLines())).label, "tknk")
    println(findRoot(parse(readLines())).label) // ahnofa
    check((isBalanced(findRoot(parse(readTestLines())))).node?.label, "ugml")
    check((isBalanced(findRoot(parse(readTestLines())))).adjusted, 60)
    println(isBalanced(findRoot(parse(readLines()))).adjusted) // 802
}

private fun isBalanced(tree: Tree): BalanceResult {
    if (tree.children.size == 0) return BalanceResult()
    if (tree.children.size == 1) return isBalanced(tree.children.first())
    if (tree.children.size == 2) {
        if (tree.children.first().tot != tree.children.last().tot) {
            throw RuntimeException("CMH - Handle the unbalanced pair calculation")
            // return BalanceResult(false, tree, -1)
        }
        val result1 = isBalanced(tree.children.first())
        if (!result1.result) return result1

        val result2 = isBalanced(tree.children.last())
        if (!result2.result) return result2
        return BalanceResult()
    }

    for (child in tree.children) {
        val result = isBalanced(child)
        if (!result.result) return result
    }

    val num = tree.children.first().tot
    for (child in tree.children) {
        if (child.tot != num) {
            return findOutlier(tree)
        }
    }

    return BalanceResult()
}

private fun findOutlier(tree: Tree): BalanceResult {

    var first: Tree? = null
    var count1 = 0
    var second: Tree? = null
    var count2 = 0
    for (c in tree.children) {
        if (first == null) {
            first = c
            count1 = 1
            continue
        }

        if (first.tot == c.tot) {
            count1++
            continue
        }

        if (second == null) {
            second = c
            count2 = 1
            continue
        }

        if (second.tot == c.tot) {
            count2++
            continue
        }
        throw RuntimeException("WTF")
    }

    if (first != null && second != null) {
        if (count1 == 1) return BalanceResult(false, first, first.weight + (second.tot - first.tot))
        if (count2 == 1) return BalanceResult(false, second, second.weight + (first.tot - second.tot))
    }
    return BalanceResult()
}


private fun calcWeigths(input: Input): Int {
    return calcWeigths(findRoot(input))
}

private fun calcWeigths(tree: Tree): Int {
    var sum = 0
    for (child in tree.children) {
        sum += calcWeigths(child)
    }
    tree.tot = sum + tree.weight
    return tree.tot
}


private fun findRoot(input: Input): Tree {
    for (node in input.nodes.values) {
        if (!input.parents.containsKey(node.label))
            return node
    }
    throw RuntimeException("Failed")
}

private fun parse(lines: List<String>): Input {

    val input = Input(HashMap(), HashMap(), HashMap())
    for (line in lines) {
        val tokens = line.split(" ")
        val p = Tree(tokens[0], tokens[1].removePrefix("(").removeSuffix(")").toInt())
        input.nodes[p.label] = p
        if (tokens.size > 2) {
            val children = HashSet<String>()
            for (i in 3 until tokens.size) {
                val child = tokens[i].removeSuffix(",")
                input.parents[child] = p
                children.add(child)
            }
            input.children[p.label] = children
        }
    }

    for (node in input.parents) {
        val child = input.nodes[node.key]
        if (child != null) node.value.children.add(child)
    }

    calcWeigths(input)
    return input
}

