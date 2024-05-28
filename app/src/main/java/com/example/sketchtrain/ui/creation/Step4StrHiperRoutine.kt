package com.example.sketchtrain.ui.creation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.Step4StrHiperRoutineAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.ui.Aaaaa

class Step4StrHiperRoutine : AppCompatActivity(), Step4StrHiperRoutineAdapter.OnItemClickListener {

    private lateinit var Step4StrHiperRoutineAdapter: Step4StrHiperRoutineAdapter
    private lateinit var rvExStr: RecyclerView
    private lateinit var btnFinish: ImageView
    private lateinit var tvTrain: TextView
    private var backPressedTime: Long = 0
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var editingRoutineIndex: Int = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_crea_4_step_str_hip)

        rvExStr = findViewById(R.id.rvRoutineStr)
        val routines = mutableListOf(Routine())
        Step4StrHiperRoutineAdapter = Step4StrHiperRoutineAdapter(routines, this)
        rvExStr.adapter = Step4StrHiperRoutineAdapter
        rvExStr.layoutManager = LinearLayoutManager(this)
        rvExStr.setHasFixedSize(true)

        tvTrain = findViewById(R.id.tvTrainName)


        val trainDescription = intent.getStringExtra("TRAIN_DESCRIPTION")
        trainDescription?.let {
            tvTrain.text = it
        }
        val trainingType = intent.getStringExtra("TRAINING_TYPE") ?: "hypertrophy"


        val descriptions = getRoutineDescriptions(routines)
        descriptions.forEach { description ->
            println(description)
        }

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK && editingRoutineIndex != -1) {
                val exercises = result.data?.getSerializableExtra("EXERCISES_LIST") as? MutableList<Exercise> ?: arrayListOf()
                val desc = result.data?.getStringExtra("ROUTINE_DESCRIPTION") ?: ""
                val routineToUpdate = Step4StrHiperRoutineAdapter.routineList[editingRoutineIndex]
                routineToUpdate.description = desc
                routineToUpdate.exerciseList.clear()
                routineToUpdate.exerciseList.addAll(exercises)
                Step4StrHiperRoutineAdapter.notifyItemChanged(editingRoutineIndex)
            }
        }

        btnFinish = findViewById(R.id.btFinish)
        btnFinish.setOnClickListener {
            val validRoutines = routines.filter { it.description.isNotBlank() }

            val homeIntent = Intent(this, Aaaaa::class.java).apply {
                putExtra("ROUTINE_LIST", ArrayList(validRoutines))
                putExtra("TRAINING_TYPE", trainingType)
                putExtra("TRAIN_DESCRIPTION", trainDescription)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            }
            startActivity(homeIntent)

            finish()
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    showExitDialog()
                } else {
                    Toast.makeText(
                        this@Step4StrHiperRoutine,
                        "Press back again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }


    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setMessage("Are you sure you want to exit? All progress will be lost.")
            .setPositiveButton("Yes") { dialog, which -> finish() }
            .setNegativeButton("No", null)
            .show()
    }

    override fun onClick(descriptionText: String, position: Int) {
        val intent = Intent(this, Step5StrHiperExercise::class.java).apply {
            putExtra("ROUTINE_DESCRIPTION", descriptionText)
            putExtra("EDITING_POSITION", position)
            putExtra("EXERCISES_LIST", Step4StrHiperRoutineAdapter.routineList[position].exerciseList as ArrayList<Exercise>)
        }
        editingRoutineIndex = position

        resultLauncher.launch(intent)
    }

}
