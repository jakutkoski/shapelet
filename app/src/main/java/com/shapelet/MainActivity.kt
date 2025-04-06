package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Box3Board
import com.shapelet.ui.Lane4Board
import com.shapelet.ui.Utility
import com.shapelet.ui.Words
import com.shapelet.ui.theme.ShapeletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Words.initialize(applicationContext)

        val boardEncoding = "box3|ebfnaitucvld|abdicate,eventful"
        val board = Utility.decode(boardEncoding)

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