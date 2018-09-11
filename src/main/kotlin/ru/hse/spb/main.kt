package ru.hse.spb

import java.util.*

data class Change(val letter: Char, val index: Int)

class Solver(k: Int, s: String, private val changes: List<Change>) {

    private var blocks: Array<Block> = Array(k) { _ -> Block(s) }

    fun solve() {
        for (change in changes) {
            applyChange(change)
        }
    }

    fun getSolution() : String {
        val sb = StringBuilder()
        for (block in blocks) {
            sb.append(block.toString())
        }
        return sb.toString()
    }

    private fun applyChange(change: Change) {
        val letterNumber = change.letter - 'a'
        val index = change.index
        var currentSkip = 0
        var currentBlock = 0
        while (currentSkip + blocks[currentBlock].getLetterCount(letterNumber) < index) {
            currentSkip += blocks[currentBlock].getLetterCount(letterNumber)
            currentBlock++
        }

        blocks[currentBlock].removeLetter(letterNumber, index - currentSkip)
    }
}

class Block(s: String) {

    private val letters : Array<Array<Boolean>> = Array(s.length) { _ -> Array(26) { _ -> false} }
    private val letterCounts : Array<Int> = Array(26) {_ -> 0}
    private val length : Int = s.length

    init {
        for ((i, c) in s.withIndex()) {
            val letterNum = c - 'a'
            letters[i][letterNum] = true
            letterCounts[letterNum]++
        }
    }

    fun getLetterCount(letter : Int) : Int {
        return letterCounts[letter]
    }

    fun removeLetter(letterNumber: Int, index: Int) {
        var currentSkip = 0
        for (i in 0 until length) {
            if (letters[i][letterNumber]) {
                if (currentSkip + 1 == index) {
                    letters[i][letterNumber] = false
                    letterCounts[letterNumber]--
                    break
                } else {
                    currentSkip++
                }
            }
        }
    }

    override fun toString(): String {
        val sb = StringBuilder()
        for (i in 0 until length) {
            for (c in 0 until  26) {
                if (letters[i][c]) {
                    sb.append((c + 'a'.toInt()).toChar())
                }
            }
        }
        return sb.toString()
    }
}

fun main(args: Array<String>) {
    with(Scanner(System.`in`)) {
        val k = nextLine().toInt()
        val s = nextLine()
        val q = nextLine().toInt()
        val changes = ArrayList<Change>()
        for (i in 0 until q) {
            val query = nextLine().split(' ')
            changes.add(Change(query[1][0], query[0].toInt()))
        }

        val solver = Solver(k, s, changes)
        solver.solve()
        print(solver.getSolution())
    }
}
