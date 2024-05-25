package com.example.sketchtrain.ui.newusers

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.Step5StrHiperExerciseAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Routine
import java.util.UUID

class Step5StrHiperExercise : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var exerciseAdapter: Step5StrHiperExerciseAdapter
    private lateinit var btnFinish: ImageView
    private var exercises: MutableList<Exercise> = mutableListOf()
private var exerciseName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_5_step_str_hip_exercises)


        val recyclerView: RecyclerView = findViewById(R.id.rvExerciseStr)
        exerciseAdapter = Step5StrHiperExerciseAdapter(exercises, this::showOptionsDialog)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter

        val addEx: AppCompatButton = findViewById(R.id.btnAdd)
        addEx.setOnClickListener {
            val intent = Intent(this, Step5StrHiperExerciseList::class.java)
            resultLauncher.launch(intent)
        }

        btnFinish = findViewById(R.id.btFinish)
        btnFinish.setOnClickListener {
            Exercise(
                idExercise = UUID.randomUUID().toString(),
                name = exerciseName?:""
            )
            val validRoutines = exercises.filter { it.name.isNotBlank() }
            val data = Intent().apply {
                putExtra("EXERCISES_LIST", ArrayList(exercises))
            }
            setResult(RESULT_OK, data)
            finish()
        }
        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                exerciseName = result.data?.getStringExtra("EXERCISE_NAME")
                updateExerciseList(exerciseName)
            }
        }

        val description = intent.getStringExtra("ROUTINE_DESCRIPTION")
        val tvRout: TextView = findViewById(R.id.tvTitle)
        tvRout.text = "$description Routine"
    }

    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("Delete", "Replace")
        AlertDialog.Builder(this)
            .setTitle("Choose Option")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> deleteExercise(position)
                    1 -> replaceExercise(position)
                }
            }.show()
    }

    private fun deleteExercise(position: Int) {
        exercises.removeAt(position)
        exerciseAdapter.notifyItemRemoved(position)
    }

    private fun replaceExercise(position: Int) {
        deleteExercise(position)
        val intent = Intent(this, Step5StrHiperExerciseList::class.java)
        resultLauncher.launch(intent)
    }

    private fun updateExerciseList(exerciseName: String?) {
        exerciseName?.let {
            exercises.add(Exercise(name = it))
            exerciseAdapter.notifyItemInserted(exercises.size - 1)
        }
    }
}
