package com.example.sketchtrain.ui.creation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.UiCrea2StepStrBinding

class Step2Str : AppCompatActivity() {

    private lateinit var binding: UiCrea2StepStrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UiCrea2StepStrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnHiper.setOnClickListener {
            val intent = Intent(this, Step3Hiper::class.java)
            intent.putExtra("TRAINING_TYPE", "hypertrophy")
            startActivity(intent)
        }
    }
}
