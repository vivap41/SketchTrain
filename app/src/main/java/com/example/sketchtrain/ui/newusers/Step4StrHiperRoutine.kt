package com.example.sketchtrain.ui.newusers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.Step4StrHiperRoutineAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.dataclasses.Training
import java.time.LocalDate
import java.util.UUID

class Step4StrHiperRoutine : AppCompatActivity() , Step4StrHiperRoutineAdapter.OnItemClickListener{

    private lateinit var Step4StrHiperRoutineAdapter: Step4StrHiperRoutineAdapter
    private lateinit var rvExStr: RecyclerView
    private lateinit var btnFinish: ImageView
    private lateinit var tvTrain: TextView  // Declarar el TextView
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_4_step_str_hip)

        resultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult())
        { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                var exercises = result.data?.getSerializableExtra("EXERCISE_LIST") as ArrayList<Exercise>
                var mutList = mutableListOf<Exercise>()
                mutList.addAll(exercises)
                Routine(
                    idRoutine = UUID.randomUUID().toString(),
//                    description =
                    mutableList = exercises
                )
            }

        }

        rvExStr = findViewById(R.id.rvRoutineStr)
        val routines = mutableListOf(Routine())
        Step4StrHiperRoutineAdapter = Step4StrHiperRoutineAdapter(routines, this)
        rvExStr.adapter = Step4StrHiperRoutineAdapter
        rvExStr.layoutManager = LinearLayoutManager(this)
        rvExStr.setHasFixedSize(true)

        tvTrain = findViewById(R.id.tvTrainName)

        btnFinish = findViewById(R.id.btFinish)
        btnFinish.setOnClickListener {
            Routine(
                idRoutine = UUID.randomUUID().toString(),
                description = tvTrain.text.toString(),
//                mutableList =
            )
            val validRoutines = routines.filter { it.description.isNotBlank() }
            val data = Intent().apply {
                putExtra("ROUTINE_LIST", ArrayList(validRoutines)) // Pasa solo rutinas válidas
            }
            setResult(RESULT_OK, data)
            finish()
        }
        val trainDescription = intent.getStringExtra("TRAIN_DESCRIPTION")
        trainDescription?.let {
            tvTrain.text = it
        }

        val descriptions = getRoutineDescriptions(routines)
        descriptions.forEach { description ->
            println(description)
        }
    }

    private fun getRoutineDescriptions(routines: List<Routine>): List<String> {
        return routines.map { it.description }
    }

    override fun onClick(descriptionText : String ) {
        if (descriptionText.isEmpty()) {
        Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
    } else {
        val intent = Intent(this, Step5StrHiperExercise::class.java).apply {
            putExtra("ROUTINE_DESCRIPTION", descriptionText)
        }
        resultLauncher.launch(intent)
    }
    }
}
