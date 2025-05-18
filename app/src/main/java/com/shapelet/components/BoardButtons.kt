package com.shapelet.components

import android.app.Activity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shapelet.model.Board
import com.shapelet.model.PuzzleLetter
import com.shapelet.utility.Constants
import com.shapelet.utility.MessageHandler
import com.shapelet.utility.SharedPrefs
import com.shapelet.utility.Solutions
import com.shapelet.utility.Utility.asSolution
import com.shapelet.utility.Utility.checkCompleted
import com.shapelet.utility.Words

@Composable
fun BoardButtons(
    activity: Activity,
    board: Board,
    activatedIds: List<Int>,
    activate: (List<Int>) -> List<Int>,
    delete: (Int) -> List<Int>
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 20.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            shape = CutCornerShape(CornerSize(8)),
            modifier = Modifier.width(150.dp).height(80.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = onClick@{
                if (activatedIds.isEmpty()) return@onClick
                if (activatedIds.size <= 3) {
                    delete(1)
                    return@onClick
                }
                if (activatedIds.lastIndexOf(Constants.SUBMIT) == activatedIds.size - 2) {
                    delete(2)
                    return@onClick
                }
                delete(1)
            }
        ) {
            Text(text = "Delete")
        }
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedButton(
            shape = CutCornerShape(CornerSize(8)),
            modifier = Modifier.width(150.dp).height(80.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            enabled = checkSubmitEnabled(activatedIds),
            onClick = onClick@{
                if (activatedIds.isEmpty()) return@onClick
                if (checkCompleted(activatedIds)) return@onClick
                val lastId = activatedIds.last()
                val startOfMostRecentIdSequence = activatedIds
                    .lastIndexOf(Constants.SUBMIT)
                    .let {
                        if (it == -1) 0 else it
                    }
                val mostRecentIdSequence = activatedIds
                    .subList(startOfMostRecentIdSequence, activatedIds.size)
                    .dropWhile { it in Constants.INDICATORS }
                if (mostRecentIdSequence.size < 3) return@onClick
                if (getSpelledWord(board.puzzle, mostRecentIdSequence) !in Words.allWords) {
                    MessageHandler.show(false, "Invalid Word")
                    return@onClick
                }
                if (checkCanComplete(board.puzzle, activatedIds)) {
                    if (asSolution(board.puzzle, activatedIds) in Solutions.get()) {
                        MessageHandler.show(false, "Already solved this way")
                    } else {
                        Solutions.update(asSolution(board.puzzle, activatedIds))
                        SharedPrefs.persist(activity, board.encoded, Solutions.get())
                        activate(listOf(Constants.COMPLETE))
                        MessageHandler.show(false, "Solved!")
                    }
                } else {
                    activate(listOf(Constants.SUBMIT, lastId))
                }
            }
        ) {
            Text(text = "Submit")
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

fun checkSubmitEnabled(ids: List<Int>): Boolean {
    return !checkCompleted(ids) && ids.count { it in Constants.INDICATORS } < 5
}