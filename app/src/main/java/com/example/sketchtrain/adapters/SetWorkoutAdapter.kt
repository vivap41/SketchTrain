package com.example.sketchtrain.adapters

import android.content.Context
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatButton
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Sets
import com.google.android.material.textfield.TextInputEditText

class SetWorkoutAdapter(
    var setList: MutableList<Sets>,
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

            holder.weight.addTextChangedListener { text ->
                val weight = text.toString().toIntOrNull() ?: 0
                setList[position].weight = weight
            }
            holder.reps.addTextChangedListener { text ->
                val reps = text.toString().toIntOrNull() ?: 0
                setList[position].reps = reps
            }
            holder.reps.setOnEditorActionListener { v, actionId, event ->
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.keyCode == KeyEvent.KEYCODE_ENTER)) {
                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                    true
                } else {
                    false
                }
            }

            holder.itemView.setOnClickListener {
                listener.onClick(set.weight, set.reps, position, set)
            }
        } else if (holder is FooterViewHolder) {
            holder.addSetButton.setOnClickListener {
                val newSet = Sets(
                    number = counter,
                    weight = 0,
                    reps = 0
                )
                setList.add(newSet)
                counter++
                notifyItemInserted(setList.size - 1)
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
                val weightText = it.weight.text.toString().toIntOrNull() ?: 0
                val repsText = it.reps.text.toString().toIntOrNull() ?: 0
                setList[i].weight = weightText
                setList[i].reps = repsText
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(weightText: Int, repsText: Int, position: Int, set: Sets)
    }
}
