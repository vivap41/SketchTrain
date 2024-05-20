package com.example.sketchtrain.dataclasses

data class Exercise(
    val idExercise: String,
    val name: String = "",
    val seriesNumber: Number,
    val seriesType: String,
    val repetitionsNumber: Number,
    val weight: Number,
    val maxRepetitions: Number,
    val duration: Number,
    val ispower:Boolean,
    val isAccesory:Boolean,
    )