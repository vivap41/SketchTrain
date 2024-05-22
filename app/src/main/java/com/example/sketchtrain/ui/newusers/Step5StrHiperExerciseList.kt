package com.example.sketchtrain.ui.newusers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.Step5StrHiperExerciseListAdapter
import com.example.sketchtrain.dataclasses.Exercise

class Step5StrHiperExerciseList : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: Step5StrHiperExerciseListAdapter
    private var exerciseList: MutableList<Exercise> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_5_step_str_hip_exercises_list)

        searchEditText = findViewById(R.id.searchEditText)
        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView)

        exerciseAdapter = Step5StrHiperExerciseListAdapter(exerciseList) { exercise ->
            val intent = Intent(this, Step5StrHiperExercise::class.java)
            intent.putExtra("EXERCISE_NAME", exercise.name)
            startActivity(intent)
        }
        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        exerciseRecyclerView.adapter = exerciseAdapter

        // Set up search functionality
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filterExercises(query)
            }
        })

        // Load initial exercise list
        loadExercises()
    }

    private fun filterExercises(query: String) {
        val filteredList = exerciseList.filter { it.name.contains(query, ignoreCase = true) }
        exerciseAdapter.updateList(filteredList)
    }

    private fun addNewExercise() {
        // Logic to add a new exercise
        val newExercise = Exercise("New Exercise")
        exerciseList.add(newExercise)
        exerciseAdapter.notifyItemInserted(exerciseList.size - 1)
    }

    private fun loadExercises() {
        exerciseList.add(Exercise(idExercise = "1", name = "Bench Press"))
        exerciseList.add(Exercise(idExercise = "2", name = "Incline Bench Press"))
        exerciseList.add(Exercise(idExercise = "3", name = "Decline Bench Press"))
        exerciseList.add(Exercise(idExercise = "4", name = "Dumbbell Bench Press"))
        exerciseList.add(Exercise(idExercise = "5", name = "Chest Fly"))
        exerciseList.add(Exercise(idExercise = "6", name = "Incline Dumbbell Fly"))
        exerciseList.add(Exercise(idExercise = "7", name = "Pec Deck"))
        exerciseList.add(Exercise(idExercise = "8", name = "Lat Pulldown"))
        exerciseList.add(Exercise(idExercise = "9", name = "Seated Row"))
        exerciseList.add(Exercise(idExercise = "10", name = "Bent Over Row"))
        exerciseList.add(Exercise(idExercise = "11", name = "One Arm Dumbbell Row"))
        exerciseList.add(Exercise(idExercise = "12", name = "Pull Up"))
        exerciseList.add(Exercise(idExercise = "13", name = "Deadlift"))
        exerciseList.add(Exercise(idExercise = "14", name = "Barbell Curl"))
        exerciseList.add(Exercise(idExercise = "15", name = "Dumbbell Curl"))
        exerciseList.add(Exercise(idExercise = "16", name = "Hammer Curl"))
        exerciseList.add(Exercise(idExercise = "17", name = "Preacher Curl"))
        exerciseList.add(Exercise(idExercise = "18", name = "Tricep Dip"))
        exerciseList.add(Exercise(idExercise = "19", name = "Tricep Pushdown"))
        exerciseList.add(Exercise(idExercise = "20", name = "Skull Crusher"))
        exerciseList.add(Exercise(idExercise = "21", name = "Overhead Tricep Extension"))
        exerciseList.add(Exercise(idExercise = "22", name = "Shoulder Press"))
        exerciseList.add(Exercise(idExercise = "23", name = "Lateral Raise"))
        exerciseList.add(Exercise(idExercise = "24", name = "Front Raise"))
        exerciseList.add(Exercise(idExercise = "25", name = "Reverse Fly"))
        exerciseList.add(Exercise(idExercise = "26", name = "Leg Press"))
        exerciseList.add(Exercise(idExercise = "27", name = "Squat"))
        exerciseList.add(Exercise(idExercise = "28", name = "Leg Extension"))
        exerciseList.add(Exercise(idExercise = "29", name = "Leg Curl"))
        exerciseList.add(Exercise(idExercise = "30", name = "Lunge"))
        exerciseList.add(Exercise(idExercise = "31", name = "Calf Raise"))
        exerciseList.add(Exercise(idExercise = "32", name = "Hip Thrust"))
        exerciseList.add(Exercise(idExercise = "33", name = "Bulgarian Split Squat"))
        exerciseList.add(Exercise(idExercise = "34", name = "Glute Bridge"))
        exerciseList.add(Exercise(idExercise = "35", name = "Hack Squat"))
        exerciseList.add(Exercise(idExercise = "36", name = "Seated Calf Raise"))
        exerciseList.add(Exercise(idExercise = "37", name = "Standing Calf Raise"))
        exerciseList.add(Exercise(idExercise = "38", name = "Cable Fly"))
        exerciseList.add(Exercise(idExercise = "39", name = "Cable Crossover"))
        exerciseList.add(Exercise(idExercise = "40", name = "Face Pull"))
        exerciseAdapter.notifyDataSetChanged()
    }

}
