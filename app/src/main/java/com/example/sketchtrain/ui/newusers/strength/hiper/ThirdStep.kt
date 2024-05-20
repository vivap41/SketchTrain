package com.example.sketchtrain.ui.newusers.strength.hiper

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.NewusersUi3StepStrHipBinding
import com.example.sketchtrain.ui.newusers.strength.hiper.routine.FourthStep

class ThirdStep : AppCompatActivity() {
    private lateinit var binding: NewusersUi3StepStrHipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewusersUi3StepStrHipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val train = binding.trainEt.text.toString()  // Captura el texto ingresado
            if (train.isEmpty()) {
                Toast.makeText(this, "Please enter a train description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {
                val intent = Intent(this, FourthStep::class.java).apply {
                    putExtra("TRAIN_DESCRIPTION", train)  // Pasa el valor como un extra
                }
                startActivity(intent)
            }

        }
    }
}
