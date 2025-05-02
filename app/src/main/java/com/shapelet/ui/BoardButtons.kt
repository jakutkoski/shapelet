package com.shapelet.ui

import android.content.Context
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
        OutlinedButton(
            shape = CutCornerShape(CornerSize(10)),
            modifier = Modifier.width(150.dp).height(100.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = Utility.getDeleteOnClick(activatedIds, delete)
        ) {
            Text(text = "Delete")
        }
        Spacer(modifier = Modifier.size(10.dp))
        OutlinedButton(
            shape = CutCornerShape(CornerSize(10)),
            modifier = Modifier.width(150.dp).height(100.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            onClick = Utility.getSubmitOnClick(context, puzzle, activatedIds, activate)
        ) {
            Text(text = "Submit")
        }
    }
}