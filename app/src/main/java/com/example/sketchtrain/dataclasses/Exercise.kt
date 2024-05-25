package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Exercise(
    var idExercise: String = "",
    var name: String = "",
    var seriesNumber: Number = 0,
    var seriesType: String = "",
    var repetitionsNumber: Number = 0,
    var weight: Number = 0,
    var maxRepetitions: Number = 0,
    var duration: Number = 0,
    var ispower:Boolean = false,
    var isAccesory:Boolean = false
): Serializable
