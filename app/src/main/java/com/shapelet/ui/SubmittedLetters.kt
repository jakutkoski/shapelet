package com.shapelet.ui

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
import com.shapelet.ui.Utility.asSolution

@Composable
fun SubmittedLetters(
    puzzle: List<PuzzleLetter>,
    activatedIds: List<Int>
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .fillMaxHeight(0.25f)
        .padding(start = 10.dp, top = 120.dp, end = 10.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = Utility.getSpelledActivated(puzzle, activatedIds),
            modifier = Modifier.fillMaxHeight(),
            maxLines = 2,
            fontStyle = if (Solutions.get().any {
                it.startsWith(asSolution(puzzle, activatedIds))
            }) FontStyle.Italic else FontStyle.Normal
        )
    }
}