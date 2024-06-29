package com.shapelet

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.shapelet.ui.Board
import com.shapelet.ui.theme.ShapeletTheme
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        Dictionary.initialize(applicationContext)

        setContent {
            ShapeletTheme {
                Board("jxzrcaueloin", {
                    Toast.makeText(this@MainActivity, "You did it!", Toast.LENGTH_SHORT).show()
                }, {
                    Toast.makeText(this@MainActivity, "Not a word.", Toast.LENGTH_SHORT).show()
                })
            }
        }
    }
}

object Dictionary {
    private val _words = mutableListOf<String>()
    private var initialized = false

    fun words() = _words.toList()

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true
        val inputStream = context.assets.open("words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        while (line != null) {
            _words.add(line)
            line = reader.readLine()
        }
    }
}
