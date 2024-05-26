package com.example.sketchtrain.ui.creation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.NewusersUi3StepStrHipBinding


class Step3Hiper : AppCompatActivity() {
    private lateinit var binding: NewusersUi3StepStrHipBinding
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var trainDescription: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NewusersUi3StepStrHipBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trainingType = intent.getStringExtra("TRAINING_TYPE") ?: "hypertrophy"

        binding.btnNext.setOnClickListener {
            trainDescription = binding.trainEt.text.toString()
            if (trainDescription.isEmpty()) {
                Toast.makeText(this, "Please enter a train description", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else {

                val intent = Intent(this, Step4StrHiperRoutine::class.java).apply {
                    putExtra("TRAIN_DESCRIPTION", trainDescription)
                    intent.putExtra("TRAINING_TYPE", trainingType)

                }
                startActivity(intent)
            }
        }
//        resultLauncher = registerForActivityResult(
//            ActivityResultContracts.StartActivityForResult()
//
//        )
//        { result ->}
//            if (result.resultCode == Activity.RESULT_OK) {
//                var routines = result.data?.getSerializableExtra("ROUTINE_LIST") as ArrayList<Routine>
//                var mutList = mutableListOf<Routine>()
//                mutList.addAll(routines)
//                Training(
//                    idTraining = UUID.randomUUID().toString(),
//                    description = trainDescription,
//                    type = trainingType,
//                    date = LocalDate.now().toString(),
//                    mutableList = routines
//                )
//            }
//        }

    }


}
