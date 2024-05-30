package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Asignation(
    var idRoutine: String = "",
    var idExercise: String = "",
    var idAsignation: String = "",
    var setsList: MutableList <Sets> = mutableListOf()
): Serializable