package com.example.sketchtrain.ui.newusers

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.Step4StrHiperRoutineAdapter
import com.example.sketchtrain.dataclasses.Routine

class Step4StrHiperRoutine : AppCompatActivity() {

    private lateinit var Step4StrHiperRoutineAdapter: Step4StrHiperRoutineAdapter
    private lateinit var rvExStr: RecyclerView
    private lateinit var btnFinish: AppCompatButton
    private lateinit var tvTrain: TextView  // Declarar el TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_4_step_str_hip)

        rvExStr = findViewById(R.id.rvRoutineStr)
        val routines = mutableListOf(Routine())  // Inicializa con un elemento vacío
        Step4StrHiperRoutineAdapter = Step4StrHiperRoutineAdapter(routines)
        rvExStr.adapter = Step4StrHiperRoutineAdapter
        rvExStr.layoutManager = LinearLayoutManager(this)
        rvExStr.setHasFixedSize(true)

        tvTrain = findViewById(R.id.tvTrainName)  // Inicializar el TextView

        // Recuperar el valor del intent y actualizar el TextView
        val trainDescription = intent.getStringExtra("TRAIN_DESCRIPTION")
        trainDescription?.let {
            tvTrain.text = it
        }

        // Llamar a la función para obtener descripciones
        val descriptions = getRoutineDescriptions(routines)
        descriptions.forEach { description ->
            println(description) // Puedes reemplazar esto con cualquier lógica que necesites
        }
    }

    // Función para obtener las descripciones de la lista de rutinas
    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }
}
