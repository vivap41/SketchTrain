package com.example.sketchtrain.dataclasses

data class Exercise(
    val idExercise: String = "",
    val name: String = "",
    val seriesNumber: Number = 0,
    val seriesType: String = "",
    val repetitionsNumber: Number = 0,
    val weight: Number = 0,
    val maxRepetitions: Number = 0,
    val duration: Number = 0,
    val ispower:Boolean = false,
    val isAccesory:Boolean = false,
    )