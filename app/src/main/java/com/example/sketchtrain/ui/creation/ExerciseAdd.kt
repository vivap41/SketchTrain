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
import com.example.sketchtrain.adapters.ExerciseAddAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.other.IntentExtras

class ExerciseAdd : AppCompatActivity() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var exerciseAdapter: ExerciseAddAdapter
    private lateinit var btnFinish: ImageView
    private var exercises: MutableList<Exercise> = mutableListOf()
    private val intEx = IntentExtras

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_exercise_add)

        //GET ROUTINE
        val descriptionRoutine = intent.getStringExtra(intEx.ROUTINE_DESCRIPTION)
        val idRoutine = intent.getStringExtra(intEx.ROUTINE_ID)
        exercises = intent.getSerializableExtra(intEx.EXERCISE_LIST) as ArrayList<Exercise>

        val tvRout: TextView = findViewById(R.id.tvTitle)
        tvRout.text = "$descriptionRoutine Routine"

        val recyclerView: RecyclerView = findViewById(R.id.rvExerciseStr)
        exerciseAdapter = ExerciseAddAdapter(exercises, this::showOptionsDialog)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = exerciseAdapter

        val addEx: AppCompatButton = findViewById(R.id.btnAdd)
        addEx.setOnClickListener {
            val intent = Intent(this, ExerciseList::class.java)
            resultLauncher.launch(intent)
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val exerciseId = data.getStringExtra(intEx.EXERCISE_ID).toString()
                    val exerciseName = data.getStringExtra(intEx.EXERCISE_NAME).toString()
                    val exerciseIsPower = data.getBooleanExtra(intEx.EXERCISE_IS_POWER, false)
                    val exerciseMaxWeight = data.getIntExtra(intEx.EXERCISE_MAXWEIGHT, 0)
                    val exerciseMaxReps = data.getIntExtra(intEx.EXERCISE_MAXREPS, 0)
                    val replacePosition = data.getIntExtra(intEx.REPLACE_POSITION, -1)

                    if (replacePosition != -1) {
                        replaceExerciseInList(replacePosition, exerciseId, exerciseName, exerciseIsPower,  exerciseMaxWeight, exerciseMaxReps)
                    } else {
                        updateExerciseList(exerciseId, exerciseName, exerciseIsPower, exerciseMaxWeight, exerciseMaxReps)
                    }
                }
            }
        }

        btnFinish = findViewById(R.id.btFinish)
        btnFinish.setOnClickListener {
            val data = Intent().apply {
                putExtra(intEx.EXERCISE_LIST, ArrayList(exercises))
                putExtra(intEx.ROUTINE_ID, idRoutine)
                putExtra(intEx.ROUTINE_DESCRIPTION, descriptionRoutine)
            }
            setResult(RESULT_OK, data)
            finish()
        }
    }

    private fun showOptionsDialog(position: Int) {
        val options = arrayOf("Delete", "Replace")
        AlertDialog.Builder(this).setTitle("Choose Option").setItems(options) { dialog, which ->
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
        val intent = Intent(this, ExerciseList::class.java).apply {
            putExtra(intEx.REPLACE_POSITION, position)
        }
        resultLauncher.launch(intent)
    }

    private fun replaceExerciseInList(position: Int, exerciseId: String?, exerciseName: String?, exerciseIsPower: Boolean,  exerciseMaxWeight: Int, exerciseMaxReps: Int) {
        exerciseId?.let { id ->
            exerciseName?.let { name ->
                exercises[position] = Exercise(idExercise = id, name = name, isPower = exerciseIsPower, maxWeight1Rep = exerciseMaxWeight, maxReps = exerciseMaxReps)
                exerciseAdapter.notifyItemChanged(position)
            }
        }
    }

    private fun updateExerciseList(exerciseId: String?, exerciseName: String?, exerciseIsPower: Boolean,  exerciseMaxWeight: Int, exerciseMaxReps: Int) {
        exerciseId?.let { id ->
            exerciseName?.let { name ->
                exercises.add(Exercise(idExercise = id, name = name, isPower = exerciseIsPower,  maxWeight1Rep = exerciseMaxWeight, maxReps = exerciseMaxReps))
                exerciseAdapter.notifyItemInserted(exercises.size - 1)
            }
        }
    }
}
