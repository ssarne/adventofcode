package aoc.aoc2022.dec07

import aoc.ktutils.*
import kotlin.math.*
import java.lang.RuntimeException

fun main() {
    check(execute1(readTestLines()), 95437)
    execute1(readLines()).let { println(it); check(it, 2104783) }

    check(execute2(readTestLines()), 24933642)
    execute2(readLines()).let { println(it); check(it, 5883165) }
}

data class Dir(
    val name: String,
    val parent: Dir?,
    val files: HashMap<String, Int> = HashMap(),
    val dirs: HashMap<String, Dir> = HashMap()
)

private fun execute1(input: List<String>): Int {
    val root: Dir = parse(input)
    val sizes = HashMap<String, Int>()
    count(root, root.name, sizes)
    var sum = 0
    for (size in sizes.values) if (size < 100000) sum += size
    return sum
}

private fun execute2(input: List<String>): Int {
    val root: Dir = parse(input)
    val sizes = HashMap<String, Int>()
    val tot = count(root, root.name, sizes)
    val free = 70000000 - tot
    val needed = 30000000 - free
    var current = tot
    for (size in sizes.values) if (size in needed until current) current = size
    return current
}

fun count(current: Dir, path: String, sizes: HashMap<String, Int>): Int {
    var sum = 0
    for (dir in current.dirs.values) {
        sum += count(dir, path + "/" + dir.name, sizes)
    }
    for (size in current.files.values) {
        sum += size
    }
    sizes.put(path, sum)
    return sum
}

private fun parse(input: List<String>): Dir {
    var root = Dir("/", null)
    var current = root
    for (line in input) {
        if (line == "\$ cd /") {
            current = root
        } else if (line == "\$ cd ..") {
            current = current.parent!!
            continue
        } else if (line.startsWith("\$ cd ")) {
            val dir = line.split(" ")[2]
            current = current.dirs[dir]!!
            continue
        } else if (line == "\$ ls") {
            continue
        } else if (line.startsWith("dir ")) {
            val name = line.substring("dir ".length)
            current.dirs.put(name, Dir(name, current))
            continue
        } else if (line[0].isDigit()) {
            val pair = line.split(" ")
            current.files.put(pair[1], pair[0].toInt())
            continue
        } else {
            throw RuntimeException("CMH")
        }
    }
    return root
}