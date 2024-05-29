package com.example.sketchtrain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Sets
import com.google.android.material.textfield.TextInputEditText

class SetWorkoutAdapter(
    val setList: MutableList<Sets>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE = 0
    private val FOOTER_VIEW_TYPE = 1
    private lateinit var recyclerView: RecyclerView
    private var counter: Int = setList.size + 1

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    inner class SetsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val weight: TextInputEditText = itemView.findViewById(R.id.etWeight)
        val reps: TextInputEditText = itemView.findViewById(R.id.repEt)
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addSetButton: AppCompatButton = itemView.findViewById(R.id.addSets)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_set_workout_footer, parent, false)
            FooterViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_set_workout_item, parent, false)
            SetsViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is SetsViewHolder) {
            val set = setList[position]
            holder.weight.setText(set.weight.toString())
            holder.reps.setText(set.reps.toString())
        } else if (holder is FooterViewHolder) {
            holder.addSetButton.setOnClickListener {
                if (setList.isNotEmpty()) {
                    val lastSet = setList.last()
                    val weightText = lastSet.weight
                    val repsText = lastSet.reps

                    if (weightText != null && repsText != null) {
                        val newSet = Sets(
                            number = counter,
                            weight = weightText,
                            reps = repsText
                        )
                        setList.add(newSet)
                        counter++
                        notifyItemInserted(setList.size - 1)
                    }
                } else {
                    // Manejar el caso cuando setList está vacío
                    val newSet = Sets(
                        number = counter,
                        weight = 0.0, // o algún valor predeterminado
                        reps = 0 // o algún valor predeterminado
                    )
                    setList.add(newSet)
                    counter++
                    notifyItemInserted(setList.size - 1)
                }
            }
        }
    }

    override fun getItemCount() = setList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == setList.size) FOOTER_VIEW_TYPE else ITEM_VIEW_TYPE
    }


    fun updateAllSets() {
        for (i in 0 until setList.size) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? SetsViewHolder
            holder?.let {
                val weightText = it.weight.text.toString().toDoubleOrNull() ?: 0.0
                val repsText = it.reps.text.toString().toIntOrNull() ?: 0
                setList[i].weight = weightText
                setList[i].reps = repsText
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(weightText: Double, repsText: Int, position: Int, set: Sets)
    }
}
