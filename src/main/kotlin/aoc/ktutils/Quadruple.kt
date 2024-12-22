package aoc.ktutils

import java.io.Serializable

data class Quadruple<out S, out T, out U, out V>(val first: S, val second: T, val third: U, val fourth: V) : Serializable
