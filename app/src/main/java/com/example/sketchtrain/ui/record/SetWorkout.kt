package com.example.sketchtrain.ui.record

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.adapters.SetWorkoutAdapter
import com.example.sketchtrain.dataclasses.Sets
import com.example.sketchtrain.other.IntentExtras

class SetWorkout : AppCompatActivity(), SetWorkoutAdapter.OnItemClickListener {
    private lateinit var setAdapter: SetWorkoutAdapter
    private lateinit var rvSets: RecyclerView
    private val intEx = IntentExtras
    private lateinit var trainingType: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ui_set_workout)

        rvSets = findViewById(R.id.rvSets)
        val sets = intent.getSerializableExtra(intEx.SET_LIST) as? MutableList<Sets> ?: mutableListOf()
        val exerciseId = intent.getStringExtra(intEx.EXERCISE_ID)
        trainingType = intent.getStringExtra(intEx.TRAINING_TYPE).toString()
        val exerciseIsPower = intent.getBooleanExtra(intEx.EXERCISE_IS_POWER, false)

        setAdapter = SetWorkoutAdapter(sets, this)
        rvSets.adapter = setAdapter
        rvSets.layoutManager = LinearLayoutManager(this)
        rvSets.setHasFixedSize(true)

        val btnFinish: AppCompatButton = findViewById(R.id.btnFinishWorkout)
        val btn1RM: AppCompatButton = findViewById(R.id.bt1RM)

        // Verificar si se debe mostrar el botón bt1RM
        if (trainingType == "Powerlifting" && exerciseIsPower) {
            btn1RM.visibility = View.VISIBLE
        }

        btnFinish.setOnClickListener {
            setAdapter.updateAllSets()

            val intent = Intent(this, ExerciseWorkout::class.java).apply {
                putExtra(intEx.EXERCISE_ID, exerciseId)
                putExtra(intEx.SET_LIST, ArrayList(sets))
                putExtra(intEx.TRAINING_TYPE, trainingType)
                putExtra(intEx.EXERCISE_MAXWEIGHT, getMaxWeight(sets))
            }
            setResult(RESULT_OK, intent)
            finish()
        }

        btn1RM.setOnClickListener {
            calculateAndShow1RM()
        }
    }
    private fun getMaxWeight(sets: List<Sets>): Double {
        return sets.maxByOrNull { it.weight }?.weight ?: 0.0
    }
    private fun calculateAndShow1RM() {
        setAdapter.updateAllSets() // Actualizar los valores en la lista
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

    override fun onClick(weightText: Double, repsText: Int, position: Int, set: Sets) {
        // Implementar si se requiere acción al hacer clic en un set específico
    }
}
