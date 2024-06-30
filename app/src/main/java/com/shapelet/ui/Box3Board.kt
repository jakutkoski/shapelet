package com.shapelet.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.shapelet.Constants
import com.shapelet.Dictionary
import com.shapelet.PuzzleLetter

@Composable
fun Box3Board(
    puzzle: List<PuzzleLetter>,
    onComplete: () -> Unit,
    onInvalidWord: () -> Unit
) {
    var activatedIds by rememberSaveable { mutableStateOf(listOf<Int>()) }
    val activate: (List<Int>) -> Unit = { ids ->
        activatedIds = activatedIds.toMutableList().apply { addAll(ids) }.toList()
    }
    val delete: (Int) -> Unit = { amountToDrop ->
        activatedIds = activatedIds.dropLast(amountToDrop)
    }

    var nodeWidth by remember { mutableFloatStateOf(0.0f) }
    var nodeHeight by remember { mutableFloatStateOf(0.0f) }

    var node0offset by remember { mutableStateOf(Offset.Zero) }
    var node1offset by remember { mutableStateOf(Offset.Zero) }
    var node2offset by remember { mutableStateOf(Offset.Zero) }
    var node3offset by remember { mutableStateOf(Offset.Zero) }
    var node4offset by remember { mutableStateOf(Offset.Zero) }
    var node5offset by remember { mutableStateOf(Offset.Zero) }
    var node6offset by remember { mutableStateOf(Offset.Zero) }
    var node7offset by remember { mutableStateOf(Offset.Zero) }
    var node8offset by remember { mutableStateOf(Offset.Zero) }
    var node9offset by remember { mutableStateOf(Offset.Zero) }
    var node10offset by remember { mutableStateOf(Offset.Zero) }
    var node11offset by remember { mutableStateOf(Offset.Zero) }

    val node0onGloballyPositioned: (LayoutCoordinates) -> Unit = {
        node0offset = it.positionInRoot()
        nodeWidth = it.boundsInRoot().width
        nodeHeight = it.boundsInRoot().height
    }
    val node1onGloballyPositioned: (LayoutCoordinates) -> Unit = { node1offset = it.positionInRoot() }
    val node2onGloballyPositioned: (LayoutCoordinates) -> Unit = { node2offset = it.positionInRoot() }
    val node3onGloballyPositioned: (LayoutCoordinates) -> Unit = { node3offset = it.positionInRoot() }
    val node4onGloballyPositioned: (LayoutCoordinates) -> Unit = { node4offset = it.positionInRoot() }
    val node5onGloballyPositioned: (LayoutCoordinates) -> Unit = { node5offset = it.positionInRoot() }
    val node6onGloballyPositioned: (LayoutCoordinates) -> Unit = { node6offset = it.positionInRoot() }
    val node7onGloballyPositioned: (LayoutCoordinates) -> Unit = { node7offset = it.positionInRoot() }
    val node8onGloballyPositioned: (LayoutCoordinates) -> Unit = { node8offset = it.positionInRoot() }
    val node9onGloballyPositioned: (LayoutCoordinates) -> Unit = { node9offset = it.positionInRoot() }
    val node10onGloballyPositioned: (LayoutCoordinates) -> Unit = { node10offset = it.positionInRoot() }
    val node11onGloballyPositioned: (LayoutCoordinates) -> Unit = { node11offset = it.positionInRoot() }

    val completed = Utility.checkCompleted(activatedIds)

    fun onClickOf(id: Int) = Utility.getOnClick(puzzle, id, activatedIds, activate)

    fun getCenteredNodeOffset(id: Int): Offset {
        return Utility.centerNodeOffset(
            offset = when(id) {
                0 -> node0offset
                1 -> node1offset
                2 -> node2offset
                3 -> node3offset
                4 -> node4offset
                5 -> node5offset
                6 -> node6offset
                7 -> node7offset
                8 -> node8offset
                9 -> node9offset
                10 -> node10offset
                11 -> node11offset
                else -> throw Exception("cannot get centered node offset of a node ID that does not exist")
            },
            nodeWidth = nodeWidth,
            nodeHeight = nodeHeight
        )
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        Utility.drawSideLine(this, getCenteredNodeOffset(0), getCenteredNodeOffset(2))
        Utility.drawSideLine(this, getCenteredNodeOffset(3), getCenteredNodeOffset(5))
        Utility.drawSideLine(this, getCenteredNodeOffset(6), getCenteredNodeOffset(8))
        Utility.drawSideLine(this, getCenteredNodeOffset(9), getCenteredNodeOffset(11))

        // TODO this can probably be put in some Utility.drawLines function
        if (activatedIds.size >= 2) {
            val lastSubmitIndex = activatedIds.lastIndexOf(Constants.SUBMIT)
            var i = 0
            while (i + 1 < activatedIds.size) {
                val firstId = activatedIds[i]
                val secondId = activatedIds[i + 1]
                if (firstId !in Constants.INDICATORS && secondId !in Constants.INDICATORS) {
                    drawLine(
                        start = getCenteredNodeOffset(firstId),
                        end = getCenteredNodeOffset(secondId),
                        color = if (completed || i + 1 < lastSubmitIndex) Constants.ACTIVATION_GREEN_TRANSPARENT else Color.Black,
                        strokeWidth = 4.0f,
                        pathEffect = if (completed || i + 1 < lastSubmitIndex) null else Constants.DOTTED_PATH_EFFECT
                    )
                }
                i++
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(220.dp)
            .padding(start = 10.dp, top = 100.dp, end = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = Utility.getSpelledActivated(puzzle, activatedIds), modifier = Modifier.fillMaxHeight())
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(puzzle[0], activatedIds, onClickOf(0), node0onGloballyPositioned)
            Node(puzzle[1], activatedIds, onClickOf(1), node1onGloballyPositioned)
            Node(puzzle[2], activatedIds, onClickOf(2), node2onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[3], activatedIds, onClickOf(3), node3onGloballyPositioned)
            Node(puzzle[6], activatedIds, onClickOf(6), node6onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[4], activatedIds, onClickOf(4), node4onGloballyPositioned)
            Node(puzzle[7], activatedIds, onClickOf(7), node7onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[5], activatedIds, onClickOf(5), node5onGloballyPositioned)
            Node(puzzle[8], activatedIds, onClickOf(8), node8onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(puzzle[9], activatedIds, onClickOf(9), node9onGloballyPositioned)
            Node(puzzle[10], activatedIds, onClickOf(10), node10onGloballyPositioned)
            Node(puzzle[11], activatedIds, onClickOf(11), node11onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(80.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onClick@{
                // TODO put in Utility function
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
            }) {
                Text(text = "DELETE")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = onClick@{
                // TODO put in Utility function
                if (activatedIds.isEmpty()) return@onClick
                if (completed) return@onClick
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
                if (Utility.getSpelledWord(puzzle, mostRecentIdSequence) !in Dictionary.words()) {
                    onInvalidWord()
                    return@onClick
                }
                if (Utility.checkCanComplete(puzzle, activatedIds)) {
                    activate(listOf(Constants.COMPLETE))
                    Utility.calculateScore(puzzle, activatedIds)
                    onComplete()
                } else {
                    activate(listOf(Constants.SUBMIT, lastId))
                }
            }) {
                Text(text = "SUBMIT")
            }
        }
    }

}

@Composable
private fun Node(
    puzzleLetter: PuzzleLetter,
    activatedIds: List<Int>,
    onClick: () -> Unit,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    val activated = puzzleLetter.id in activatedIds
    val lastActivated = activatedIds.isNotEmpty() && activatedIds.last() == puzzleLetter.id

    val text = Utility.getUsageLeft(puzzleLetter, activatedIds)?.let {
        "${puzzleLetter.letter} $it"
    } ?: puzzleLetter.letter

    val border = when {
        lastActivated -> BorderStroke(3.dp, Constants.ACTIVATION_GREEN)
        activated -> BorderStroke(1.dp, Constants.ACTIVATION_GREEN)
        else -> BorderStroke(1.dp, Color.Black)
    }

    OutlinedButton(
        shape = RoundedCornerShape(CornerSize(0)),
        modifier = Modifier
            .size(45.dp)
            .onGloballyPositioned(onGloballyPositioned),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = if (puzzleLetter.usageBonus) Color.Blue else Color.Black
        ),
        onClick = onClick,
        border = border
    ) {
        Text(text = text)
    }
}
