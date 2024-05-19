package com.example.sketchtrain.ui.newusers.strength.routine

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.google.android.material.textfield.TextInputEditText

class ExerciseAdapter(private val exercises: MutableList<Exercise>) : RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextInputEditText = itemView.findViewById(R.id.descEt)
        val button: AppCompatButton = itemView.findViewById(R.id.btnEx)

        init {
            description.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty() && adapterPosition == exercises.size - 1) {
                        addExercise(Exercise())
                    }
                }
            })
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.newusers_ui_4_step_str_hip_recycler, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.description.setText(exercise.description)
        // Configurar el botón u otros elementos aquí si es necesario
    }

    override fun getItemCount() = exercises.size

    fun addExercise(exercise: Exercise) {
        exercises.add(exercise)
        notifyItemInserted(exercises.size - 1)
    }
}
