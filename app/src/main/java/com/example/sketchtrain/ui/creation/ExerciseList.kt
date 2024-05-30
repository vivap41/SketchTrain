package com.example.sketchtrain.ui.creation

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.ExerciseListAdapter
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.other.IntentExtras
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

class ExerciseList : AppCompatActivity() {

    private lateinit var searchEditText: EditText
    private lateinit var exerciseRecyclerView: RecyclerView
    private lateinit var exerciseAdapter: ExerciseListAdapter
    private var exerciseList: MutableList<Exercise> = mutableListOf()
    private val intEx = IntentExtras

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_exercise_list)

        searchEditText = findViewById(R.id.searchEditText)
        exerciseRecyclerView = findViewById(R.id.exerciseRecyclerView)

        exerciseAdapter = ExerciseListAdapter(exerciseList, { exercise ->
            val replacePosition = intent.getIntExtra(intEx.REPLACE_POSITION, -1)
            val returnIntent = Intent().apply {
                putExtra(intEx.EXERCISE_ID, exercise.idExercise)
                putExtra(intEx.EXERCISE_NAME, exercise.name)
                putExtra(intEx.EXERCISE_IS_POWER, exercise.isPower)
                putExtra(intEx.REPLACE_POSITION, replacePosition)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }, {
            showAddExerciseDialog()
        })

        exerciseRecyclerView.layoutManager = LinearLayoutManager(this)
        exerciseRecyclerView.adapter = exerciseAdapter

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                filterExercises(query)
            }
        })

        loadExercisesFromFirestore()
    }

    private fun filterExercises(query: String) {
        val filteredList = exerciseList.filter { it.name.contains(query, ignoreCase = true) }
        exerciseAdapter.updateList(filteredList.toMutableList())
    }

    private fun showAddExerciseDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.new_exercise_dialog, null)
        val exerciseNameEditText: EditText = dialogView.findViewById(R.id.exerciseNameEditText)
        val powerSwitch: Switch = dialogView.findViewById(R.id.powerSwitch)

        builder.setTitle("New Exercise")
        builder.setView(dialogView)

        builder.setPositiveButton("OK") { dialog, _ ->
            val exerciseName = exerciseNameEditText.text.toString()
            val isPower = powerSwitch.isChecked
            if (exerciseName.isNotBlank()) {
                addNewExercise(exerciseName, isPower)
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun addNewExercise(exerciseName: String, isPower: Boolean) {
        val newExercise = Exercise(idExercise = UUID.randomUUID().toString(), name = exerciseName, isPower = isPower)
        exerciseList.add(newExercise)
        exerciseAdapter.notifyItemInserted(exerciseList.size - 1)
        uploadExerciseToFirestore(newExercise)  // Subir a Firestore
    }

    private fun loadExercisesFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("Exercises")
            .get()
            .addOnSuccessListener { result ->
                exerciseList.clear()
                for (document in result) {
                    val exercise = Exercise(
                        idExercise = document.getString("idExercise") ?: UUID.randomUUID().toString(),
                        name = document.getString("name") ?: "",
                        isPower = document.getBoolean("isPower") ?: false
                    )
                    exerciseList.add(exercise)
                }
                exerciseAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                println("Error getting documents: $e")
            }
    }

    private fun uploadExerciseToFirestore(exercise: Exercise) {
        val db = FirebaseFirestore.getInstance()
        db.collection("Exercises")
            .add(exercise)
            .addOnSuccessListener { documentReference ->
                println("DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Error adding document: $e")
            }
    }
}
