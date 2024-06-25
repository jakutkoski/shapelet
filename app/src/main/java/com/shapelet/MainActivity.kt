package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.unit.dp
import com.shapelet.ui.theme.ShapeletTheme

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

    val letterString = "BSAGKOZRUEIT"
    val letters = letterString.toList().map { it.toString() }

    fun getOnClick(nodeNumber: Int, nodeSide: Set<Int>): () -> Unit {
        return {
            if (activatedOrder.isEmpty() || activatedOrder.last() !in nodeSide) {
                activatedOrder = activatedOrder.toMutableList().apply { add(nodeNumber) }.toList()
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
                if (it == Constants.SUBMIT) " - "
                else letters[it]
            }, modifier = Modifier.fillMaxHeight())
        }
        Spacer(modifier = Modifier.size(10.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(0, letters[0], activatedOrder, getOnClick(0, setOf(0,1,2)))
            Node(1, letters[1], activatedOrder, getOnClick(1, setOf(0,1,2)))
            Node(2, letters[2], activatedOrder, getOnClick(2, setOf(0,1,2)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(3, letters[3], activatedOrder, getOnClick(3, setOf(3,4,5)))
            Node(6, letters[6], activatedOrder, getOnClick(6, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(4, letters[4], activatedOrder, getOnClick(4, setOf(3,4,5)))
            Node(7, letters[7], activatedOrder, getOnClick(7, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Node(5, letters[5], activatedOrder, getOnClick(5, setOf(3,4,5)))
            Node(8, letters[8], activatedOrder, getOnClick(8, setOf(6,7,8)))
        }
        Spacer(modifier = Modifier.size(30.dp))
        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(start = 30.dp, end = 30.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Node(9, letters[9], activatedOrder, getOnClick(9, setOf(9,10,11)))
            Node(10, letters[10], activatedOrder, getOnClick(10, setOf(9,10,11)))
            Node(11, letters[11], activatedOrder, getOnClick(11, setOf(9,10,11)))
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
            Button(onClick = onClick@{
                if (activatedOrder.isEmpty()) return@onClick
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
                activatedOrder = activatedOrder.toMutableList().apply {
                    add(Constants.SUBMIT)
                    add(lastLetter)
                }.toList()
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

object Constants {
    const val SUBMIT = -99
}