package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.SetWorkoutAdapter
import com.example.sketchtrain.dataclasses.Sets
import com.example.sketchtrain.other.IntentExtras
import com.google.firebase.firestore.FirebaseFirestore

class SetWorkout : AppCompatActivity(), SetWorkoutAdapter.OnItemClickListener {
    private lateinit var setAdapter: SetWorkoutAdapter
    private lateinit var rvSets: RecyclerView
    private val intEx = IntentExtras
    private lateinit var trainingType: String
    private var exerciseId: String? = null
    private var routineId: String? = null
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_set_workout)

        firestore = FirebaseFirestore.getInstance()

        val exerciName : TextView = findViewById(R.id.tvExerciseName)
        exerciName.text = intent.getStringExtra(intEx.EXERCISE_NAME)
        rvSets = findViewById(R.id.rvSets)

        exerciseId = intent.getStringExtra(intEx.EXERCISE_ID)
        routineId = intent.getStringExtra(intEx.ROUTINE_ID)
        trainingType = intent.getStringExtra(intEx.TRAINING_TYPE).toString()
        val exerciseIsPower = intent.getBooleanExtra(intEx.EXERCISE_IS_POWER, false)

        setAdapter = SetWorkoutAdapter(mutableListOf(), this)
        rvSets.adapter = setAdapter
        rvSets.layoutManager = LinearLayoutManager(this)
        rvSets.setHasFixedSize(true)

        val btnFinish: AppCompatButton = findViewById(R.id.btnFinishWorkout)
        val btn1RM: AppCompatButton = findViewById(R.id.bt1RM)

        if (trainingType == "Powerlifting" && exerciseIsPower) {
            btn1RM.visibility = View.VISIBLE
        }

        btnFinish.setOnClickListener {
            setAdapter.updateAllSets()
            val resultIntent = Intent().apply {
                putExtra(intEx.EXERCISE_ID, exerciseId)
                putExtra(intEx.SET_LIST, ArrayList(setAdapter.setList))
                putExtra(intEx.TRAINING_TYPE, trainingType)
                putExtra(intEx.ROUTINE_ID, routineId)
                putExtra(intEx.ASIGNATION_MAXWEIGHT, getMaxWeight(setAdapter.setList))
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }

        btn1RM.setOnClickListener {
            calculateAndShow1RM()
        }

        loadSetsFromDatabase()
    }

    private fun loadSetsFromDatabase() {
        exerciseId?.let { id ->
            firestore.collection("Asignation")
                .whereEqualTo("idExercise", id)
                .whereEqualTo("idRoutine", routineId)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val sets =
                            document.get("setsList") as? List<HashMap<String, Any>> ?: emptyList()
                        val setsList = sets.mapNotNull { map ->
                            Sets(
                                weight = (map["weight"] as? Long)!!.toInt(),
                                reps = (map["reps"] as? Long)?.toInt() ?: 0 ,

                            )
                        }
                        setAdapter.setList = setsList.toMutableList()
                        setAdapter.notifyDataSetChanged()
                    }
                }
                .addOnFailureListener { e ->
                    e.printStackTrace()
                }
        }
    }

    private fun getMaxWeight(sets: List<Sets>): Int {
        return sets.maxByOrNull { it.weight }?.weight ?: 0
    }

    private fun calculateAndShow1RM() {
        setAdapter.updateAllSets()
        val highestWeightSet = setAdapter.setList.maxByOrNull { it.weight }
        if (highestWeightSet != null) {
            val oneRM = highestWeightSet.weight * (1 + 0.025 * highestWeightSet.reps)
            show1RMDialog(oneRM)
        } else {
            show1RMDialog(0.0)
        }
    }

    private fun show1RMDialog(oneRM: Double) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage("1RM estimado: %.2f kg".format(oneRM))
            .setCancelable(false)
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }

        val alert = dialogBuilder.create()
        alert.setTitle("Resultado 1RM")
        alert.show()
    }

    override fun onClick(weightText: Int, repsText: Int, position: Int, set: Sets) {
    }
}
