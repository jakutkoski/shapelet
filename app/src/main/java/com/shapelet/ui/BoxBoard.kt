package com.shapelet.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp

@Composable
fun BoxBoard(
    puzzle: List<PuzzleLetter>
) {
    var activatedIds by rememberSaveable { mutableStateOf(listOf<Int>()) }
    val activate: (List<Int>) -> List<Int> = { ids ->
        activatedIds = activatedIds.toMutableList().apply { addAll(ids) }.toList()
        activatedIds
    }
    val delete: (Int) -> List<Int> = { amountToDrop ->
        activatedIds = activatedIds.dropLast(amountToDrop)
        activatedIds
    }

    var nodeLength by remember { mutableFloatStateOf(0.0f) }

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
        nodeLength = it.boundsInRoot().width // width and height of square should be the same
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

    fun onClickOf(id: Int) = Utility.getNodeOnClick(puzzle, id, activatedIds, activate)

    fun offsetOf(id: Int) = when(id) {
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
        else -> throw GetOffsetException(id)
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        Utility.drawSideLine(this, offsetOf(0), offsetOf(2), nodeLength)
        Utility.drawSideLine(this, offsetOf(3), offsetOf(5), nodeLength)
        Utility.drawSideLine(this, offsetOf(6), offsetOf(8), nodeLength)
        Utility.drawSideLine(this, offsetOf(9), offsetOf(11), nodeLength)
        Utility.drawActivationLines(this, activatedIds, nodeLength, ::offsetOf)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        SubmittedLetters(puzzle, activatedIds)

        Column(modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
        ) {
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
        }

        BoardButtons(puzzle, activatedIds, activate, delete)
    }
}
