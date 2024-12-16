package aoc.ktutils

data class Pos(val p: Point, val dir: Char) {

    fun right() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) + 1), 4)])
    fun left() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) - 1), 4)])

    fun move() = move(1)
    fun back() = move(-1)

    fun move(distance: Int): Pos {
        return when (dir) {
            '^' -> Pos(Point(p.x, p.y - distance), dir)
            '>' -> Pos(Point(p.x + distance, p.y), dir)
            'v' -> Pos(Point(p.x, p.y + distance), dir)
            '<' -> Pos(Point(p.x - distance, p.y), dir)
            else -> throw RuntimeException("CMH $dir")
        }
    }

    companion object {
        private val directions = arrayOf('^', '>', 'v', '<')
        fun direction(from: Point, to: Point): Char {
            if (from.y == to.y && from.x < to.x) return '>'
            if (from.y == to.y && from.x > to.x) return '<'
            if (from.x == to.x && from.y < to.y) return 'v'
            if (from.x == to.x && from.y > to.y) return '^'
            throw RuntimeException("CMH $from -> $to")
        }
    }
}