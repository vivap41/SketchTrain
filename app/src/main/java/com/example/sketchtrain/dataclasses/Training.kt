package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Training(
    var idTraining: String= "",
    var description: String = "",
    var type: String = "",
    var date: String = "",
    var routineList: MutableList <Routine> = mutableListOf(),
    var idUser: String
): Serializable