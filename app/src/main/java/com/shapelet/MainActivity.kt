package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.shapelet.ui.theme.ShapeletTheme

// TODO more logic about requiring at least 2 characters to submit
// TODO check words against dictionary
// TODO better UI, draw lines
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapeletTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    println(innerPadding) // TODO wtf do with this?
                    Board()
                }
            }
        }
    }
}

@Composable
private fun Board() {
    var activatedOrder by rememberSaveable { mutableStateOf(listOf<Int>()) }

    val letters = listOf("N", "L", "B", "W", "C", "A", "K", "G", "E", "U", "R", "O")

    fun getOnClickForNode(nodeNumber: Int, nodeSide: Set<Int>): () -> Unit {
        return {
            if (activatedOrder.isEmpty() || activatedOrder.last() !in nodeSide) {
                activatedOrder = activatedOrder.toMutableList().apply { add(nodeNumber) }.toList()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, top = 100.dp, end = 10.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = activatedOrder.joinToString("") {
                if (it == -1) " - "
                else letters[it]
            })
        }
        Spacer(modifier = Modifier.size(100.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(0, letters[0], activatedOrder, getOnClickForNode(0, setOf(0,1,2)))
            Node(1, letters[1], activatedOrder, getOnClickForNode(1, setOf(0,1,2)))
            Node(2, letters[2], activatedOrder, getOnClickForNode(2, setOf(0,1,2)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(3, letters[3], activatedOrder, getOnClickForNode(3, setOf(3,4,5)))
            Node(6, letters[6], activatedOrder, getOnClickForNode(6, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(4, letters[4], activatedOrder, getOnClickForNode(4, setOf(3,4,5)))
            Node(7, letters[7], activatedOrder, getOnClickForNode(7, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(5, letters[5], activatedOrder, getOnClickForNode(5, setOf(3,4,5)))
            Node(8, letters[8], activatedOrder, getOnClickForNode(8, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(9, letters[9], activatedOrder, getOnClickForNode(9, setOf(9,10,11)))
            Node(10, letters[10], activatedOrder, getOnClickForNode(10, setOf(9,10,11)))
            Node(11, letters[11], activatedOrder, getOnClickForNode(11, setOf(9,10,11)))
        }
        Spacer(modifier = Modifier.size(80.dp))
        Row(modifier = Modifier
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = {
                if (activatedOrder.isNotEmpty()) activatedOrder = activatedOrder.dropLast(1)
            }) {
                Text(text = "Delete")
            }
            Spacer(modifier = Modifier.size(30.dp))
            Button(onClick = {
                if (activatedOrder.isNotEmpty()) {
                    val lastLetter = activatedOrder.last()
                    activatedOrder = activatedOrder.toMutableList().apply {
                        add(-1)
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
private fun Node(id: Int, letter: String, activatedOrder: List<Int>, onClick: () -> Unit) {
    val activated = id in activatedOrder
    val lastActivated = activatedOrder.isNotEmpty() && activatedOrder.last() == id

    var border: BorderStroke? = null
    var color = Color(0x990AC27B)
    when {
        activated && lastActivated -> {
            border = BorderStroke(1.dp, Color.Black)
        }
        activated -> {
            border = BorderStroke(2.dp, Color.Black)
            color = Color(0xFF0AC27B)
        }
    }

    OutlinedButton(
        colors = ButtonDefaults.buttonColors(
            containerColor = color,
            contentColor = Color.White
        ),
        onClick = onClick,
        border = border
    ) {
        Text(text = letter)
    }
}