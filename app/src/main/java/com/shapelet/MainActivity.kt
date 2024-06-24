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

// TODO work on ability to hit node more than once
// TODO work on submitting
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

    fun getOnClickForNode(nodeNumber: Int, nodeSide: Set<Int>): () -> Unit {
        return {
            if (activatedOrder.isEmpty() ||
                (nodeNumber !in activatedOrder && activatedOrder.last() !in nodeSide)) {
                activatedOrder = activatedOrder.toMutableList().apply { add(nodeNumber) }.toList()
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, top = 230.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(1, activatedOrder, getOnClickForNode(1, setOf(1,2,3)))
            Node(2, activatedOrder, getOnClickForNode(2, setOf(1,2,3)))
            Node(3, activatedOrder, getOnClickForNode(3, setOf(1,2,3)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(4, activatedOrder, getOnClickForNode(4, setOf(4,5,6)))
            Node(7, activatedOrder, getOnClickForNode(7, setOf(7,8,9)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(5, activatedOrder, getOnClickForNode(5, setOf(4,5,6)))
            Node(8, activatedOrder, getOnClickForNode(8, setOf(7,8,9)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(6, activatedOrder, getOnClickForNode(6, setOf(4,5,6)))
            Node(9, activatedOrder, getOnClickForNode(9, setOf(7,8,9)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(10, activatedOrder, getOnClickForNode(10, setOf(10,11,12)))
            Node(11, activatedOrder, getOnClickForNode(11, setOf(10,11,12)))
            Node(12, activatedOrder, getOnClickForNode(12, setOf(10,11,12)))
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

            }) {
                Text(text = "Submit")
            }
        }
    }

}

@Composable
private fun Node(id: Int, activatedOrder: List<Int>, onClick: () -> Unit) {
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
        Text(text = id.toString())
    }
}