package com.shapelet

import java.io.File

// TODO
// generate 50 box3, 50 lane4, shuffle all, save to file

const val allWordsFileName = "app/src/main/assets/all_words.txt"
const val seedWordsFileName = "words/seed_words.txt"
const val offensiveWordsFileName = "words/offensive_words.txt"

fun main(args: Array<String>) {
    val words = getWords(seedWordsFileName)
    val allWords = getWords(allWordsFileName)
    val result = words.filter {
        it in allWords
    }
    write(result, "result.txt")
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
                val encodedPuzzle = "box3|${puzzle.joinToString("")}|$word1,$word2"
                result.add(encodedPuzzle)
                println(encodedPuzzle)
                break
            }
            println(++processed)
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
        var currentSide = puzzleSequence.indexOf(wordSequence[0]) / 3
        wordSequence.drop(1).forEach {
            val nextSide = puzzleSequence.indexOf(it) / 3
            if (nextSide == currentSide) return false
            currentSide = nextSide
        }
        return true
    }
}

object Lane4Generator {
    // TODO update this
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
                val encodedPuzzle = "box3|${puzzle.joinToString("")}|$word1,$word2"
                result.add(encodedPuzzle)
                println(encodedPuzzle)
                break
            }
            println(++processed)
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
        var currentSide = puzzleSequence.indexOf(wordSequence[0]) / 3
        wordSequence.drop(1).forEach {
            val nextSide = puzzleSequence.indexOf(it) / 3
            if (nextSide == currentSide) return false
            currentSide = nextSide
        }
        return true
    }
}
