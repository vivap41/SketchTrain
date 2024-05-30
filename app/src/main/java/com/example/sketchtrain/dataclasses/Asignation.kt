package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Asignation(
    var idRoutine: String = "",
    var idExercise: String = "",
    var idAsignation: String = "",
    var maxWeight: Int = 0,
    var maxWeight1Rep: Double = 0.0,
    var setsList: MutableList<Sets>?
): Serializable