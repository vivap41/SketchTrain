package com.example.sketchtrain.ui.newusers.strength

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.NewusersUi2StepStrBinding

class SecondStep : AppCompatActivity() {

    private lateinit var binding: NewusersUi2StepStrBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewusersUi2StepStrBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEx.setOnClickListener {
            val intent = Intent(this, ThirdStep::class.java)
            startActivity(intent)
        }
    }
}
