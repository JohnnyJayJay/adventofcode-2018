package one

import java.io.File

val input =  File("01/input.txt").useLines { it.toList() }.map { it.toInt() }

fun partOne(): Int {
    var solution = 0
    input.forEach { solution = Math.addExact(solution, it) }
    return solution
}

fun partTwo(): Int {
    val numbers = mutableSetOf<Int>()
    var index = 0
    while (true) {
        input.forEach {
            index += it
            if (numbers.contains(index))
                return index
            else
                numbers.add(index)
        }
    }
    return 0
}

fun main(args: Array<String>) {
    println("Part one solution: ${partOne()}")
    println("Part two solution: ${partTwo()}")
}