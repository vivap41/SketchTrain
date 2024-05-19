package com.example.sketchtrain.ui.newusers.strength

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.R
import com.example.sketchtrain.databinding.NewusersUi3StepStrHipBinding
import com.example.sketchtrain.ui.newusers.strength.routine.FourthStep

class ThirdStep : AppCompatActivity() {
    private lateinit var binding: NewusersUi3StepStrHipBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewusersUi3StepStrHipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            val intent = Intent(this, FourthStep::class.java)
            startActivity(intent)
        }
    }
}
