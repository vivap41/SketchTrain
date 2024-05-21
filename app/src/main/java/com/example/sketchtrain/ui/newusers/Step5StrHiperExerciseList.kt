package com.example.sketchtrain.ui.newusers

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
    private lateinit var addExerciseButton: Button
    private lateinit var exerciseAdapter: Step5StrHiperExerciseListAdapter
    private var exerciseList: MutableList<Exercise> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_5_step_str_hip_exercises_list)  // Asegúrate de usar el nombre correcto del layout

        searchEditText = findViewById(R.id.searchEditText)
        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView)
        addExerciseButton = findViewById(R.id.addExerciseButton)

        // Initialize RecyclerView
        exerciseAdapter = Step5StrHiperExerciseListAdapter(exerciseList)
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

        // Set up add exercise button
        addExerciseButton.setOnClickListener {
            // Add new exercise logic here
            addNewExercise()
        }

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
        // Load your initial exercise list here
        // For demonstration, we'll add some dummy data
        exerciseList.add(Exercise("Push Up"))
        exerciseList.add(Exercise("Pull Up"))
        exerciseList.add(Exercise("Squat"))
        exerciseAdapter.notifyDataSetChanged()
    }
}
