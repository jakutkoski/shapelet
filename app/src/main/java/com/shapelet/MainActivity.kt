package com.shapelet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import com.shapelet.ui.BoardTypeException
import com.shapelet.ui.BoxBoard
import com.shapelet.ui.Constants
import com.shapelet.ui.CupBoard
import com.shapelet.ui.MessageHandler
import com.shapelet.ui.PuzzleDatabase
import com.shapelet.ui.Solutions
import com.shapelet.ui.Utility
import com.shapelet.ui.Words
import com.shapelet.ui.theme.ShapeletTheme

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

            var solutions by rememberSaveable { mutableStateOf(listOf<String>()) }
            val updateSolutions: (String) -> List<String> = { solution ->
                solutions = solutions.toMutableList().apply { add(solution) }.toList()
                solutions
            }
            Solutions.initialize(solutions, updateSolutions)

            val amountToUnlock = 5
            val keyWordsUnlocked = solutions.size >= amountToUnlock

            var puzzleChoice by rememberSaveable { mutableStateOf("") }

            val savedProgress = Solutions.retrieve(this)
            if (savedProgress != null && puzzleChoice.isBlank()) {
                ShapeletTheme {
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
                                Solutions.update(savedProgress.second)
                            }
                        ) {
                            Text("Continue with previous puzzle")
                        }
                        Spacer(modifier = Modifier.size(10.dp))
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
                                Solutions.clear(this@MainActivity)
                            }
                        ) {
                            Text("Start a new puzzle")
                        }
                    }
                }
                return@setContent
            }

            ShapeletTheme {
                Scaffold(
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Solved: ${solutions.size}")
                            },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Constants.ACTIVATION_GREEN_TRANSPARENT
                            ),
                            actions = {
                                IconButton(
                                    enabled = true,
                                    onClick = {
                                        if (keyWordsUnlocked) {
                                            val message = puzzleChoice
                                                .substringAfter("|")
                                                .substringAfter("|")
                                                .uppercase()
                                                .replace(",", " - ")
                                            MessageHandler.show(true, message)
                                        } else {
                                            val message = "Solve $amountToUnlock different ways to reveal the Key Words"
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
                    if (puzzleChoice.isBlank()) {
                        puzzleChoice = PuzzleDatabase.puzzles.random()
                    }
                    val board = Utility.decode(puzzleChoice)
                    when (board.type) {
                        "box" -> BoxBoard(this, board.puzzle, puzzleChoice)
                        "cup" -> CupBoard(this, board.puzzle, puzzleChoice)
                        else -> throw BoardTypeException(board.type)
                    }
                }
            }
        }
    }

}