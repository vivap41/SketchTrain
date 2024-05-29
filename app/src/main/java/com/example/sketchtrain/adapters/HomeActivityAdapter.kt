package com.example.sketchtrain.adapters

import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.example.sketchtrain.R
import com.example.sketchtrain.dataclasses.Training
import com.example.sketchtrain.objects.IntentExtras
import com.example.sketchtrain.ui.MainActivity
import com.example.sketchtrain.ui.creation.TrainingHiperName

private const val TYPE_ITEM = 1
private const val TYPE_FOOTER = 2
private val intEx = IntentExtras

class HomeActivityAdapter(
    private var trainingList: MutableList<Training>,
    private val listener: OnItemClickListener,
    private val onTrainingClick: (Training) -> Unit

) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class TrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val description: TextView = itemView.findViewById(R.id.tvTrainDesc)
        val date: TextView = itemView.findViewById(R.id.tvDate)
        init {
            itemView.setOnClickListener {
                val training = trainingList[adapterPosition]
                onTrainingClick(training)
                listener.onItemClick(training)
            }
        }


    }

    inner class NewTrainingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val btnAdd: AppCompatButton = itemView.findViewById(R.id.btNew)

    }

    override fun getItemViewType(position: Int): Int {
        return if (position == trainingList.size) TYPE_FOOTER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ITEM) {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.ui_home_activity_recycler, parent, false)
            TrainingViewHolder(itemView)
        } else {
            val addView = LayoutInflater.from(parent.context)
                .inflate(R.layout.ui_home_activity_footer, parent, false)
            NewTrainingViewHolder(addView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TrainingViewHolder) {
            val train = trainingList[position]
            holder.description.text = train.description
            holder.date.text = train.date
        } else if (holder is NewTrainingViewHolder) {
            holder.btnAdd.setOnClickListener {
                val types = arrayOf("Hypertrophy", "Powerlifting")
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Select Training Type")
                    .setItems(types) { _, which ->
                        val selectedType = types[which]
                        val context = holder.itemView.context
                        if (context is MainActivity) {
                            val intent = Intent(context, TrainingHiperName::class.java)
                            intent.putExtra(intEx.TRAINING_TYPE, selectedType)
                            context.resultLauncher.launch(intent)
                        }
                    }
                    .show()
            }
        }

    }

    override fun getItemCount(): Int {
        return trainingList.size + 1
    }

    fun addTraining(training: Training) {
        trainingList.add(training)
        notifyItemInserted(trainingList.size)
    }
    interface OnItemClickListener{
        fun onItemClick(training: Training)
    }
}
