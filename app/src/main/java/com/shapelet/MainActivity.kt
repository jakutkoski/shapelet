package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.BoardTypeException
import com.shapelet.ui.BoxBoard
import com.shapelet.ui.CupBoard
import com.shapelet.ui.MirrorBoard
import com.shapelet.ui.PuzzleDatabase
import com.shapelet.ui.Utility
import com.shapelet.ui.Words
import com.shapelet.ui.theme.ShapeletTheme

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
            ShapeletTheme {
                when (board.type) {
                    "box" -> BoxBoard(this@MainActivity, board.puzzle)
                    "cup" -> CupBoard(this@MainActivity, board.puzzle)
                    "mirror" -> MirrorBoard(this@MainActivity, board.puzzle)
                    else -> throw BoardTypeException(board.type)
                }
            }
        }
    }
}