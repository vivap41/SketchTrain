package com.example.sketchtrain.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sketchtrain.adapters.HomeActivityAdapter
import com.example.sketchtrain.databinding.UiHomeBinding
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.dataclasses.Training
import com.example.sketchtrain.ui.record.RoutineAct
import java.time.LocalDate
import java.util.UUID


class Home : Fragment(), HomeActivityAdapter.OnItemClickListener {
    private lateinit var adapter: HomeActivityAdapter
    private val trainingList: MutableList<Training> = mutableListOf()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private var _binding: UiHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        _binding = UiHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = HomeActivityAdapter(trainingList, this) { training ->

        }
        binding.rvTrain.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrain.adapter = adapter

        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                handleIncomingIntent(result.data)
            }
        }

        handleIncomingIntent(activity?.intent)
    }

    private fun handleIncomingIntent(intent: Intent?) {
        intent?.let {
            val routine = it.getSerializableExtra("ROUTINE_LIST") as? ArrayList<Routine>
            val trainingType = it.getStringExtra("TRAINING_TYPE")
            val trainingDesc = it.getStringExtra("TRAIN_DESCRIPTION")
            if (routine != null && trainingType != null && trainingDesc != null) {
                val newTraining = Training(
                    idTraining = UUID.randomUUID().toString(),
                    description = trainingDesc,
                    type = trainingType,
                    date = LocalDate.now().toString(),
                    routineList = routine.toMutableList()
                )
                adapter.addTraining(newTraining)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(training: Training) {
        val intent = Intent(requireContext(), RoutineAct::class.java).apply {
            putExtra("TRAINING_ID", training.idTraining)
            putExtra("ROUTINE_LIST", training.routineList as java.io.Serializable)
        }
        resultLauncher.launch(intent)
    }
}
