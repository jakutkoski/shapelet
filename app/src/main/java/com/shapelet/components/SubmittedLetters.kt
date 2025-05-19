package com.shapelet.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import com.shapelet.model.Board
import com.shapelet.model.PuzzleLetter
import com.shapelet.utility.Constants
import com.shapelet.utility.Solutions
import com.shapelet.utility.Utility.asSolution

@Composable
fun SubmittedLetters(
    board: Board,
    activatedIds: List<Int>
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.15f)
        .padding(start = 10.dp, top = 25.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = getSpelledActivated(board.puzzle, activatedIds),
            modifier = Modifier.fillMaxHeight(),
            maxLines = 2,
            fontStyle = getFontStyle(board.puzzle, activatedIds)
        )
    }
}

fun getSpelledActivated(puzzle: List<PuzzleLetter>, ids: List<Int>): String {
    return ids.joinToString("") {
        when (it) {
            Constants.SUBMIT -> " - "
            Constants.COMPLETE -> ""
            else -> puzzle[it].letter.uppercase()
        }
    }.let {
        when {
            it.isBlank() || ids.contains(Constants.COMPLETE) -> it
            else -> it + "_"
        }
    }
}

fun getFontStyle(puzzle: List<PuzzleLetter>, ids: List<Int>): FontStyle {
    val alreadySubmitted = Solutions.get().any {
        it.startsWith(asSolution(puzzle, ids))
    }
    return when {
        alreadySubmitted -> FontStyle.Italic
        else -> FontStyle.Normal
    }
}