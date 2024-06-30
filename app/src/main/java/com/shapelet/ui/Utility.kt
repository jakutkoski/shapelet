package com.shapelet.ui

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.shapelet.Constants
import com.shapelet.PuzzleLetter

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
        // TODO guarantee ids has no indicators?
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

    fun checkCanActivate(puzzle: List<PuzzleLetter>, ids: List<Int>, incompatibleIds: Set<Int>, id: Int): Boolean {
        if (ids.isNotEmpty()) {
            if (ids.last() in incompatibleIds) return false
            getUsageLeft(puzzle[id], ids)?.let {
                if (it == 0) return false
            }
        }
        return true
    }

    fun getOnClick(
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

    fun centerNodeOffset(offset: Offset, nodeWidth: Float, nodeHeight: Float): Offset {
        return offset.plus(Offset(nodeWidth/2.0f, nodeHeight/2.0f))
    }

    fun calculateScore(puzzle: List<PuzzleLetter>, ids: List<Int>) {
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
        println("score = $score")
    }

    fun drawSideLine(drawScope: DrawScope, start: Offset, end: Offset) {
        drawScope.drawLine(
            start = start,
            end = end,
            color = Color.Black,
            strokeWidth = Constants.STROKE_WIDTH
        )
    }
}