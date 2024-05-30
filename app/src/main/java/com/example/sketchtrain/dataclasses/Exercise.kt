package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Exercise(
    var idExercise: String = "",
    var name: String = "",
    var isPower:Boolean = false
): Serializable

