package com.shapelet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Box3Board
import com.shapelet.ui.Cup3Board
import com.shapelet.ui.Dictionary
import com.shapelet.ui.Utility
import com.shapelet.ui.theme.ShapeletTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Dictionary.initialize(applicationContext)

//        val boardEncoding = "box3|jxzrcaueloin|101000000000|000300000003|journalize|documenting events"
        val boardEncoding = "cup3|jxzrcauel|101000000|000300000|idk|idk yet"
        val board = Utility.decode(boardEncoding)

        setContent {
            ShapeletTheme {
                when (board.type) {
                    "box3" -> Box3Board(this@MainActivity, board.puzzle)
                    "cup3" -> Cup3Board(this@MainActivity, board.puzzle)
                    else -> throw Exception("board type ${board.type} does not exist")
                }
            }
        }
    }
}