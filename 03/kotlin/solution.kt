package three

import java.io.File

val input = File("03/input.txt").useLines { it.toList() }

fun drawFabric(): Array<CharArray> {
    val out = mutableSetOf<CharArray>()
    for (i in 0 until 1000)
        out.add(".".repeat(1000).toCharArray())
    return out.toTypedArray()
}

data class Claim(val id: Int, val leftSpace: Int, val topSpace: Int, val width: Int, val height: Int) {
    companion object {
        private val idRegex = Regex("(?<=#)\\d+")
        private val leftSpaceRegex = Regex("(?<=@ )\\d+")
        private val topSpaceRegex = Regex("(?<=,)\\d+")
        private val widthRegex = Regex("(?<=: )\\d+")
        private val heightRegex = Regex("(?<=x)\\d+")
        @JvmStatic
        fun fromInput(input: String): Claim {
            val id = idRegex.find(input)!!.value.toInt()
            val leftSpace = leftSpaceRegex.find(input)!!.value.toInt()
            val topSpace = topSpaceRegex.find(input)!!.value.toInt()
            val width = widthRegex.find(input)!!.value.toInt()
            val height = heightRegex.find(input)!!.value.toInt()
            return Claim(id, leftSpace, topSpace, width, height)
        }

    }

    fun isOverlapping(fabric: Array<CharArray>): Boolean {
        val x = leftSpace
        val y = topSpace
        for (i in 0 until width)
            for (j in 0 until height)
                if (fabric[x + i][y + j] == 'X')
                    return true
        return false
    }


    fun drawClaim(fabric: Array<CharArray>): Array<CharArray> {
        val x = leftSpace
        val y = topSpace
        for (i in 0 until width) {
            for (j in 0 until height) {
                if (fabric[x + i][y + j] == 'O' || fabric[x + i][y + j] == 'X')
                    fabric[x + i][y + j] = 'X'
                else
                    fabric[x + i][y + j] = 'O'
            }
        }
        return fabric
    }

}

fun partOne(fabric: Array<CharArray>): Int {
    var count = 0
    for (i in 0 until 1000)
        for (j in 0 until 1000)
            if (fabric[i][j] == 'X')
                count++
    return count
}

fun partTwo(fabric: Array<CharArray>, claims: List<Claim>): Int {
    claims.forEach {
        if (!it.isOverlapping(fabric))
            return it.id
    }
    return 0
}

fun main(args: Array<String>) {
    val claims = mutableListOf<Claim>()
    var fabric = drawFabric()
    input.forEach {
        val claim = Claim.fromInput(it)
        claims.add(claim)
        fabric = claim.drawClaim(fabric)
    }

    println("Part one solution ${partOne(fabric)}")
    println("Part two solution ${partTwo(fabric, claims)}")
}

