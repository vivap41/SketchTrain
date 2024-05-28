package com.example.sketchtrain.ui.creation

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

class Step5StrHiperExercise : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var exerciseAdapter: Step5StrHiperExerciseAdapter
    private lateinit var btnFinish: ImageView
    private var exercises: MutableList<Exercise> = mutableListOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_crea_5_step_str_hip_exercises)


        val description = intent.getStringExtra("ROUTINE_DESCRIPTION")
        exercises = intent.getSerializableExtra("EXERCISES_LIST") as ArrayList<Exercise>
        val tvRout: TextView = findViewById(R.id.tvTitle)
        tvRout.text = "$description Routine"

        val recyclerView: RecyclerView = findViewById(R.id.rvExerciseStr)
        exerciseAdapter = Step5StrHiperExerciseAdapter(exercises, this::showOptionsDialog)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter

        val addEx: AppCompatButton = findViewById(R.id.btnAdd)
        addEx.setOnClickListener {
            val intent = Intent(this, Step5StrHiperExerciseList::class.java)
            resultLauncher.launch(intent)
        }


        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val exerciseName = result.data?.getStringExtra("EXERCISE_NAME").toString()
                updateExerciseList(exerciseName)
            }
        }

        btnFinish = findViewById(R.id.btFinish)
        btnFinish.setOnClickListener {
            val data = Intent().apply {
                putExtra("EXERCISES_LIST", ArrayList(exercises))
                putExtra("ROUTINE_DESCRIPTION", description)
            }
            setResult(RESULT_OK, data)
            finish()
        }
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
