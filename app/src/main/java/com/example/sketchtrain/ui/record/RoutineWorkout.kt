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
import com.example.sketchtrain.adapters.RoutineWorkoutAdapter
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.objects.IntentExtras

class RoutineWorkout : AppCompatActivity(), RoutineWorkoutAdapter.OnItemClickListener {
    private lateinit var routineAdapter: RoutineWorkoutAdapter
    private lateinit var rvRoutine: RecyclerView
    private val intEx = IntentExtras
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingRoutineIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_routine_workout)

        rvRoutine = findViewById(R.id.rvRoutineStr)
        val routines = intent.getSerializableExtra(intEx.ROUTINE_LIST) as? MutableList<Routine> ?: mutableListOf()
        routineAdapter = RoutineWorkoutAdapter(routines, this)
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
            val train = result.data?.getStringExtra(intEx.TRAINING_ID)
            val routine = result.data?.getSerializableExtra(intEx.ROUTINE_LIST)
            val routineUpToDate = routineAdapter.routineList[editingRoutineIndex]
            routineUpToDate.idRoutine
        }
    }

    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }

    override fun onClick(routine: Routine) {
        val intent = Intent(this, ExerciseWorkout::class.java).apply {
            putExtra(intEx.ROUTINE_DESCRIPTION, routine.description)
            putExtra(intEx.EXERCISE_LIST, routine.exerciseList as java.io.Serializable)
        }
        resultLauncher.launch(intent)
    }

}
