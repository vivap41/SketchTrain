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
import com.example.sketchtrain.other.FirebaseManager
import com.example.sketchtrain.other.IntentExtras
import com.example.sketchtrain.ui.record.RoutineWorkout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class Home : Fragment(), HomeActivityAdapter.OnItemClickListener {
    private lateinit var adapter: HomeActivityAdapter
    private val trainingList: MutableList<Training> = mutableListOf()
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fireauth: FirebaseAuth
    private var _binding: UiHomeBinding? = null
    private val intEx = IntentExtras
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


        firestore = FirebaseManager().firestore
        fireauth = FirebaseManager().fireauth


        adapter = HomeActivityAdapter(trainingList, this) { training -> }
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
            val isUpdate = it.getBooleanExtra(intEx.IS_UPDATE, false)
            val routine = it.getSerializableExtra(intEx.ROUTINE_LIST) as? ArrayList<Routine>
            val idTraining = it.getStringExtra(intEx.TRAINING_ID).toString()
            val trainingDate = it.getStringExtra(intEx.TRAINING_DATE).toString()
            val trainingType = it.getStringExtra(intEx.TRAINING_TYPE)
            val trainingDesc = it.getStringExtra(intEx.TRAINING_DESCRIPTION)

            if (routine != null && trainingType != null && trainingDesc != null) {
                val training = Training(
                    idTraining = idTraining,
                    description = trainingDesc,
                    type = trainingType,
                    date = trainingDate,
                    routineList = routine.toMutableList(),
                    idUser = fireauth.currentUser?.uid.toString()
                )
                if (isUpdate) {
                    adapter.updateTraining(training)
                } else {
                    adapter.addTraining(training)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onItemClick(training: Training) {
        val intent = Intent(requireContext(), RoutineWorkout::class.java).apply {
            putExtra(intEx.TRAINING_ID, training.idTraining)
            putExtra(intEx.TRAINING_TYPE, training.type)
            putExtra(intEx.ROUTINE_LIST, training.routineList as java.io.Serializable)
            putExtra(intEx.TRAINING_DESCRIPTION, training.description)
        }
        resultLauncher.launch(intent)
    }
}
