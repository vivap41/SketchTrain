package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Exercise(
    var idExercise: String = "",
    var name: String = "",
    var maxWeight1Rep: Number = 0,
    var isPower:Boolean = false,
    var setsList: MutableList <Sets> = mutableListOf(),
    var maxWeight: Double = 0.0,
    var maxReps: Number = 0
): Serializable

