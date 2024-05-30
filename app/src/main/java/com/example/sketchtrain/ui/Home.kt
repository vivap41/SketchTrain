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
import com.example.sketchtrain.dataclasses.Asignation
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.dataclasses.Sets
import com.example.sketchtrain.dataclasses.Training
import com.example.sketchtrain.other.FirebaseManager
import com.example.sketchtrain.other.IntentExtras
import com.example.sketchtrain.ui.record.RoutineWorkout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID

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

        firestore.collection("Training").whereEqualTo("idUser", fireauth.uid.toString()).get().addOnSuccessListener {
            trainingList.clear()
            for (document in it) {
                val routineList = document.get("routineList") as List<HashMap<String, Any>>
                val routines = routineList.map { routineMap ->
                    val exerciseList = routineMap["exerciseList"] as List<HashMap<String, Any>>
                    val exercises = exerciseList.map { exerciseMap ->
                        Exercise(
                            idExercise = exerciseMap["idExercise"].toString(),
                            name = exerciseMap["name"].toString(),
                            maxWeight1Rep = exerciseMap["maxWeight1Rep"] as Number,
                            isPower = exerciseMap["power"] as Boolean,
                            setsList = exerciseMap["setsList"] as MutableList<Sets>,
                            maxWeight = exerciseMap["maxWeight"] as Double,
                            maxReps = exerciseMap["maxReps"] as Number
                        )
                    }

                    Routine(
                        idRoutine = routineMap["idRoutine"].toString(),
                        description = routineMap["description"].toString(),
                        exerciseList = exercises.toMutableList(),
                        idTraining = routineMap["idTraining"].toString()
                    )
                }

                val t = Training(
                    idTraining = document.id,
                    description = document.getString("description").toString(),
                    type = document.getString("type").toString(),
                    date = document.getString("date").toString(),
                    routineList = routines as MutableList<Routine>,
                    idUser = document.getString("idUser").toString()
                )
                trainingList.add(t)
            }
            adapter.notifyDataSetChanged()
        }


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
            val getDo = it.getStringExtra(intEx.DO)
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
                if (getDo == "update") {
                    routine.forEach {r ->
                        r.exerciseList.forEach{e ->
                            firestore.collection("Asignation")
                                .whereEqualTo("idExercise", e.idExercise)
                                .whereEqualTo("idRoutine", r.idRoutine)
                                .get().addOnSuccessListener {
                                for (document in it) {
                                    val idAsig = document.getString("idAsignation")!!
                                    firestore.collection("Asignation").document(idAsig).update("setsList", e.setsList)
                                }
                            }
                        }

                    }
                    adapter.updateTraining(training)
                } else if (getDo == "add") {
                    routine.forEach {r ->
                        r.idTraining = idTraining
                        firestore.collection("Routine").document(r.idRoutine +"").set(r)

                        r.exerciseList.forEach{e ->
                            val idAsig = UUID.randomUUID().toString()
                            val asig = Asignation(idRoutine = r.idRoutine, idExercise = e.idExercise, idAsignation = idAsig, setsList = mutableListOf())
                            firestore.collection("Asignation").document(idAsig).set(asig)
                        }

                    }
                    firestore.collection("Training").document(idTraining).set(training)
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
            putExtra(intEx.ROUTINE_LIST, ArrayList(training.routineList) )
            putExtra(intEx.TRAINING_DESCRIPTION, training.description)
        }
        resultLauncher.launch(intent)
    }
}
