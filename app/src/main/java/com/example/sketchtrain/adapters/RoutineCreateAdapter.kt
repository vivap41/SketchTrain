package com.example.sketchtrain.adapters

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Routine
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class RoutineCreateAdapter(
    val routineList: MutableList<Routine>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val ITEM_VIEW_TYPE = 0
    private val FOOTER_VIEW_TYPE = 1
    private lateinit var recyclerView: RecyclerView

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextInputEditText = itemView.findViewById(R.id.descEt)
        val button: AppCompatButton = itemView.findViewById(R.id.btnEx)

        init {
            button.setOnClickListener {
                val descriptionText = description.text.toString().trim()
                listener.onClick(descriptionText, adapterPosition, routineList[adapterPosition])
            }

            description.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    routineList[adapterPosition].description = s.toString().trim()
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }
    }

    inner class FooterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addRoutineButton: AppCompatButton = itemView.findViewById(R.id.btAddRoutine)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == FOOTER_VIEW_TYPE) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_routine_create_footer, parent, false)
            FooterViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.rv_routine_create_item, parent, false)
            RoutineViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is RoutineViewHolder) {
            val routine = routineList[position]
            holder.description.setText(routine.description)
        } else if (holder is FooterViewHolder) {
            holder.addRoutineButton.setOnClickListener {
                updateAllRoutines()
                if (routineList.isNotEmpty()) {
                    val lastRoutine = routineList.lastOrNull()
                    val lastDescription = lastRoutine?.description?.trim()

                    if (!lastDescription.isNullOrEmpty() && routineList.none { it.description == lastDescription }) {
                        val newRoutine = Routine(
                            idRoutine = UUID.randomUUID().toString(),
                            description = lastDescription
                        )
                        addRoutine(newRoutine)
                    }
                }

                addRoutine(Routine(idRoutine = UUID.randomUUID().toString(), description = ""))
            }
        }
    }

    override fun getItemCount() = routineList.size + 1

    override fun getItemViewType(position: Int): Int {
        return if (position == routineList.size) FOOTER_VIEW_TYPE else ITEM_VIEW_TYPE
    }

    fun addRoutine(routine: Routine) {
        this.routineList.add(routine)
        notifyItemInserted(this.routineList.size - 1)
    }

    fun updateAllRoutines() {
        for (i in 0 until routineList.size) {
            val holder = recyclerView.findViewHolderForAdapterPosition(i) as? RoutineViewHolder
            holder?.let {
                val text = it.description.text.toString().trim()
                routineList[i].description = text
            }
        }
    }

    interface OnItemClickListener {
        fun onClick(descriptionText: String, position: Int, routine: Routine)
    }
}
