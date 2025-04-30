package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Box3Board
import com.shapelet.ui.Lane4Board
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
                    "box3" -> Box3Board(this@MainActivity, board.puzzle)
                    "lane4" -> Lane4Board(this@MainActivity, board.puzzle)
                    else -> throw Exception("board type ${board.type} does not exist")
                }
            }
        }
    }
}