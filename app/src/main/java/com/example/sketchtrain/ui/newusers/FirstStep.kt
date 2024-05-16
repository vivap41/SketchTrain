package com.example.sketchtrain.ui.newusers
import android.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.sketchtrain.R
import com.example.sketchtrain.databinding.FirstStepBinding
class  FirstStep : Fragment() {
    private var _binding: FirstStepBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: android.view.LayoutInflater,
        container: android.view.ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): android.view.View {
        _binding = FirstStepBinding.inflate(inflater, container, false)
        val root: android.view.View = binding.root

        binding.infoStrength.setOnClickListener {
            // Create and show the dialog.
            val dialogView = layoutInflater.inflate(R.layout.strength_dialog_popup, null)
            val dialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .create()
            dialog.show()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}