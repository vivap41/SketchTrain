package com.example.sketchtrain.dataclasses

data class Progress(
    val idProgress: String = "",
    val nameExercise: String = "",
    val weight: String = "",
    val reps: String = "",
    val date: String = "",
    val idExercise: Exercise
)