package com.shapelet

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Board
import com.shapelet.ui.theme.ShapeletTheme

// TODO check words against dictionary
// TODO create other types of boards
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShapeletTheme {
                Board("BSAGKOZRUEIT") {
                    Toast.makeText(this@MainActivity, "You did it!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
