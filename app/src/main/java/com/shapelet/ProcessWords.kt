package com.shapelet

import java.io.File

// TODO can this file be moved to root of repo?

fun main(args: Array<String>) {
//    clean("words/seed_words.txt", "seed_words_clean.txt")
//    clean("app/src/main/assets/all_words.txt", "all_words_clean.txt")

    generatePuzzles(getWords("words/seed_words.txt"), 0, 100)
    // shuffle before writing to text file
    // de-dupe before writing to text file
    // add ID to puzzles?
}

fun getWords(fileName: String): List<String> {
    return File(fileName).readLines().map { it.trim() }
}

fun clean(sourceFileName: String, destinationFileName: String) {
    val sourceWords = getWords(sourceFileName)
    val profaneWords = getWords("words/profane_words.txt")

    val result = sourceWords.filter {
        it.length >= 3 && it !in profaneWords
    }

    println("$sourceFileName : original word count : ${sourceWords.size}")
    println("$destinationFileName : final word count: ${result.size}")

    File(destinationFileName).printWriter().use { out ->
        result.forEach {
            out.println(it)
        }
    }
}

fun works(puzzleSequence: List<Char>, wordSequence: String): Boolean {
    // this assumes all chars in wordSequence are part of puzzleSequence
    var currentSide = puzzleSequence.indexOf(wordSequence[0]) / 3
    wordSequence.drop(1).forEach {
        val nextSide = puzzleSequence.indexOf(it) / 3
        if (nextSide == currentSide) return false
        currentSide = nextSide
    }
    return true
}

fun generatePuzzle(uniqueLetters: List<Char>, wordSequence: String): List<Char> {
    var attempts = 0
    while (attempts < 500) {
        val shuffled = uniqueLetters.shuffled()
        if (works(shuffled, wordSequence)) return shuffled
        attempts++
    }
    return emptyList()
}

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