package aoc.ktutils

class Node(val name: String, val edges: MutableList<Edge>) {
    var visited: Boolean = false
    override fun toString(): String {
        return "$name ($visited) ${edges.size}"
    }
}

class Edge(val name: String, val cost: Int, val from: Node, val to: Node) {
    override fun toString(): String {
        return name
    }
}
