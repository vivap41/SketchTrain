package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
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
import com.example.sketchtrain.dataclasses.Asignation
import com.example.sketchtrain.other.IntentExtras

class ExerciseWorkout : AppCompatActivity(), ExerciseWorkoutAdapter.OnItemClickListener {
    private lateinit var exerciseAdapter: ExerciseWorkoutAdapter
    private lateinit var rvExercise: RecyclerView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingExerciseIndex: Int = -1
    private val intEx = IntentExtras
    private lateinit var trainingType: String
    private var routineId: String? = null
    private var assignments: MutableList<Asignation> = mutableListOf() // Nueva lista para almacenar las asignaciones
    private var updatedSets: MutableList<Sets> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_exercise_workout)

        rvExercise = findViewById(R.id.rvExercise)
        val exercises = intent.getSerializableExtra(intEx.EXERCISE_LIST) as? MutableList<Exercise> ?: mutableListOf()
        routineId = intent.getStringExtra(intEx.ROUTINE_ID)
        trainingType = intent.getStringExtra(intEx.TRAINING_TYPE).toString()
        updatedSets = intent.getSerializableExtra(intEx.SET_LIST) as? MutableList<Sets> ?: mutableListOf()

        exercises.forEach { exercise ->
            assignments.add(Asignation(idRoutine = routineId ?: "", idExercise = exercise.idExercise, setsList = updatedSets))
        }

        exerciseAdapter = ExerciseWorkoutAdapter(exercises, this)
        rvExercise.adapter = exerciseAdapter
        rvExercise.layoutManager = LinearLayoutManager(this)
        rvExercise.setHasFixedSize(true)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                updatedSets = result.data?.getSerializableExtra(intEx.SET_LIST) as? MutableList<Sets> ?: mutableListOf()
                val exerciseId = result.data?.getStringExtra(intEx.EXERCISE_ID)

                if (updatedSets.isNotEmpty() && exerciseId != null) {
                    updateExerciseSets(exerciseId, updatedSets)
                }
            }
        }

        val btnFinish: AppCompatButton = findViewById(R.id.btnFinishWorkout)
        btnFinish.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("ASSIGNMENTS", ArrayList(assignments)) // Pasar las asignaciones al intent de resultado
            resultIntent.putExtra(intEx.ROUTINE_ID, routineId)
            resultIntent.putExtra(intEx.EXERCISE_LIST, ArrayList(exercises))
            resultIntent.putExtra(intEx.SET_LIST, ArrayList(updatedSets))
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onClick(nameText: String, position: Int) {
        val selectedExercise = exerciseAdapter.exerciseList[position]
        editingExerciseIndex = position
        val intent = Intent(this, SetWorkout::class.java).apply {
            putExtra(intEx.EXERCISE_ID, selectedExercise.idExercise)
            putExtra(intEx.EXERCISE_NAME, selectedExercise.name)
            putExtra(intEx.EXERCISE_IS_POWER, selectedExercise.isPower)
            putExtra(intEx.TRAINING_TYPE, trainingType)
            putExtra(intEx.ROUTINE_ID, routineId)
            val assignment = assignments.find { it.idExercise == selectedExercise.idExercise }
            putExtra(intEx.SET_LIST, ArrayList(assignment?.setsList ?: mutableListOf()))
        }
        resultLauncher.launch(intent)
    }

    private fun updateExerciseSets(exerciseId: String, updatedSets: MutableList<Sets>) {
        val assignment = assignments.find { it.idExercise == exerciseId }
        assignment?.setsList = updatedSets

        exerciseAdapter.notifyItemChanged(editingExerciseIndex)
    }
}
