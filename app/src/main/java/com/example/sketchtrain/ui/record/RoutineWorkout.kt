package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.RoutineWorkoutAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.other.IntentExtras
import java.time.LocalDate

class RoutineWorkout : AppCompatActivity(), RoutineWorkoutAdapter.OnItemClickListener {
    private lateinit var routineAdapter: RoutineWorkoutAdapter
    private lateinit var rvRoutine: RecyclerView
    private val intEx = IntentExtras
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingRoutineIndex: Int = -1
    private lateinit var trainingType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_routine_workout)

        rvRoutine = findViewById(R.id.rvRoutineStr)
        val routines = intent.getSerializableExtra(intEx.ROUTINE_LIST) as? MutableList<Routine>
            ?: mutableListOf()
        val trainingId = intent.getStringExtra(intEx.TRAINING_ID)
        trainingType = intent.getStringExtra(intEx.TRAINING_TYPE).toString()
        val trainingDescription = intent.getStringExtra(intEx.TRAINING_DESCRIPTION)

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
            if (result.resultCode == RESULT_OK) {
                val updatedExercises =
                    result.data?.getSerializableExtra(intEx.EXERCISE_LIST) as? ArrayList<Exercise>
                val routineId = result.data?.getStringExtra(intEx.ROUTINE_ID)
                if (updatedExercises != null && routineId != null) {
                    updateRoutineExercises(routineId, updatedExercises)
                }
            }
        }

        val btFinish = findViewById<ImageView>(R.id.btFinish)
        btFinish.setOnClickListener {
            val intent = Intent().apply {
                putExtra(intEx.TRAINING_ID, trainingId)
                putExtra(intEx.TRAINING_TYPE, trainingType)
                putExtra(intEx.TRAINING_DESCRIPTION, trainingDescription)
                putExtra(intEx.ROUTINE_LIST, ArrayList(routines))
                putExtra(intEx.TRAINING_DATE, LocalDate.now().toString())
                putExtra(intEx.DO, "update").toString()
            }
            setResult(RESULT_OK, intent)
            finish()
        }
    }

    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }

    override fun onClick(routine: Routine, position: Int) {
        editingRoutineIndex = position
        val intent = Intent(this, ExerciseWorkout::class.java).apply {
            putExtra(intEx.ROUTINE_ID, routine.idRoutine)
            putExtra(intEx.EXERCISE_LIST, routine.exerciseList as ArrayList<Exercise>)
            putExtra(intEx.TRAINING_TYPE, trainingType)

        }
        resultLauncher.launch(intent)
    }

    private fun updateRoutineExercises(routineId: String, updatedExercises: ArrayList<Exercise>) {
        val routine = routineAdapter.routineList.find { it.idRoutine == routineId }
        routine?.exerciseList = updatedExercises
        routineAdapter.notifyDataSetChanged()
    }
}