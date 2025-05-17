package com.shapelet

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.rememberCoroutineScope
import com.shapelet.ui.BoardTypeException
import com.shapelet.ui.BoxBoard
import com.shapelet.ui.CupBoard
import com.shapelet.ui.PuzzleDatabase
import com.shapelet.ui.Utility
import com.shapelet.ui.Words
import com.shapelet.ui.theme.ShapeletTheme
import kotlinx.coroutines.launch

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

        val snackbarHostState = SnackbarHostState()

        setContent {
            val scope = rememberCoroutineScope()

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
                    },
                    topBar = {
                        TopAppBar(
                            title = {
                                Text("Solutions: 0")
                            },
                            actions = {
                                IconButton(
                                    enabled = true,
                                    onClick = {
                                        scope.launch {
                                            snackbarHostState.showSnackbar(
                                                message = puzzleChoice
                                                    .substringAfter("|")
                                                    .substringAfter("|")
                                                    .uppercase()
                                                    .replace(",", " - ")
                                                ,
                                                duration = SnackbarDuration.Indefinite,
                                                withDismissAction = true
                                            )
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Lock,
                                        contentDescription = "Special Words"
                                    )
                                }
                            }
                        )
                    }
                ) {
                    when (board.type) {
                        "box" -> BoxBoard(snackbarHostState, board.puzzle)
                        "cup" -> CupBoard(snackbarHostState, board.puzzle)
                        else -> throw BoardTypeException(board.type)
                    }
                }
            }
        }
    }

}