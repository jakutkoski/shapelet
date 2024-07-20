package com.shapelet.ui

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BoardButtons(
    context: Context,
    puzzle: List<PuzzleLetter>,
    activatedIds: List<Int>,
    activate: (List<Int>) -> List<Int>,
    delete: (Int) -> List<Int>
) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(top = 40.dp),
        horizontalArrangement = Arrangement.Center
    ) {
        Button(onClick = Utility.getDeleteOnClick(activatedIds, delete)) {
            Text(text = "DELETE")
        }
        Spacer(modifier = Modifier.size(30.dp))
        Button(onClick = Utility.getSubmitOnClick(context, puzzle, activatedIds, activate)) {
            Text(text = "SUBMIT")
        }
    }
}