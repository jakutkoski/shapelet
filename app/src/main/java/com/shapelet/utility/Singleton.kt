package com.shapelet.utility

import android.app.Activity
import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.DrawScope
import com.shapelet.model.Board
import com.shapelet.model.PuzzleLetter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

object Utility {
    fun checkCompleted(ids: List<Int>): Boolean {
        return ids.lastIndexOf(Constants.COMPLETE) != -1
    }

    fun asSolution(puzzle: List<PuzzleLetter>, ids: List<Int>): String {
        return ids.joinToString("") {
            when (it) {
                Constants.SUBMIT -> ","
                Constants.COMPLETE -> ""
                else -> puzzle[it].letter
            }
        }
    }

    fun getNodeOnClick(
        puzzle: List<PuzzleLetter>,
        id: Int,
        ids: List<Int>,
        activate: (List<Int>) -> List<Int>
    ): () -> Unit {
        return onClick@{
            if (ids.isNotEmpty()) {
                if (ids.size >= 80) return@onClick
                if (checkCompleted(ids)) return@onClick
                if (ids.last() in puzzle[id].incompatibleIds) return@onClick
            }
            activate(listOf(id))
        }
    }

    fun drawSideLine(drawScope: DrawScope, start: Offset, end: Offset, nodeLength: Float) {
        drawScope.drawLine(
            start = centerNodeOffset(start, nodeLength),
            end = centerNodeOffset(end, nodeLength),
            color = Color.Black,
            strokeWidth = Constants.STROKE_WIDTH
        )
    }

    fun drawActivationLines(
        drawScope: DrawScope,
        ids: List<Int>,
        nodeLength: Float,
        offsetOf: (Int) -> Offset
    ) {
        if (ids.size <= 1) return
        val lastSubmitIndex = ids.lastIndexOf(Constants.SUBMIT)
        var i = 0
        while (i + 1 < ids.size) {
            val firstId = ids[i]
            val secondId = ids[i + 1]
            if (firstId !in Constants.INDICATORS && secondId !in Constants.INDICATORS) {
                val completed = checkCompleted(ids)
                drawScope.drawLine(
                    start = centerNodeOffset(offsetOf(firstId), nodeLength),
                    end = centerNodeOffset(offsetOf(secondId), nodeLength),
                    color = if (completed || i + 1 < lastSubmitIndex)
                        Constants.ACTIVATION_GREEN_TRANSPARENT
                    else
                        Color.Black,
                    strokeWidth = Constants.STROKE_WIDTH,
                    pathEffect = if (completed || i + 1 < lastSubmitIndex)
                        null
                    else
                        Constants.DOTTED_PATH_EFFECT
                )
            }
            i++
        }
    }

    fun decode(boardEncoding: String): Board {
        val parts = boardEncoding.split("|")
        val type = parts[0]
        val letters = parts[1].map { it.toString() }
        val keyWords = parts[2].split(",")
        val puzzleLetters = letters.mapIndexed { index, letter ->
            PuzzleLetter(
                id = index,
                letter = letter,
                incompatibleIds = getIncompatibleIds(type, index)
            )
        }
        return Board(
            type = type,
            puzzle = puzzleLetters,
            keyWords = keyWords,
            encoded = boardEncoding
        )
    }

    private fun centerNodeOffset(offset: Offset, nodeLength: Float): Offset {
        return offset.plus(Offset(nodeLength/2.0f, nodeLength/2.0f))
    }

    private fun getIncompatibleIds(boardType: String, id: Int): Set<Int> {
        return when (boardType) {
            "box" -> when (id) {
                0,1,2 -> setOf(0,1,2)
                3,4,5 -> setOf(3,4,5)
                6,7,8 -> setOf(6,7,8)
                9,10,11 -> setOf(9,10,11)
                else -> throw InvalidIdException(id, boardType)
            }
            "cup" -> when (id) {
                0,1,2,3 -> setOf(0,1,2,3)
                4,5,6,7 -> setOf(4,5,6,7)
                8,9,10,11 -> setOf(8,9,10,11)
                else -> throw InvalidIdException(id, boardType)
            }
            else -> throw BoardTypeException(boardType)
        }
    }
}

object Words {
    private var initialized = false
    lateinit var allWords: List<String> private set

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true
        val processed = mutableListOf<String>()
        val inputStream = context.assets.open("all_words.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        while (line != null) {
            processed.add(line)
            line = reader.readLine()
        }
        allWords = processed.toList()
    }
}

object PuzzleDatabase {
    private var initialized = false
    lateinit var puzzles: List<String> private set

    fun initialize(context: Context) {
        if (initialized) return
        initialized = true
        val processed = mutableListOf<String>()
        val inputStream = context.assets.open("test_puzzles.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))
        var line = reader.readLine()
        while (line != null) {
            processed.add(line)
            line = reader.readLine()
        }
        puzzles = processed.toList()
    }
}

object Solutions {
    private var initialized = false
    private lateinit var solutions: List<String>
    private lateinit var updater: (String) -> List<String>

    fun initialize(givenSolutions: List<String>, givenUpdater: (String) -> List<String>) {
        if (initialized) return
        initialized = true
        solutions = givenSolutions
        updater = givenUpdater
    }

    fun update(solution: String) {
        solutions = updater(solution)
    }

    fun update(givenSolutions: List<String>) {
        givenSolutions.forEach { update(it) }
    }

    fun get(): List<String> {
        return solutions
    }
}

object SharedPrefs {
    // value should look like this:
    // type|letters|key,words/user,solution,number,one+user,solution,number,two+...

    fun persist(activity: Activity, puzzle: String, userSolutions: List<String>) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("savedpuzzle", "$puzzle/${userSolutions.joinToString("+")}")
            apply()
        }
    }

    fun clear(activity: Activity) {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("savedpuzzle", "")
            apply()
        }
    }

    fun retrieve(activity: Activity): Pair<String, List<String>>? {
        val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
        val retrieved = sharedPref.getString("savedpuzzle", "")
        if (retrieved.isNullOrBlank()) return null
        val separated = retrieved.split("/")
        val puzzle = separated[0]
        val savedSolutions = separated[1].split("+")
        return Pair(puzzle, savedSolutions)
    }
}

object MessageHandler {
    private val jobs = mutableListOf<Job>()

    private var initialized = false
    private lateinit var scope: CoroutineScope
    private lateinit var snackbarHostState: SnackbarHostState

    fun initialize(givenScope: CoroutineScope, givenSnackbarHostState: SnackbarHostState) {
        if (initialized) return
        initialized = true
        scope = givenScope
        snackbarHostState = givenSnackbarHostState
    }

    fun show(interactive: Boolean, message: String) {
        jobs.forEach(Job::cancel)
        val job = scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                duration = if (interactive) SnackbarDuration.Indefinite else SnackbarDuration.Short,
                withDismissAction = interactive
            )
        }
        jobs.add(job)
    }
}

object Constants {
    const val SUBMIT = -9001
    const val COMPLETE = -9002
    const val STROKE_WIDTH = 4.0f
    const val UNLOCK_AMOUNT = 5
    val INDICATORS = listOf(SUBMIT, COMPLETE)
    val DOTTED_PATH_EFFECT = PathEffect.dashPathEffect(listOf(15.0f, 15.0f).toFloatArray(), 15.0f)
    val ACTIVATION_GREEN = Color(0xFF0AC27B)
    val ACTIVATION_GREEN_TRANSPARENT = Color(0x330AC27B)
    val BACKGROUND = Color(0xFFEEEEEE)
}
