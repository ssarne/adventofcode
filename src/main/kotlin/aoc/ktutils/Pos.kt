package aoc.ktutils

data class Pos(val p: Point, val dir: Char) {

    private val directions = arrayOf('^', '>', 'v', '<')
    fun right() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) + 1), 4)])
    fun left() = Pos(p, directions[Math.floorMod((directions.indexOf(dir) - 1), 4)])
    fun move(): Pos {
        return when (dir) {
            '^' -> Pos(Point(p.x, p.y - 1), dir)
            '>' -> Pos(Point(p.x + 1, p.y), dir)
            'v' -> Pos(Point(p.x, p.y + 1), dir)
            '<' -> Pos(Point(p.x - 1, p.y), dir)
            else -> throw RuntimeException("CMH $dir")
        }
    }
}