package aoc.ktutils



/* Breadth First Search

private data class Node(val name: String, val left: String, val right: String, var steps: Int)

while (queue.isNotEmpty()) {
    val pos = queue.pop()
    if (pos.name == "ZZZ") break  // success
    val left = map[pos.left]!!
    if (left.steps < 0) {
        left.steps = pos.steps + 1
        queue.add(left)
    }
    val right = map[pos.right]!!
    if (right.steps < 0) {
        right.steps = pos.steps + 1
        queue.add(right)
    }
}
*/