package com.example.sketchtrain.adapters

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.ui.newusers.Step5StrHiperExercise
import com.google.android.material.textfield.TextInputEditText

class Step4StrHiperRoutineAdapter(private val routine: MutableList<Routine>) : RecyclerView.Adapter<Step4StrHiperRoutineAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextInputEditText = itemView.findViewById(R.id.descEt)
        val button: AppCompatButton = itemView.findViewById(R.id.btnEx)

        init {
            description.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s != null && s.isNotEmpty() && adapterPosition == routine.size - 1) {
                        addRoutine(Routine())
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
        val routine = routine[position]
        holder.description.setText(routine.description)
        holder.button.setOnClickListener {
            val context = holder.itemView.context
            val descriptionText = holder.description.text.toString()
            if (descriptionText.isEmpty()) {
                Toast.makeText(context, "Please enter a description", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(context, Step5StrHiperExercise::class.java).apply {
                    putExtra("EXERCISE_DESCRIPTION", descriptionText)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = routine.size

    fun addRoutine(routine: Routine) {
        this.routine.add(routine)
        notifyItemInserted(this.routine.size - 1)
    }
}
