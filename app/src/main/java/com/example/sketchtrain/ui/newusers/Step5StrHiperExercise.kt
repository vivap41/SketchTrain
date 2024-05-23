package com.example.sketchtrain.ui.newusers

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.sketchtrain.R

class Step5StrHiperExercise : AppCompatActivity() {
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.newusers_ui_5_step_str_hip_exercises)

        val addEx : AppCompatButton = findViewById(R.id.btnAdd)
        addEx.setOnClickListener{
            val intent = Intent(this, Step5StrHiperExerciseList::class.java)
            startActivity(intent)
        }
        val description = intent.getStringExtra("ROUTINE_DESCRIPTION")
        val tvRout: TextView = findViewById(R.id.tvTitle)
        tvRout.text = "$description Routine"


    }

}

