package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Exercise

class ExerciseWorkoutAdapter(
    val exerciseList: MutableList<Exercise>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<ExerciseWorkoutAdapter.ExerciseViewHolder>() {

    inner class ExerciseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: AppCompatButton = itemView.findViewById(R.id.btExercise)

        init {
            name.setOnClickListener {
                val nameText = name.text.toString()
                listener.onClick(nameText, adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciseViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_exercises_workout, parent, false)
        return ExerciseViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ExerciseViewHolder, position: Int) {
        val exercise = exerciseList[position]
        holder.name.text = exercise.name
    }

    override fun getItemCount() = exerciseList.size

    fun addExercise(exercise: Exercise) {
        this.exerciseList.add(exercise)
        notifyItemInserted(this.exerciseList.size - 1)
    }

    interface OnItemClickListener {
        fun onClick(nameText: String, position: Int)
    }
}
