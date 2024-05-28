package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Training(
    val idTraining: String= "",
    var description: String = "",
    val type: String = "",
    val date: String = "",
    var routineList: MutableList <Routine> = mutableListOf()
): Serializable