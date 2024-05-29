package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.ExerciseWorkoutAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Sets
import com.example.sketchtrain.other.IntentExtras

class ExerciseWorkout : AppCompatActivity(), ExerciseWorkoutAdapter.OnItemClickListener {
    private lateinit var exerciseAdapter: ExerciseWorkoutAdapter
    private lateinit var rvExercise: RecyclerView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingExerciseIndex: Int = -1
    private val intEx = IntentExtras
    private lateinit var trainingType: String
    private var maxWeight: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_exercise_workout)

        rvExercise = findViewById(R.id.rvExercise)
        val exercises = intent.getSerializableExtra(intEx.EXERCISE_LIST) as? MutableList<Exercise>?: mutableListOf()
        val routineId = intent.getStringExtra(intEx.ROUTINE_ID)
        trainingType = intent.getStringExtra(intEx.TRAINING_TYPE).toString()

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
            if (result.resultCode == RESULT_OK) {
                val updatedSets = result.data?.getSerializableExtra(intEx.SET_LIST) as? MutableList<Sets>?: mutableListOf()
                val exerciseId = result.data?.getStringExtra(intEx.EXERCISE_ID)
                 maxWeight = result.data?.getDoubleExtra(intEx.EXERCISE_MAXWEIGHT , 0.0)!!
                if (updatedSets != null && exerciseId != null) {
                    updateExerciseSets(exerciseId, updatedSets)
                }
            }
        }

        val btnFinish: AppCompatButton = findViewById(R.id.btnFinishWorkout)
        btnFinish.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra(intEx.EXERCISE_LIST, ArrayList(exercises))
            resultIntent.putExtra(intEx.ROUTINE_ID,routineId)
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    private fun getExerciseDescriptions(exercises: List<Exercise>): List<String> {
        return exercises.map { it.name }
    }

    override fun onClick(nameText: String, position: Int) {
        val selectedExercise = exerciseAdapter.exerciseList[position]
        editingExerciseIndex = position
        val intent = Intent(this, SetWorkout::class.java).apply {
            putExtra(intEx.EXERCISE_ID, selectedExercise.idExercise)
            putExtra(intEx.SET_LIST, ArrayList(selectedExercise.setsList))
            putExtra(intEx.TRAINING_TYPE, trainingType)
            putExtra(intEx.EXERCISE_IS_POWER, selectedExercise.isPower)
        }
        Log.d("ExerciseWorkout", "Passing sets to SetWorkout: ${selectedExercise.setsList}")
        resultLauncher.launch(intent)
    }

    private fun updateExerciseSets(exerciseId: String, updatedSets: MutableList<Sets>) {
        val exercise = exerciseAdapter.exerciseList.find { it.idExercise == exerciseId }
        if (exercise != null) {
            exercise.setsList = updatedSets
            exercise.maxWeight = maxWeight
            exerciseAdapter.notifyItemChanged(editingExerciseIndex)
        }
    }
}
