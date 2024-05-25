package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Training(
    val idTraining: String= "",
    val description: String = "",
    val type: String = "",
    val date: String = "",
    var mutableList: MutableList <Routine> = mutableListOf()
): Serializable