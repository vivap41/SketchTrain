package com.example.sketchtrain.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sketchtrain.adapters.HomeActivityAdapter
import com.example.sketchtrain.databinding.UiHomeBinding
import com.example.sketchtrain.dataclasses.Asignation
import com.example.sketchtrain.dataclasses.Exercise
import com.example.sketchtrain.dataclasses.Progress
import com.example.sketchtrain.dataclasses.Routine
import com.example.sketchtrain.dataclasses.Sets
import com.example.sketchtrain.dataclasses.Training
import com.example.sketchtrain.dataclasses.Users
import com.example.sketchtrain.other.FirebaseManager
import com.example.sketchtrain.other.IntentExtras
import com.example.sketchtrain.ui.record.RoutineWorkout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.time.LocalDate
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
                            isPower = exerciseMap["power"] as Boolean
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
            val assignments = it.getSerializableExtra("ASSIGNMENTS") as? ArrayList<Asignation>
            val idTraining = it.getStringExtra(intEx.TRAINING_ID).toString()
            val trainingDate = it.getStringExtra(intEx.TRAINING_DATE).toString()
            val trainingType = it.getStringExtra(intEx.TRAINING_TYPE)
            val trainingDesc = it.getStringExtra(intEx.TRAINING_DESCRIPTION)
            val sets = it.getSerializableExtra(intEx.SET_LIST) as? ArrayList<Sets>

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
                    updateAssignments(assignments)
                    adapter.updateTraining(training)
                } else if (getDo == "add") {
                    routine.forEach { r ->
                        r.idTraining = idTraining
                        firestore.collection("Routine").document(r.idRoutine).set(r)

                        r.exerciseList.forEach { e ->
                            val idAsig = UUID.randomUUID().toString()
                            val asig = Asignation(idRoutine = r.idRoutine, idExercise = e.idExercise, idAsignation = idAsig, setsList = sets)
                            firestore.collection("Asignation").document(idAsig).set(asig)
                        }
                    }
                    firestore.collection("Training").document(idTraining).set(training)
                    adapter.addTraining(training)
                }
            }
        }
    }

    private fun updateAssignments(assignments: ArrayList<Asignation>?) {
        assignments?.forEach { assignment ->
            val maxWeight = assignment.setsList?.maxByOrNull { it.weight }?.weight

            firestore.collection("Asignation")
                .whereEqualTo("idExercise", assignment.idExercise)
                .whereEqualTo("idRoutine", assignment.idRoutine)
                .get()
                .addOnSuccessListener { assignmentQuerySnapshot ->
                    for (assignmentDocument in assignmentQuerySnapshot) {
                        val idAsig = assignmentDocument.getString("idAsignation")!!
                        val map = hashMapOf(
                            "maxWeight" to maxWeight?.toInt(),
                            "setsList" to assignment.setsList
                        )
                        firestore.collection("Asignation")
                            .document(idAsig)
                            .update(map)
                            .addOnSuccessListener {
                            Log.d("TAG", "FUNCIONA GENTEEEEE")
                                createProgressRecord(assignment, maxWeight!!.toInt())
                            }
                    }
                }
        }
    }
    private fun createProgressRecord(assignment: Asignation, maxWeight: Int) {
        val currentDate = LocalDate.now().toString()
        val exerciseId = assignment.idExercise
        Log.d("TAG", exerciseId +"XD")
        val userId = fireauth.currentUser?.uid.toString()

        firestore.collection("Exercises").
            whereEqualTo("idExercise", exerciseId).get()
            .addOnSuccessListener {
                for(doc in it){
                    val exerciseName = doc.getString("name") ?: "Unknown Exercise"
                    val maxWeight1Rep = (assignment.maxWeight1Rep)

                    val progress = Progress(
                        idProgress = UUID.randomUUID().toString(),
                        nameExercise = exerciseName,
                        maxWeight = maxWeight,
                        maxWeight1Rep = maxWeight1Rep,
                        date = currentDate,
                        idExercise = exerciseId,
                        idUser = userId
                    )

                    firestore.collection("Progress").document(progress.idProgress + UUID.randomUUID().toString()).set(progress)
                }
            }
            .addOnFailureListener { exception ->
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
            putExtra(intEx.ROUTINE_LIST, ArrayList(training.routineList))
            putExtra(intEx.TRAINING_DESCRIPTION, training.description)
            putExtra("ASSIGNMENTS", ArrayList<Asignation>())

        }
        resultLauncher.launch(intent)
    }

    override fun onLongItemClick(training: Training) {
        firestore.collection("Training").document(training.idTraining).delete()
    }
}
