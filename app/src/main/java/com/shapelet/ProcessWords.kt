package com.shapelet

import java.io.File

const val allWordsFileName = "app/src/main/assets/all_words.txt"
const val seedWordsFileName = "words/seed_words.txt"
const val offensiveWordsFileName = "words/offensive_words.txt"

fun main(args: Array<String>) {
    val seedWords = getWords(seedWordsFileName)
    val box3results = Box3Generator.generatePuzzles(seedWords.shuffled())
//    val lane4results = Lane4Generator.generatePuzzles(seedWords.shuffled())
//    val result = (box3results + lane4results).shuffled()
    write(box3results, "result.txt")
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

object Box3Generator {
    fun generatePuzzles(seedWords: List<String>, maxAmount: Int = seedWords.size): List<String> {
        val result = mutableListOf<String>()
        for ((amountAttempted, word1) in seedWords.withIndex()) {
            for (i in seedWords.indices) {
                val word2 = seedWords.random()
                if (word1.last() != word2.first()) continue
                val wordSequence = word1.dropLast(1) + word2
                val uniqueLetters = wordSequence.groupBy { it }.keys.toList()
                if (uniqueLetters.size != 12) continue
                val puzzle = generatePuzzle(uniqueLetters, wordSequence)
                if (puzzle.size != 12) continue
                val encodedPuzzle = "box3|${puzzle.joinToString("")}|$word1,$word2"
                result.add(encodedPuzzle)
                break
            }
            print("\rGenerating Puzzles: ${amountAttempted+1}/${seedWords.size} words attempted, ${result.size}/$maxAmount puzzles generated.")
            if (result.size >= maxAmount) break
        }
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

    private fun getSideOf(index: Int): Int {
        return when (index) {
            0,1,2 -> 0
            3,4,5 -> 1
            6,7,8 -> 2
            9,10,11 -> 3
            else -> throw Exception("Box3Generator has a bug")
        }
    }
}

object Lane4Generator {
    fun generatePuzzles(seedWords: List<String>, start: Int, end: Int): List<String> {
        val result = mutableListOf<String>()
        val word1options = seedWords.subList(start, end)
        var processed = 0 // this is temporary, for watching progress
        for (word1 in word1options) {
            for (i in seedWords.indices) {
                val word2 = seedWords.random()
                if (word1.last() != word2.first()) continue
                val wordSequence = word1.dropLast(1) + word2
                val uniqueLetters = wordSequence.groupBy { it }.keys.toList()
                if (uniqueLetters.size != 12) continue
                val puzzle = generatePuzzle(uniqueLetters, wordSequence)
                if (puzzle.size != 12) continue
                val encodedPuzzle = "lane4|${puzzle.joinToString("")}|$word1,$word2"
                result.add(encodedPuzzle)
                println(encodedPuzzle)
                break
            }
            println(++processed)
            if (result.size >= 50) break
        }
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

    private fun getSideOf(index: Int): Int {
        return when (index) {
            0,1,2,3,8,9,10,11 -> 0
            4,5,6,7 -> 1
            else -> throw Exception("Lane4Generator has a bug")
        }
    }
}
