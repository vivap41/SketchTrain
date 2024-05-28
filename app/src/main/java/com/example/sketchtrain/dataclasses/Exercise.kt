package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Exercise(
    var idExercise: String = "",
    var name: String = "",
    var maxWeight1Rep: Number = 0,
    var ispower:Boolean = false,
    var isAccesory:Boolean = false,
    var setsList: MutableList <Sets> = mutableListOf()
): Serializable

