package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Routine

class RoutineWorkoutAdapter(
     val routineList: MutableList<Routine>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RoutineWorkoutAdapter.RoutineViewHolder>() {

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.routDesc)
        val button: AppCompatButton = itemView.findViewById(R.id.btRecordWorkout)

        init {
            button.setOnClickListener {
                listener.onClick(routineList[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_routine_workout_item, parent, false)
        return RoutineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routineList[position]
        holder.description.text = routine.description
    }

    override fun getItemCount() = routineList.size
//
//    fun addRoutine(routine: Routine) {
//        this.routineList.add(routine)
//        notifyItemInserted(this.routineList.size - 1)
//    }

    interface OnItemClickListener {
        fun onClick(routine: Routine)
    }
}
