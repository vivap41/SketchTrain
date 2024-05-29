package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.ExerciseWorkoutAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.objects.IntentExtras

class ExerciseWorkout : AppCompatActivity(), ExerciseWorkoutAdapter.OnItemClickListener {
    private lateinit var exerciseAdapter: ExerciseWorkoutAdapter
    private lateinit var rvExercise: RecyclerView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingExerciseIndex: Int = -1
    private val intEx = IntentExtras


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_record_workout)

        rvExercise = findViewById(R.id.rvExercise)
        val exercises = intent.getSerializableExtra(intEx.EXERCISE_LIST) as? MutableList<Exercise> ?: mutableListOf()
        exerciseAdapter = ExerciseWorkoutAdapter(exercises, this)
        rvExercise.adapter = exerciseAdapter
        rvExercise.layoutManager = LinearLayoutManager(this)
        rvExercise.setHasFixedSize(true)

        val descriptions = getExerciseDescriptions(exercises)
        descriptions.forEach { description ->
            println(description)
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            result.data?.getStringExtra(intEx.TRAINING_DESCRIPTION)
            result.data?.getSerializableExtra(intEx.EXERCISE_LIST)
            val exerciseUpToDate = exerciseAdapter.exerciseList[editingExerciseIndex]
            exerciseUpToDate.idExercise
        }
    }

    private fun getExerciseDescriptions(exercises: List<Exercise>): List<String> {
        return exercises.map { it.name }
    }

    override fun onClick(nameText: String, position: Int) {
        // Implementar la lógica de manejo de clics en ítems de ejercicio aquí
    }
}
