package com.example.sketchtrain.ui.creation
import android.app.AlertDialog
import android.content.Intent
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import com.example.sketchtrain.R
import com.example.sketchtrain.databinding.UiCreation1StepBinding
import com.example.sketchtrain.ui.SideMenu

class Step1 : Fragment() {
    private var _binding: UiCreation1StepBinding? = null
    private val binding get() = _binding!!

    private var backPressedTime: Long = 0
    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View {
        _binding = UiCreation1StepBinding.inflate(inflater, container, false)
        val root: android.view.View = binding.root

        // Listeners --> Buttons
        val buttonStr = binding.btnStrength
        buttonStr.setOnClickListener {
        val intent = Intent(requireContext(), Step2Str::class.java)

            startActivity(intent)
        }

        // Listeners --> Dialog
        binding.infoStrength.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.hint_strength, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()

            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        binding.infoEndurance.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.hint_endurance, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()

            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        binding.infoHIIT.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.hint_hiit, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()
            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedTime + 2000 > System.currentTimeMillis()) {
                    showExitDialog()
                } else {
                    Toast.makeText(
                        requireContext(),
                        "Press back again to exit",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                backPressedTime = System.currentTimeMillis()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return root
    }
    private fun showExitDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage("Are you sure you want to skip tutorial?")
            .setPositiveButton("Yes") { dialog, which ->
                (requireActivity() as SideMenu).finish()
            }
            .setNegativeButton("No", null)
            .show()
    }
        override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
