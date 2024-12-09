package aoc.aoc2024


import aoc.ktutils.*

fun main() {
    check(execute1(readTestLines(1)), 1928)
    execute1(readLines()).let { println(it); check(it, readAnswerAsLong(1)) }

    check(execute2(readTestLines(1)), 2858)
    execute2(readLines()).let { println(it); check(it, readAnswerAsLong(2)) }
}

private fun execute1(input: List<String>): Long {

    val disk = parseToArray(input)
    // printArrayDisk(disk)

    var pos = disk.indices.last
    for (i in disk.indices) {
        if (disk[i] == -1) {
            disk[i] = disk[pos]
            disk[pos] = -1
        }
        while (disk[pos] == -1 && pos > i) pos--
        if (i >= pos) break
    }

    return checksumArrayDisk(disk)
}

private fun parseToArray(input: List<String>): MutableList<Int> {
    val line = input.first()
    val disk = mutableListOf<Int>()
    var content = true
    var index = 0
    for (c in line) {
        val len = c.toString().toInt()
        for (i in 0 until len)
            disk.add(if (content) index else -1)
        if (content) index++
        content = !content
    }
    return disk
}

private fun printArrayDisk(disk: MutableList<Int>) {
    for (i in disk.indices)
        print(if (disk[i] == -1) '.' else disk[i])
    println()
}

private fun checksumArrayDisk(disk: MutableList<Int>): Long {
    var sum = 0L
    for (i in disk.indices) {
        if (disk[i] != -1) {
            sum += i.toLong() * disk[i]
            // println(" $i * ${disk[i]} ")
        }
    }
    return sum
}

private data class Block(var addr: Int, var len: Int, var free: Boolean, var content: Int) {
    
    var prev: Block = this
    var next: Block = this

    fun mergeNext(): Block {
        len += next.len
        next = next.next
        next.prev = this
        return this
    }

    fun split(size: Int): Block {
        val block = Block(addr + size, len - size, free, content)
        len = size
        return insertAfter(block)
    }

    fun insertAfter(block: Block): Block {
        block.prev = this
        block.next = this.next
        block.next.prev = block
        this.next = block
        return block
    }
}

private fun execute2(input: List<String>): Long {

    val line = input.first()
    val disk = parseToBlocks(line)

    var fragment = disk.prev // try to merge last block
    while (fragment != disk) {

        // printBlockDisk(disk)

        if (fragment.free) {
            fragment = fragment.prev
            continue
        }

        var candidate = disk.next
        while (candidate != fragment) {
            if (candidate.free && candidate.len >= fragment.len) {
                if (candidate.len > fragment.len)
                    candidate.split(fragment.len)

                candidate.content = fragment.content
                candidate.free = false

                if (fragment.prev.free)
                    fragment.prev.mergeNext()
                else
                    fragment.free = true
                break
            }
            candidate = candidate.next
        }
        fragment = fragment.prev
    }

    return checksumBlocksDisk(disk)
}

private fun parseToBlocks(line: String): Block {

    val disk = Block(0, 0, false, -1)
    var block = disk
    var content = true
    var value = 0

    for (c in line) {
        val len = c.toString().toInt()
        if (len != 0)
            block = block.insertAfter(Block(block.addr + block.len, len, !content, if (content) value else -1))
        if (content) value++
        content = !content
    }
    return disk
}

private fun printBlockDisk(disk: Block) {

    var block = disk
    do {
        for (i in 0 until (block.len))
            print(if (block.free) '.' else block.content)
        block = block.next
    } while (block != disk)
    println()
}

private fun checksumBlocksDisk(disk: Block): Long {
    var sum = 0L
    var current = disk
    do {
        if (!current.free) {
            for (j in 0 until current.len) {
                sum += (current.addr.toLong() + j) * current.content
                // println(" $i * ${disk[i].content} ")
            }
        }
        current = current.next
    } while (current != disk)
    return sum
}