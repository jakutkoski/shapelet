package com.shapelet.model

data class Board(
    val type: String,
    val puzzle: List<PuzzleLetter>,
    val keyWords: List<String>,
    val encoded: String
)