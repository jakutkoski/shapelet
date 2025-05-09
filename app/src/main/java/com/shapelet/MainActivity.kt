package com.shapelet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import com.shapelet.ui.BoardTypeException
import com.shapelet.ui.BoxBoard
import com.shapelet.ui.CupBoard
import com.shapelet.ui.MirrorBoard
import com.shapelet.ui.PuzzleDatabase
import com.shapelet.ui.Utility
import com.shapelet.ui.Words
import com.shapelet.ui.theme.ShapeletTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Words.initialize(applicationContext)
        PuzzleDatabase.initialize(applicationContext)

        val puzzleChoice = PuzzleDatabase.puzzles.filter { it.startsWith("box") }.random()
        println("Puzzle Choice: $puzzleChoice")
        val board = Utility.decode(puzzleChoice)

        val snackbarHostState = SnackbarHostState()

        setContent {
            ShapeletTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(
                            hostState = snackbarHostState,
                            snackbar = {
                                // this is where you can customize how the snackbar looks
                                Snackbar(
                                    snackbarData = it
                                )
                            }
                        )
                    }
                ) { contentPadding ->
                    when (board.type) {
                        "box" -> BoxBoard(snackbarHostState, board.puzzle)
                        "cup" -> CupBoard(snackbarHostState, board.puzzle)
                        "mirror" -> MirrorBoard(snackbarHostState, board.puzzle)
                        else -> throw BoardTypeException(board.type)
                    }
                }
            }
        }
    }
}