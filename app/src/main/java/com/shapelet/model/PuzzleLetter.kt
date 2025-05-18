package com.shapelet.model

data class PuzzleLetter(
    val id: Int,
    val letter: String,
    val incompatibleIds: Set<Int>
)