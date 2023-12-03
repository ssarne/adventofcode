package aoc.ktutils

data class Pos(val p: Point, val dir: Char) {

    private val directions = arrayOf('^', '>', 'v', '<')
    fun right() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) + 1), 4)])
    fun left() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) - 1), 4)])

    fun move() = move(1)

    fun move(distance: Int): Pos {
        return when (dir) {
            '^' -> Pos(Point(p.x, p.y - distance), dir)
            '>' -> Pos(Point(p.x + distance, p.y), dir)
            'v' -> Pos(Point(p.x, p.y + distance), dir)
            '<' -> Pos(Point(p.x - distance, p.y), dir)
            else -> throw RuntimeException("CMH $dir")
        }
    }
}