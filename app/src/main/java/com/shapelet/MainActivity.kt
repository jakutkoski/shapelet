package com.shapelet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInRoot
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.unit.dp
import com.shapelet.ui.theme.ShapeletTheme

// TODO figure out ViewModel
// TODO create other types of boards
// TODO check words against dictionary
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapeletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    println(innerPadding) // TODO wtf do with this?
                    Board("BSAGKOZRUEIT") {
                        Toast.makeText(this@MainActivity, "You did it!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}

@Composable
private fun Board(letterString: String, onComplete: () -> Unit) {
    var activatedOrder by rememberSaveable { mutableStateOf(listOf<Int>()) }
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
    var nodeWidth by remember { mutableFloatStateOf(0.0f) }
    var nodeHeight by remember { mutableFloatStateOf(0.0f) }

    val letters = letterString.toList().map { it.toString() }
    val indicators = listOf(Constants.SUBMIT, Constants.COMPLETE)

    fun checkComplete(): Boolean {
        return activatedOrder.containsAll(setOf(0,1,2,3,4,5,6,7,8,9,10,11))
    }

    fun getOnClick(nodeNumber: Int, nodeSide: Set<Int>): () -> Unit {
        return {
            if (activatedOrder.isEmpty() || activatedOrder.last() !in nodeSide) {
                activatedOrder = activatedOrder.toMutableList().apply { add(nodeNumber) }.toList()
            }
        }
    }

    fun getCenteredNodeOffset(nodeNumber: Int): Offset {
        val originalOffset = when(nodeNumber) {
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
            else -> Offset.Zero
        }
        return originalOffset.plus(Offset(nodeWidth/2.0f, nodeHeight/2.0f))
    }

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            start = getCenteredNodeOffset(0),
            end = getCenteredNodeOffset(2),
            color = Color.Black,
            strokeWidth = 4.0f
        )

        drawLine(
            start = getCenteredNodeOffset(3),
            end = getCenteredNodeOffset(5),
            color = Color.Black,
            strokeWidth = 4.0f
        )

        drawLine(
            start = getCenteredNodeOffset(6),
            end = getCenteredNodeOffset(8),
            color = Color.Black,
            strokeWidth = 4.0f
        )

        drawLine(
            start = getCenteredNodeOffset(9),
            end = getCenteredNodeOffset(11),
            color = Color.Black,
            strokeWidth = 4.0f
        )

        if (activatedOrder.size >= 2) {
            val lastSubmit = activatedOrder.lastIndexOf(Constants.SUBMIT)
            val completed = activatedOrder.lastIndexOf(Constants.COMPLETE) != -1
            var i = 0
            while (i + 1 < activatedOrder.size) {
                val firstNode = activatedOrder[i]
                val secondNode = activatedOrder[i + 1]
                if (firstNode !in indicators && secondNode !in indicators) {
                    drawLine(
                        start = getCenteredNodeOffset(firstNode),
                        end = getCenteredNodeOffset(secondNode),
                        color = if (completed || i + 1 < lastSubmit) Constants.ACTIVATION_GREEN_TRANSPARENT else Color.Black,
                        strokeWidth = 4.0f,
                        pathEffect = if (completed || i + 1 < lastSubmit) null else Constants.DOTTED_PATH_EFFECT
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
            Text(text = activatedOrder.joinToString("") {
                when (it) {
                    Constants.SUBMIT -> " - "
                    Constants.COMPLETE -> ""
                    else -> letters[it]
                }
            }, modifier = Modifier.fillMaxHeight())
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(0, letters[0], activatedOrder, getOnClick(0, setOf(0,1,2))) {
                node0offset = it.positionInRoot()
                nodeWidth = it.boundsInRoot().width
                nodeHeight = it.boundsInRoot().height
            }
            Node(1, letters[1], activatedOrder, getOnClick(1, setOf(0,1,2))) {
                node1offset = it.positionInRoot()
            }
            Node(2, letters[2], activatedOrder, getOnClick(2, setOf(0,1,2))) {
                node2offset = it.positionInRoot()
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(3, letters[3], activatedOrder, getOnClick(3, setOf(3,4,5))) {
                node3offset = it.positionInRoot()
            }
            Node(6, letters[6], activatedOrder, getOnClick(6, setOf(6,7,8))) {
                node6offset = it.positionInRoot()
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(4, letters[4], activatedOrder, getOnClick(4, setOf(3,4,5))) {
                node4offset = it.positionInRoot()
            }
            Node(7, letters[7], activatedOrder, getOnClick(7, setOf(6,7,8))) {
                node7offset = it.positionInRoot()
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(5, letters[5], activatedOrder, getOnClick(5, setOf(3,4,5))) {
                node5offset = it.positionInRoot()
            }
            Node(8, letters[8], activatedOrder, getOnClick(8, setOf(6,7,8))) {
                node8offset = it.positionInRoot()
            }
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(9, letters[9], activatedOrder, getOnClick(9, setOf(9,10,11))) {
                node9offset = it.positionInRoot()
            }
            Node(10, letters[10], activatedOrder, getOnClick(10, setOf(9,10,11))) {
                node10offset = it.positionInRoot()
            }
            Node(11, letters[11], activatedOrder, getOnClick(11, setOf(9,10,11))) {
                node11offset = it.positionInRoot()
            }
        }
        Spacer(modifier = Modifier.size(80.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onClick@{
                if (activatedOrder.isEmpty()) return@onClick
                if (activatedOrder.size <= 3) {
                    activatedOrder = activatedOrder.dropLast(1)
                    return@onClick
                }
                if (activatedOrder.lastIndexOf(Constants.SUBMIT) == activatedOrder.size - 2) {
                    activatedOrder = activatedOrder.dropLast(2)
                    return@onClick
                }
                activatedOrder = activatedOrder.dropLast(1)
            }) {
                Text(text = "Delete")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = onClick@{
                if (activatedOrder.isEmpty()) return@onClick
                if (activatedOrder.lastIndexOf(Constants.COMPLETE) != -1) return@onClick
                val lastLetter = activatedOrder.last()
                val startOfMostRecentWord = activatedOrder
                    .lastIndexOf(Constants.SUBMIT)
                    .let {
                        if (it == -1) 0 else it
                    }
                val mostRecentWord = activatedOrder
                    .subList(startOfMostRecentWord, activatedOrder.size)
                    .dropWhile { it == Constants.SUBMIT }
                if (mostRecentWord.size < 3) return@onClick
                if (checkComplete()) {
                    activatedOrder = activatedOrder.toMutableList().apply {
                        add(Constants.COMPLETE)
                    }.toList()
                    onComplete()
                } else {
                    activatedOrder = activatedOrder.toMutableList().apply {
                        add(Constants.SUBMIT)
                        add(lastLetter)
                    }.toList()
                }
            }) {
                Text(text = "Submit")
            }
        }
    }

}

@Composable
private fun Node(
    id: Int,
    letter: String,
    activatedOrder: List<Int>,
    onClick: () -> Unit,
    onGloballyPositioned: (LayoutCoordinates) -> Unit
) {
    val activated = id in activatedOrder
    val lastActivated = activatedOrder.isNotEmpty() && activatedOrder.last() == id

    var border: BorderStroke? = BorderStroke(1.dp, Color.Black)
    when {
        activated && lastActivated -> {
            border = BorderStroke(3.dp, Constants.ACTIVATION_GREEN)
        }
        activated -> {
            border = BorderStroke(1.dp, Constants.ACTIVATION_GREEN)
        }
    }

    OutlinedButton(
        shape = RoundedCornerShape(CornerSize(0)),
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
        Text(text = letter)
    }
}

object Constants {
    const val SUBMIT = -99
    const val COMPLETE = -100
    val DOTTED_PATH_EFFECT = PathEffect.dashPathEffect(listOf(15.0f, 15.0f).toFloatArray(), 15.0f)
    val ACTIVATION_GREEN = Color(0xFF0AC27B)
    val ACTIVATION_GREEN_TRANSPARENT = Color(0x440AC27B)
}