package com.example.sketchtrain.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.sketchtrain.databinding.UiProgressBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.sketchtrain.adapters.HomeActivityAdapter
import com.example.sketchtrain.adapters.ProgressActivityAdapter
import com.example.sketchtrain.dataclasses.Progress
import com.example.sketchtrain.dataclasses.ProgressVisualizer
import com.example.sketchtrain.dataclasses.Training
import com.example.sketchtrain.other.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProgressFragment : Fragment() {
    private var _binding: UiProgressBinding? = null
    private var progList: MutableList<ProgressVisualizer> = mutableListOf()
    private val binding get() = _binding!!
    private lateinit var adapter: ProgressActivityAdapter
    private lateinit var firestore: FirebaseFirestore
    private lateinit var fireauth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = UiProgressBinding.inflate(inflater, container, false)
        return binding.root

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firestore = FirebaseManager().firestore
        fireauth = FirebaseManager().fireauth

        progList.clear()

        firestore.collection("Exercises")
            .get().addOnSuccessListener {
                for(doc in it){
                    val nameExercise = doc.getString("name")!!
                    val dates: MutableList<String> = mutableListOf()
                    val maxWeights: MutableList<Int> = mutableListOf()
                    firestore.collection("Progress")
                        .whereEqualTo("idUser", fireauth.uid)
                        .whereEqualTo("nameExercise", nameExercise)
                        .get().addOnSuccessListener {docus ->
                            for(document in docus){
                                val maxWeight = (document.getLong("maxWeight"))!!.toInt()
                                val date = document.getString("date")!!
                                dates.add(date)
                                maxWeights.add(maxWeight)
                            }
                            if(!docus.isEmpty){
                                progList.add(ProgressVisualizer(nameExercise = nameExercise, maxWeights = maxWeights, dates = dates))
                                adapter.notifyDataSetChanged()

                            }

                        }

                }

            }

        adapter = ProgressActivityAdapter(progList) { progress ->
            val intent = Intent(requireActivity(), VisualizeProgress::class.java)
            intent.putExtra("EXERCISE_NAME", progress.nameExercise)
            intent.putExtra("MAX_WEIGHTS", ArrayList(progress.maxWeights))
            intent.putExtra("DATES", ArrayList(progress.dates))
            startActivity(intent)
        }
        binding.rvProgress.layoutManager = LinearLayoutManager(requireContext())
        binding.rvProgress.adapter = adapter
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}