package com.shapelet.ui

import android.content.Context
import android.widget.Toast
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

    fun getNodeOnClick(
        puzzle: List<PuzzleLetter>,
        id: Int,
        ids: List<Int>,
        activate: (List<Int>) -> List<Int>
    ): () -> Unit {
        return onClick@{
            if (ids.isNotEmpty()) {
                if (checkCompleted(ids)) return@onClick
                if (ids.last() in puzzle[id].incompatibleIds) return@onClick
            }
            activate(listOf(id))
        }
    }

    fun getDeleteOnClick(
        ids: List<Int>,
        delete: (Int) -> List<Int>
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
        context: Context,
        puzzle: List<PuzzleLetter>,
        ids: List<Int>,
        activate: (List<Int>) -> List<Int>
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
            if (getSpelledWord(puzzle, mostRecentIdSequence) !in Words.allWords) {
                Toast.makeText(context, "Not a word.", Toast.LENGTH_SHORT).show()
                return@onClick
            }
            if (checkCanComplete(puzzle, ids)) {
                val updatedIds = activate(listOf(Constants.COMPLETE))
                val score = calculateScore(updatedIds)
                Toast.makeText(context, "You did it! Score: $score", Toast.LENGTH_SHORT).show()
            } else {
                activate(listOf(Constants.SUBMIT, lastId))
            }
        }
    }

    fun centerNodeOffset(offset: Offset, nodeLength: Float): Offset {
        return offset.plus(Offset(nodeLength/2.0f, nodeLength/2.0f))
    }

    fun calculateScore(
        ids: List<Int>
    ): Int {
        val amountOfWords = ids.count { it in Constants.INDICATORS }
        return when (amountOfWords) {
            1 -> 100
            2 -> 80
            3 -> 40
            4 -> 20
            5 -> 10
            6 -> 5
            7 -> 4
            8 -> 3
            9 -> 2
            10 -> 1
            else -> 0
        }
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

    fun decode(boardEncoding: String): Board {
        val parts = boardEncoding.split("|")
        val type = parts[0]
        val letters = parts[1].map { it.toString() }
        val specialWords = parts[2].split(",")
        val puzzleLetters = letters.mapIndexed { index, letter ->
            PuzzleLetter(
                id = index,
                letter = letter,
                incompatibleIds = getIncompatibleIds(type, index)
            )
        }
        return Board(
            type = type,
            puzzle = puzzleLetters,
            specialWords = specialWords
        )
    }

    fun getIncompatibleIds(boardType: String, id: Int): Set<Int> {
        return when (boardType) {
            "box3" -> when (id) {
                0,1,2 -> setOf(0,1,2)
                3,4,5 -> setOf(3,4,5)
                6,7,8 -> setOf(6,7,8)
                9,10,11 -> setOf(9,10,11)
                else -> throw InvalidIdException(id, boardType)
            }
            "lane4" -> when (id) {
                0,1,2,3,8,9,10,11 -> setOf(0,1,2,3,8,9,10,11)
                4,5,6,7 -> setOf(4,5,6,7)
                else -> throw InvalidIdException(id, boardType)
            }
            else -> throw BoardTypeException(boardType)
        }
    }
}

object Words {
    private var initialized = false
    lateinit var allWords: List<String> private set

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true
        val processed = mutableListOf<String>()
        val inputStream = context.assets.open("all_words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        while (line != null) {
            processed.add(line)
            line = reader.readLine()
        }
        allWords = processed.toList()
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
    val incompatibleIds: Set<Int>
)

data class Board(
    val type: String,
    val puzzle: List<PuzzleLetter>,
    val specialWords: List<String>
)

class GetOffsetException(id: Int): Exception("cannot get offset of node $id since it does not exist")

class BoardTypeException(type: String): Exception("board type $type does not exist")

class InvalidIdException(id: Int, boardType: String): Exception("id $id is not valid for board type $boardType")