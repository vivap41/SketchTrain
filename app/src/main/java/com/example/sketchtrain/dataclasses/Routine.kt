package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Routine(
    var idRoutine: String = "",
    var description: String = "",
    var exerciseList: MutableList <Exercise> = mutableListOf()
): Serializable
