package com.shapelet

import java.io.File

const val allWordsFileName = "app/src/main/assets/all_words.txt"
const val seedWordsFileName = "words/seed_words.txt"
const val offensiveWordsFileName = "words/offensive_words.txt"

fun main(args: Array<String>) {
    val seedWords = getWords(seedWordsFileName)
    val result = mutableListOf<String>()
    result += BoxPuzzleGenerator().generatePuzzles(seedWords.shuffled(), 200)
    result += CupPuzzleGenerator().generatePuzzles(seedWords.shuffled(), 200)
    result += MirrorPuzzleGenerator().generatePuzzles(seedWords.shuffled(), 200)
    write(result.shuffled(), "result.txt")
}

fun getWords(fileName: String): List<String> {
    return File(fileName).readLines().map { it.trim() }
}

fun write(words: List<String>, destinationFileName: String) {
    File(destinationFileName).printWriter().use { out ->
        words.forEach {
            out.println(it)
        }
    }
}

abstract class PuzzleGenerator(private val type: String) {
    abstract fun getSideOf(index: Int): Int

    fun generatePuzzles(seedWords: List<String>, maxAmount: Int = seedWords.size): List<String> {
        val result = mutableListOf<String>()
        println()
        for ((amountAttempted, word1) in seedWords.withIndex()) {
            for (i in seedWords.indices) {
                val word2 = seedWords.random()
                if (word1.last() != word2.first()) continue
                val wordSequence = word1.dropLast(1) + word2
                val uniqueLetters = wordSequence.groupBy { it }.keys.toList()
                if (uniqueLetters.size != 12) continue
                val puzzle = generatePuzzle(uniqueLetters, wordSequence)
                if (puzzle.size != 12) continue
                val encodedPuzzle = "$type|${puzzle.joinToString("")}|$word1,$word2"
                result.add(encodedPuzzle)
                break
            }
            print("\rgenerating $type puzzles: ${amountAttempted+1}/${seedWords.size} words attempted, ${result.size}/$maxAmount puzzles generated")
            if (result.size >= maxAmount) break
        }
        println()
        return result
    }

    private fun generatePuzzle(uniqueLetters: List<Char>, wordSequence: String): List<Char> {
        var attempts = 0
        while (attempts < 500) {
            val shuffled = uniqueLetters.shuffled()
            if (works(shuffled, wordSequence)) return shuffled
            attempts++
        }
        return emptyList()
    }

    private fun works(puzzleSequence: List<Char>, wordSequence: String): Boolean {
        // this assumes all chars in wordSequence are part of puzzleSequence
        var currentSide = getSideOf(puzzleSequence.indexOf(wordSequence[0]))
        wordSequence.drop(1).forEach {
            val nextSide = getSideOf(puzzleSequence.indexOf(it))
            if (nextSide == currentSide) return false
            currentSide = nextSide
        }
        return true
    }
}

class BoxPuzzleGenerator: PuzzleGenerator("box") {
    override fun getSideOf(index: Int): Int {
        return when (index) {
            0,1,2 -> 0
            3,4,5 -> 1
            6,7,8 -> 2
            9,10,11 -> 3
            else -> throw Exception("${this.javaClass.simpleName} has a bug")
        }
    }
}

class CupPuzzleGenerator: PuzzleGenerator("cup") {
    override fun getSideOf(index: Int): Int {
        return when (index) {
            0,1,2,3 -> 0
            4,5,6,7 -> 1
            8,9,10,11 -> 2
            else -> throw Exception("${this.javaClass.simpleName} has a bug")
        }
    }
}

class MirrorPuzzleGenerator: PuzzleGenerator("mirror") {
    override fun getSideOf(index: Int): Int {
        return when (index) {
            0,1,2,3,4,5 -> 0
            6,7,8,9,10,11 -> 1
            else -> throw Exception("${this.javaClass.simpleName} has a bug")
        }
    }
}