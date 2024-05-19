package com.example.sketchtrain.ui.newusers.strength.routine

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R

class FourthStep: AppCompatActivity() {

    private lateinit var exerciseAdapter: ExerciseAdapter
    private lateinit var rvExStr: RecyclerView
    private lateinit var btnAddEx: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_4_step_str_hip)

        rvExStr = findViewById(R.id.rvExStr)
        val exercises = mutableListOf(Exercise())  // Inicializa con un elemento vacío
        exerciseAdapter = ExerciseAdapter(exercises)
        rvExStr.adapter = exerciseAdapter
        rvExStr.layoutManager = LinearLayoutManager(this)
        rvExStr.setHasFixedSize(true)
    }

}