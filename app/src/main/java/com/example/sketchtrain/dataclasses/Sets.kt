package com.example.sketchtrain.dataclasses

import java.io.Serializable

data class Sets(
    var number: Number = 0,
    var weight: Double = 0.0,
    var reps: Int = 0
) : Serializable

