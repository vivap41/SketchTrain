package com.example.sketchtrain.ui.creation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.UiTrainingHiperNameBinding
import com.example.sketchtrain.other.IntentExtras
import java.time.LocalDate
import java.util.UUID


class TrainingHiperName : AppCompatActivity() {
    private lateinit var binding: UiTrainingHiperNameBinding
    private lateinit var trainDescription: String
    private val intEx = IntentExtras

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UiTrainingHiperNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trainingType = intent.getStringExtra(intEx.TRAINING_TYPE) ?: "hypertrophy"

        binding.btnNext.setOnClickListener {
            trainDescription = binding.trainEt.text.toString()
            if (trainDescription.isEmpty()) {
                Toast.makeText(this, "Please enter a train description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                val intent = Intent(this, RoutineCreate::class.java).apply {
                    putExtra(intEx.TRAINING_ID, UUID.randomUUID().toString())
                    putExtra(intEx.TRAINING_DATE, LocalDate.now().toString())
                    putExtra(intEx.TRAINING_DESCRIPTION, trainDescription)
                    putExtra(intEx.TRAINING_TYPE, trainingType)

                }
                startActivity(intent)
            }
        }


    }


}
