package com.example.sketchtrain.ui.creation
import android.app.AlertDialog
import android.content.Intent
import androidx.fragment.app.Fragment
import com.example.sketchtrain.R
import com.example.sketchtrain.databinding.UiCreation1StepBinding

class Step1 : Fragment() {
    private var _binding: UiCreation1StepBinding? = null
    private val binding get() = _binding!!

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
            val dialogView = layoutInflater.inflate(R.layout.newusers_ui_dialog_popup_strength, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()

            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        binding.infoEndurance.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.newusers_ui_dialog_popup_endurance, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()

            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        binding.infoHIIT.setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.newusers_ui_dialog_popup_hiit, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()
            dialogView.findViewById<android.widget.Button>(R.id.btnOK).setOnClickListener {
                dialog.dismiss()
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
