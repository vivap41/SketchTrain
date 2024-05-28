package com.example.sketchtrain.adapters

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Routine
import com.google.android.material.textfield.TextInputEditText
import java.util.UUID

class Step4StrHiperRoutineAdapter(val routineList: MutableList<Routine>,
                                  private val listener: OnItemClickListener
) : RecyclerView.Adapter<Step4StrHiperRoutineAdapter.RoutineViewHolder>() {
    inner class RoutineViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextInputEditText = itemView.findViewById(R.id.descEt)
        val button: AppCompatButton = itemView.findViewById(R.id.btnEx)
        var hasEntered: Boolean = false

        init {
            // Establecer fondo rojo inicialmente si no se ha pulsado "Enter"
            if (!hasEntered) {
                itemView.setBackgroundColor(Color.parseColor("#3BFF0000"))
            }

            description.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) { }
            })

            description.setOnEditorActionListener(object : TextView.OnEditorActionListener {
                override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        val text = description.text.toString().trim()
                        if (text.isNotEmpty() && adapterPosition == routineList.size - 1) {
                            // Verifica si la rutina ya ha sido añadida
                            if (routineList.none { it.description == text }) {
                                val newRoutine = Routine(
                                    idRoutine = UUID.randomUUID().toString(),
                                    description = text
                                )
                                addRoutine(newRoutine)

                                // Desactivar la edición pero mantener el texto
                                description.isEnabled = false
                                description.clearFocus()

                                // Ocultar el teclado
                                val imm = itemView.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(description.windowToken, 0)

                                // Marcar que se ha pulsado "Enter" y cambiar el fondo a blanco
                                hasEntered = true
                                itemView.setBackgroundColor(Color.WHITE)
                            }
                        }
                        return true
                    }
                    return false
                }
            })
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.recycler_crea_4_step_str_hip, parent, false)
        return RoutineViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RoutineViewHolder, position: Int) {
        val routine = routineList[position]
        holder.description.setText(routine.description)
        holder.button.setOnClickListener {
            val descriptionText = holder.description.text.toString()
            listener.onClick(descriptionText, position)
        }
    }

    override fun getItemCount() = routineList.size

    fun addRoutine(routine: Routine) {
        this.routineList.add(routine)
        notifyItemInserted(this.routineList.size - 1)
    }

    interface OnItemClickListener {
        fun onClick(descriptionText: String, position: Int)
    }
}
