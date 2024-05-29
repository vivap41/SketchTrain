package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Exercise

class ExerciseAddAdapter(
    private val exercises: MutableList<Exercise>,
    private val onItemLongClick: (Int) -> Unit
) : RecyclerView.Adapter<ExerciseAddAdapter.ExerciseViewHolder>() {
    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val exerciseName: TextView = itemView.findViewById(R.id.tvExercise)
        init {
            itemView.setOnLongClickListener {
                onItemLongClick(adapterPosition)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_exercise_add, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exercises[position]
        holder.exerciseName.setText(exercise.name)
    }

    override fun getItemCount() = exercises.size
}

