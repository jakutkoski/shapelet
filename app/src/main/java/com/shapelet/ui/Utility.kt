package com.shapelet.ui

import android.content.Context
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import java.io.BufferedReader
import java.io.InputStreamReader

object Utility {
    fun getSpelledActivated(puzzle: List<PuzzleLetter>, ids: List<Int>): String {
        return ids.joinToString("") {
            when (it) {
                Constants.SUBMIT -> " - "
                Constants.COMPLETE -> ""
                else -> puzzle[it].letter
            }
        }
    }

    fun getSpelledWord(puzzle: List<PuzzleLetter>, ids: List<Int>): String {
        // if ids has any of Constants.INDICATORS, this will throw an error
        return ids.joinToString("") {
            puzzle[it].letter
        }
    }

    fun checkCanComplete(puzzle: List<PuzzleLetter>, ids: List<Int>): Boolean {
        val fullSet = puzzle.map { it.id }.toSet()
        return ids.containsAll(fullSet)
    }

    fun checkCompleted(ids: List<Int>): Boolean {
        return ids.lastIndexOf(Constants.COMPLETE) != -1
    }

    fun getUsageLeft(puzzleLetter: PuzzleLetter, ids: List<Int>): Int? {
        return puzzleLetter.usageLimit?.let { usageLimit ->
            val usageCount = ids.count {
                it == puzzleLetter.id
            }
            val result = usageLimit - usageCount
            if (result < 0) throw Exception("usageLimit was somehow exceeded")
            result
        }
    }

    fun getNodeOnClick(
        puzzle: List<PuzzleLetter>,
        id: Int,
        ids: List<Int>,
        activate: (List<Int>) -> Unit
    ): () -> Unit {
        return onClick@{
            if (ids.isNotEmpty()) {
                if (ids.last() in puzzle[id].incompatibleIds) return@onClick
                getUsageLeft(puzzle[id], ids)?.let {
                    if (it == 0) return@onClick
                }
            }
            activate(listOf(id))
        }
    }

    fun getDeleteOnClick(
        ids: List<Int>,
        delete: (Int) -> Unit
    ): () -> Unit {
        return onClick@{
            if (ids.isEmpty()) return@onClick
            if (ids.size <= 3) {
                delete(1)
                return@onClick
            }
            if (ids.lastIndexOf(Constants.SUBMIT) == ids.size - 2) {
                delete(2)
                return@onClick
            }
            delete(1)
        }
    }

    fun getSubmitOnClick(
        puzzle: List<PuzzleLetter>,
        comboPairs: List<Pair<Int, Int>>,
        avoidPairs: List<Pair<Int, Int>>,
        ids: List<Int>,
        onInvalidWord: () -> Unit,
        onComplete: () -> Unit,
        activate: (List<Int>) -> Unit
    ): () -> Unit {
        return onClick@{
            if (ids.isEmpty()) return@onClick
            if (checkCompleted(ids)) return@onClick
            val lastId = ids.last()
            val startOfMostRecentIdSequence = ids
                .lastIndexOf(Constants.SUBMIT)
                .let {
                    if (it == -1) 0 else it
                }
            val mostRecentIdSequence = ids
                .subList(startOfMostRecentIdSequence, ids.size)
                .dropWhile { it in Constants.INDICATORS }
            if (mostRecentIdSequence.size < 3) return@onClick
            if (getSpelledWord(puzzle, mostRecentIdSequence) !in Dictionary.words()) {
                onInvalidWord()
                return@onClick
            }
            if (checkCanComplete(puzzle, ids)) {
                activate(listOf(Constants.COMPLETE))
                calculateScore(puzzle, comboPairs, avoidPairs, ids)
                onComplete()
            } else {
                activate(listOf(Constants.SUBMIT, lastId))
            }
        }
    }

    fun centerNodeOffset(offset: Offset, nodeLength: Float): Offset {
        return offset.plus(Offset(nodeLength/2.0f, nodeLength/2.0f))
    }

    fun calculateScore(
        puzzle: List<PuzzleLetter>,
        comboPairs: List<Pair<Int, Int>>,
        avoidPairs: List<Pair<Int, Int>>,
        ids: List<Int>
    ) {
        val amountOfWords = ids.count {
            it in Constants.INDICATORS
        }
        var score = when (amountOfWords) {
            1 -> 100
            2 -> 80
            3 -> 40
            4 -> 20
            5 -> 10
            6 -> 5
            7 -> 1
            else -> {
                println("score = 0")
                return
            }
        }
        val amountOfUsageBonus = ids.count {
            if (it !in Constants.INDICATORS) {
                puzzle[it].usageBonus
            } else false
        }
        println("amountOfUsageBonus = $amountOfUsageBonus")
        score += 5 * amountOfUsageBonus
        // TODO logic for comboPairs and avoidPairs
        println("score = $score")
    }

    fun drawSideLine(drawScope: DrawScope, start: Offset, end: Offset, nodeLength: Float) {
        drawScope.drawLine(
            start = centerNodeOffset(start, nodeLength),
            end = centerNodeOffset(end, nodeLength),
            color = Color.Black,
            strokeWidth = Constants.STROKE_WIDTH
        )
    }

    fun drawActivationLines(
        drawScope: DrawScope,
        ids: List<Int>,
        nodeLength: Float,
        offsetOf: (Int) -> Offset
    ) {
        if (ids.size >= 2) {
            val lastSubmitIndex = ids.lastIndexOf(Constants.SUBMIT)
            var i = 0
            while (i + 1 < ids.size) {
                val firstId = ids[i]
                val secondId = ids[i + 1]
                if (firstId !in Constants.INDICATORS && secondId !in Constants.INDICATORS) {
                    val completed = checkCompleted(ids)
                    drawScope.drawLine(
                        start = centerNodeOffset(offsetOf(firstId), nodeLength),
                        end = centerNodeOffset(offsetOf(secondId), nodeLength),
                        color = if (completed || i + 1 < lastSubmitIndex)
                            Constants.ACTIVATION_GREEN_TRANSPARENT
                        else
                            Color.Black,
                        strokeWidth = Constants.STROKE_WIDTH,
                        pathEffect = if (completed || i + 1 < lastSubmitIndex)
                            null
                        else
                            Constants.DOTTED_PATH_EFFECT
                    )
                }
                i++
            }
        }
    }

    fun drawComboLines(
        drawScope: DrawScope,
        comboPairs: List<Pair<Int, Int>>,
        nodeLength: Float,
        offsetOf: (Int) -> Offset
    ) {
        comboPairs.forEach { combo ->
            drawScope.drawLine(
                start = centerNodeOffset(offsetOf(combo.first), nodeLength),
                end = centerNodeOffset(offsetOf(combo.second), nodeLength),
                color = Color(0x220000FF),
                strokeWidth = 15.0f
            )
        }
    }

    fun drawAvoidLines(
        drawScope: DrawScope,
        avoidPairs: List<Pair<Int, Int>>,
        nodeLength: Float,
        offsetOf: (Int) -> Offset
    ) {
        avoidPairs.forEach { combo ->
            drawScope.drawLine(
                start = centerNodeOffset(offsetOf(combo.first), nodeLength),
                end = centerNodeOffset(offsetOf(combo.second), nodeLength),
                color = Color(0x22FF0000),
                strokeWidth = 15.0f
            )
        }
    }
}

object Dictionary {
    private val _words = mutableListOf<String>()
    private var initialized = false

    fun words() = _words.toList()

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true
        val inputStream = context.assets.open("words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        while (line != null) {
            _words.add(line)
            line = reader.readLine()
        }
    }
}

object Constants {
    const val SUBMIT = -9001
    const val COMPLETE = -9002
    const val STROKE_WIDTH = 4.0f
    val INDICATORS = listOf(SUBMIT, COMPLETE)
    val DOTTED_PATH_EFFECT = PathEffect.dashPathEffect(listOf(15.0f, 15.0f).toFloatArray(), 15.0f)
    val ACTIVATION_GREEN = Color(0xFF0AC27B)
    val ACTIVATION_GREEN_TRANSPARENT = Color(0x440AC27B)
}

data class PuzzleLetter(
    val id: Int,
    val letter: String,
    val incompatibleIds: Set<Int>,
    val usageBonus: Boolean = false,
    val usageLimit: Int? = null
)