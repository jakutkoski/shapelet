package com.shapelet.ui

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp

@Composable
fun Mirror4Board(
    context: Context,
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
        else -> throw Exception("cannot get centered node offset of a node ID that does not exist")
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        Utility.drawSideLine(this, offsetOf(0), offsetOf(3), nodeLength)
        Utility.drawSideLine(this, offsetOf(4), offsetOf(7), nodeLength)
        Utility.drawActivationLines(this, activatedIds, nodeLength, ::offsetOf)
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
        Spacer(modifier = Modifier.size(85.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[0], activatedIds, onClickOf(0), node0onGloballyPositioned)
            Node(puzzle[4], activatedIds, onClickOf(4), node4onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[1], activatedIds, onClickOf(1), node1onGloballyPositioned)
            Node(puzzle[5], activatedIds, onClickOf(5), node5onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[2], activatedIds, onClickOf(2), node2onGloballyPositioned)
            Node(puzzle[6], activatedIds, onClickOf(6), node6onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(20.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(puzzle[3], activatedIds, onClickOf(3), node3onGloballyPositioned)
            Node(puzzle[7], activatedIds, onClickOf(7), node7onGloballyPositioned)
        }
        Spacer(modifier = Modifier.size(80.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
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

}