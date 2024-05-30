package com.example.sketchtrain.dataclasses

data class Progress(
    val idProgress: String = "",
    val nameExercise: String = "",
    val maxWeight: Number = 0,
    val maxWeight1Rep: Number =0,
    val date: String = "",
    val idExercise: Exercise,
    val idUser: Users
)