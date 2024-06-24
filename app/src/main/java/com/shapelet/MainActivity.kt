package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
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

// TODO restructure Nodes to be Buttons in Rows/Columns
// TODO work on deleting
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

                    var activatedOrder by rememberSaveable { mutableStateOf(listOf<Int>()) }

                    fun getOnClickForNode(nodeNumber: Int, nodeSide: Set<Int>): () -> Unit {
                        return {
                            if (activatedOrder.isEmpty() ||
                                (nodeNumber !in activatedOrder && activatedOrder.last() !in nodeSide)) {
                                activatedOrder = activatedOrder.toMutableList().apply { add(nodeNumber) }.toList()
                            }
                        }
                    }

                    fun checkLastActivated(nodeNumber: Int) = activatedOrder.isNotEmpty() && activatedOrder.last() == nodeNumber

                    Column(modifier = Modifier.size(600.dp)) {
                        Row(modifier = Modifier.fillMaxSize()) {
                            Node(100, 100, 1 in activatedOrder, checkLastActivated(1), getOnClickForNode(1, setOf(1,2,3)))
                            Node(200, 100, 2 in activatedOrder, checkLastActivated(2), getOnClickForNode(2, setOf(1,2,3)))
                            Node(300, 100, 3 in activatedOrder, checkLastActivated(3), getOnClickForNode(3, setOf(1,2,3)))
                            Node(50, 200, 4 in activatedOrder, checkLastActivated(4), getOnClickForNode(4, setOf(4,5,6)))
                            Node(50, 300, 5 in activatedOrder, checkLastActivated(5), getOnClickForNode(5, setOf(4,5,6)))
                            Node(50, 400, 6 in activatedOrder, checkLastActivated(6), getOnClickForNode(6, setOf(4,5,6)))
                            Node(350, 200, 7 in activatedOrder, checkLastActivated(7), getOnClickForNode(7, setOf(7,8,9)))
                            Node(350, 300, 8 in activatedOrder, checkLastActivated(8), getOnClickForNode(8, setOf(7,8,9)))
                            Node(350, 400, 9 in activatedOrder, checkLastActivated(9), getOnClickForNode(9, setOf(7,8,9)))
                            Node(100, 500, 10 in activatedOrder, checkLastActivated(10), getOnClickForNode(10, setOf(10,11,12)))
                            Node(200, 500, 11 in activatedOrder, checkLastActivated(11), getOnClickForNode(11, setOf(10,11,12)))
                            Node(300, 500, 12 in activatedOrder, checkLastActivated(12), getOnClickForNode(12, setOf(10,11,12)))
                        }
                    }

                }
            }
        }
    }
}

@Composable
private fun Node(x: Int, y: Int, activated: Boolean, lastActivated: Boolean, onClick: () -> Unit) {
    Canvas(
        modifier = Modifier
            .offset(x = x.dp, y = y.dp)
            .clickable(onClick = onClick)
    ) {
        var color = Color.Black
        var style: DrawStyle = Stroke(width = 10.0f)
        when {
            activated && lastActivated -> {
                color = Color(0xFFF2A349)
            }
            activated -> {
                color = Color(0xFFF2A349)
                style = Fill
            }
        }
        drawCircle(
            color = color,
            style = style,
            radius = 50.0f
        )
    }
}