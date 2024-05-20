package com.example.sketchtrain.ui.newusers.strength.hiper.routine

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R

class FourthStep: AppCompatActivity() {

    private lateinit var routineAdapter: RoutineAdapter
    private lateinit var rvExStr: RecyclerView
    private lateinit var btnFinish: AppCompatButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_4_step_str_hip)

        rvExStr = findViewById(R.id.rvRoutineStr)
        val routines = mutableListOf(Routine())  // Inicializa con un elemento vacío
        routineAdapter = RoutineAdapter(routines)
        rvExStr.adapter = routineAdapter
        rvExStr.layoutManager = LinearLayoutManager(this)
        rvExStr.setHasFixedSize(true)

    }

}