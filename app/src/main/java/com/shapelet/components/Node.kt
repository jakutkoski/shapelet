package com.shapelet.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.shapelet.model.PuzzleLetter
import com.shapelet.utility.Constants

@Composable
fun Node(
    puzzleLetter: PuzzleLetter,
    activatedIds: List<Int>,
    onClick: () -> Unit,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    val activated = puzzleLetter.id in activatedIds
    val lastActivated = activatedIds.isNotEmpty() && activatedIds.last() == puzzleLetter.id

    val border = when {
        lastActivated -> BorderStroke(3.dp, Constants.ACTIVATION_GREEN)
        activated -> BorderStroke(1.dp, Constants.ACTIVATION_GREEN)
        else -> BorderStroke(1.dp, Color.Black)
    }

    val cornerSize = if (activated) 0 else 20

    val fontWeight =
        if (puzzleLetter.letter in setOf("a", "e", "i", "o", "u"))
            FontWeight.ExtraBold
        else
            FontWeight.Normal

    OutlinedButton(
        shape = RoundedCornerShape(CornerSize(cornerSize)),
        modifier = Modifier
            .size(45.dp)
            .onGloballyPositioned(onGloballyPositioned),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        onClick = onClick,
        border = border
    ) {
        Text(text = puzzleLetter.letter.uppercase(), fontWeight = fontWeight)
    }
}