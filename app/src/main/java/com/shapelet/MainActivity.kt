package com.shapelet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Box3Board
import com.shapelet.ui.Dictionary
import com.shapelet.ui.PuzzleLetter
import com.shapelet.ui.theme.ShapeletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Dictionary.initialize(applicationContext)

        // TODO write function to interpret board encoding
        // box3|j10x00z10r03c00a00u00e00l00o00i00n03|09|37|journalize|documenting events

        setContent {
            ShapeletTheme {
                Box3Board(
                    puzzle = listOf(
                        PuzzleLetter(0, "j", setOf(0, 1, 2), true),
                        PuzzleLetter(1, "x", setOf(0,1,2)),
                        PuzzleLetter(2, "z", setOf(0,1,2), true),
                        PuzzleLetter(3, "r", setOf(3,4,5), false, 3),
                        PuzzleLetter(4, "c", setOf(3,4,5)),
                        PuzzleLetter(5, "a", setOf(3,4,5)),
                        PuzzleLetter(6, "u", setOf(6,7,8)),
                        PuzzleLetter(7, "e", setOf(6,7,8)),
                        PuzzleLetter(8, "l", setOf(6,7,8)),
                        PuzzleLetter(9, "o", setOf(9,10,11)),
                        PuzzleLetter(10, "i", setOf(9,10,11)),
                        PuzzleLetter(11, "n", setOf(9,10,11), false, 3)
                    ),
                    onComplete = {
                        Toast.makeText(this@MainActivity, "You did it!", Toast.LENGTH_SHORT).show()
                    },
                    onInvalidWord = {
                        Toast.makeText(this@MainActivity, "Not a word.", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}