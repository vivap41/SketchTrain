package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.RoutineAdapter
import com.example.sketchtrain.dataclasses.Routine

class RoutineAct : AppCompatActivity(), RoutineAdapter.OnItemClickListener {
    private lateinit var routineAdapter: RoutineAdapter
    private lateinit var rvRoutine: RecyclerView
    private lateinit var btnFinish: ImageView
    private lateinit var tvTrain: TextView
    private var backPressedTime: Long = 0
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingRoutineIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_record_4_step_str_hip)

        rvRoutine = findViewById(R.id.rvRoutineStr)
        val routines = intent.getSerializableExtra("ROUTINE_LIST") as? MutableList<Routine> ?: mutableListOf()
        routineAdapter = RoutineAdapter(routines, this)
        rvRoutine.adapter = routineAdapter
        rvRoutine.layoutManager = LinearLayoutManager(this)
        rvRoutine.setHasFixedSize(true)

        val descriptions = getRoutineDescriptions(routines)
        descriptions.forEach { description ->
            println(description)
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val train = result.data?.getStringExtra("TRAINING_ID")
            val routine = result.data?.getSerializableExtra("ROUTINE_LIST")
            val routineUpToDate = routineAdapter.routineList[editingRoutineIndex]
            routineUpToDate.idRoutine
        }
    }

    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }

    override fun onClick(descriptionText: String, position: Int) {
        // Implementar la lógica de manejo de clics en ítems de rutina aquí
    }
}
