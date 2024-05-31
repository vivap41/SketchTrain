package com.example.sketchtrain.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.R

class VisualizeProgress : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_visualize_progress)

        val tvExerciseName = findViewById<TextView>(R.id.tvExerciseName)
        val tvMaxWeights = findViewById<TextView>(R.id.tvMaxWeights)
        val tvDates = findViewById<TextView>(R.id.tvDates)

        // Captura los datos enviados desde el Fragment
        intent?.let {
            val exerciseName = it.getStringExtra("EXERCISE_NAME")
            val maxWeights = it.getIntegerArrayListExtra("MAX_WEIGHTS")
            val dates = it.getStringArrayListExtra("DATES")

            // Actualiza la UI con los datos recibidos
            tvExerciseName.text = exerciseName
            tvMaxWeights.text = "Max Weights: ${maxWeights?.joinToString(", ")}"
            tvDates.text = "Dates: ${dates?.joinToString(", ")}"
        }
    }
}
