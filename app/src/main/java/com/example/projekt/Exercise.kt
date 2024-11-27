package com.example.projekt

data class Exercise(
    val id: String,
    val name: String,
    val mainMuscle: String,
    val sideMuscle: String,
    val difficulty: String,
    val isDeleteButtonVisible: Boolean = false
)
