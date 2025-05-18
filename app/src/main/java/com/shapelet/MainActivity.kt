package com.shapelet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.shapelet.components.BoxBoard
import com.shapelet.components.CupBoard
import com.shapelet.theme.ShapeletTheme
import com.shapelet.utility.BoardTypeException
import com.shapelet.utility.Constants
import com.shapelet.utility.MessageHandler
import com.shapelet.utility.PuzzleDatabase
import com.shapelet.utility.SharedPrefs
import com.shapelet.utility.Solutions
import com.shapelet.utility.Utility
import com.shapelet.utility.Words

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Words.initialize(applicationContext)
        PuzzleDatabase.initialize(applicationContext)
        val snackbarHostState = SnackbarHostState()

        setContent {
            MessageHandler.initialize(rememberCoroutineScope(), snackbarHostState)

            var puzzleChoice by rememberSaveable { mutableStateOf("") }
            var solutions by rememberSaveable { mutableStateOf(listOf<String>()) }
            val updater: (String) -> List<String> = { solution ->
                solutions = solutions.toMutableList().apply { add(solution) }.toList()
                solutions
            }
            Solutions.initialize(solutions, updater)
            val keyWordsUnlocked = solutions.size >= Constants.UNLOCK_AMOUNT

            val savedProgress = SharedPrefs.retrieve(this)
            if (savedProgress != null && puzzleChoice.isBlank()) {
                ShapeletTheme {
                    Column(
                        modifier = Modifier.fillMaxSize().background(Constants.BACKGROUND),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        OutlinedButton(
                            shape = CutCornerShape(CornerSize(8)),
                            modifier = Modifier.width(300.dp).height(50.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                puzzleChoice = savedProgress.first
                                savedProgress.second?.let { Solutions.update(it) }
                            }
                        ) {
                            val solvedAmount = savedProgress.second?.size ?: 0
                            Text("Continue (Solved: $solvedAmount)")
                        }
                        Spacer(modifier = Modifier.size(50.dp))
                        OutlinedButton(
                            shape = CutCornerShape(CornerSize(8)),
                            modifier = Modifier.width(300.dp).height(50.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.White,
                                contentColor = Color.Black
                            ),
                            onClick = {
                                puzzleChoice = PuzzleDatabase.puzzles.random()
                                SharedPrefs.persist(this@MainActivity, puzzleChoice, emptyList())
                            }
                        ) {
                            Text("Start New Puzzle")
                        }
                    }
                }
                return@setContent
            }

            if (puzzleChoice.isBlank()) {
                puzzleChoice = PuzzleDatabase.puzzles.random()
                SharedPrefs.persist(this@MainActivity, puzzleChoice, emptyList())
            }
            val board = Utility.decode(puzzleChoice)

            ShapeletTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = { Text("Solved: ${solutions.size}") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Constants.ACTIVATION_GREEN_TRANSPARENT
                            ),
                            actions = {
                                IconButton(
                                    onClick = {
                                        if (keyWordsUnlocked) {
                                            val message = board.keyWords.joinToString(" - ").uppercase()
                                            MessageHandler.show(true, message)
                                        } else {
                                            val message = "Solve ${Constants.UNLOCK_AMOUNT} different ways to reveal Key Words"
                                            MessageHandler.show(false, message)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (keyWordsUnlocked) Icons.Default.Star else Icons.Default.Lock,
                                        contentDescription = "Key Words"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    when (board.type) {
                        "box" -> BoxBoard(this, board)
                        "cup" -> CupBoard(this, board)
                        else -> throw BoardTypeException(board.type)
                    }
                }
            }
        }
    }

}