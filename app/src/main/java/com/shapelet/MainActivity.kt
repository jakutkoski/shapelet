package com.shapelet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.shapelet.ui.BoardTypeException
import com.shapelet.ui.BoxBoard
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

        val puzzleChoice = PuzzleDatabase.puzzles.random()
        println("Puzzle Choice: $puzzleChoice")
        val board = Utility.decode(puzzleChoice)

        setContent {
            val snackbarHostState = SnackbarHostState()
            val scope = rememberCoroutineScope()
            MessageHandler.initialize(scope, snackbarHostState)

            var solutions by rememberSaveable { mutableStateOf(listOf<String>()) }
            val updateSolutions: (String) -> List<String> = { solution ->
                solutions = solutions.toMutableList().apply { add(solution) }.toList()
                solutions
            }
            Solutions.initialize(solutions, updateSolutions)

            val amountToUnlock = 5
            val specialWordsUnlocked = solutions.size >= amountToUnlock

            ShapeletTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = {
                                // TODO is this needed if I do not customize?
                                // this is where you can customize how the snackbar looks
                                Snackbar(
                                    snackbarData = it
                                )
                            }
                        )
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Solved: ${solutions.size}")
                            },
                            actions = {
                                IconButton(
                                    enabled = true,
                                    onClick = {
                                        if (specialWordsUnlocked) {
                                            val message = puzzleChoice
                                                .substringAfter("|")
                                                .substringAfter("|")
                                                .uppercase()
                                                .replace(",", " - ")
                                            MessageHandler.show(true, message)
                                        } else {
                                            val message = "Solve $amountToUnlock different ways to reveal special words"
                                            MessageHandler.show(false, message)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (specialWordsUnlocked) Icons.Default.Star else Icons.Default.Lock,
                                        contentDescription = "Special Words"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    when (board.type) {
                        "box" -> BoxBoard(board.puzzle)
                        "cup" -> CupBoard(board.puzzle)
                        else -> throw BoardTypeException(board.type)
                    }
                }
            }
        }
    }

}