package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.ProgressVisualizer


class ProgressActivityAdapter(
    private val progressList: List<ProgressVisualizer>,
    private val onClick: (ProgressVisualizer) -> Unit
) : RecyclerView.Adapter<ProgressActivityAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.ui_progress_activity_recycler, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val progress = progressList[position]
        holder.bind(progress)
        holder.itemView.setOnClickListener {
            onClick(progress)
        }
    }

    override fun getItemCount(): Int = progressList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvTrainDesc: TextView = itemView.findViewById(R.id.tvTrainDesc)

        fun bind(progress: ProgressVisualizer) {
            tvTrainDesc.text = progress.nameExercise
        }
    }
}
