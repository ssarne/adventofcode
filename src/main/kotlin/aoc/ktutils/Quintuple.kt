package aoc.ktutils

import java.io.Serializable

data class Quintuple<out S, out T, out U, out V, out W>(
    val first: S,
    val second: T,
    val third: U,
    val fourth: V,
    val fifth: W
) : Serializable