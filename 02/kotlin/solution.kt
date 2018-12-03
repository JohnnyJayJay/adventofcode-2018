package two

import java.io.File

val input = File("02/input.txt").useLines { it.toList() }

fun partOne(): Int {
    var twice = 0
    var three = 0
    input.forEach { string ->
        val letters = mutableMapOf<Char, Int>()
        string.toCharArray().forEach {
            if (it in letters) {
                val entry = (letters[it]!! + 1)
                letters[it] = entry
            } else
                letters[it] = 1
        }
        if (2 in letters.values)
            twice += 1
        if (3 in letters.values)
            three += 1
    }
    return twice * three
}

fun sameChars(one: String, two: String): Array<Char> {
    return sameChars(one.toCharArray(), two.toCharArray())
}


fun sameChars(one: CharArray, two: CharArray): Array<Char> {
    val chars = mutableListOf<Char>()
    for (i in 0 until one.size) {
        if (one[i] == two[i])
            chars.add(one[i])
    }
    return chars.toTypedArray()
}

fun countDifferentChars(one: String, two: String): Int {
    return countDifferentChars(one.toCharArray(), two.toCharArray())
}

fun countDifferentChars(one: CharArray, two: CharArray): Int {
    var count = 0
    for (i in 0 until one.size) {
        if (one[i] == two[i])
            count++
    }
    return one.size - count
}

fun partTwo(): String {
    input.forEach { one ->
        input.forEach { two ->
            var count = countDifferentChars(one, two)
            if (count == 1)
                return sameChars(one, two).joinToString(separator = "", truncated = "")
        }
    }
    return ""
}


    fun main(args: Array<String>) {
        println("Part one solution: ${partOne()}")
        println("Part two solution: ${partTwo()}")
    }
