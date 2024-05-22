package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Exercise

private val TYPE_ITEM = 1
private val TYPE_FOOTER = 2

class Step5StrHiperExerciseListAdapter(private var exercises: List<Exercise>, private val onExerciseClick: (Exercise) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.exerciseName)
        init {
            itemView.setOnClickListener {
                onExerciseClick(exercises[adapterPosition])
            }
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val newExercise: TextView = itemView.findViewById(R.id.newExercise)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == exercises.size) TYPE_FOOTER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.newusers_ui_5_step_str_hip_exercises_list_item, parent, false)
            ExerciseViewHolder(itemView)
        } else {
            val footerView = LayoutInflater.from(parent.context).inflate(R.layout.newusers_ui_5_step_str_hip_exercises_list_footer, parent, false) // Reemplaza "footer_layout" con el layout de tu footer
            FooterViewHolder(footerView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ExerciseViewHolder) {
            val exercise = exercises[position]
            holder.exerciseName.text = exercise.name
        } else if (holder is FooterViewHolder) {
            // Configura el footer si es necesario
        }
    }

    override fun getItemCount(): Int {
        return exercises.size + 1 // +1 para el footer
    }

    fun updateList(newExercises: List<Exercise>) {
        exercises = newExercises
        notifyDataSetChanged()
    }
}
