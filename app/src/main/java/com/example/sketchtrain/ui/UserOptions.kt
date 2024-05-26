package com.example.sketchtrain.ui

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.sketchtrain.databinding.UiUserOptionsBinding
import com.example.sketchtrain.ui.login.SignInActivity
import com.google.firebase.auth.FirebaseAuth


class UserOptions : Fragment() {
    private var _binding: UiUserOptionsBinding? = null
    private val binding get() = _binding!!
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        firebaseAuth = FirebaseAuth.getInstance()

        _binding = UiUserOptionsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Log Out Button
        binding.logOut.setOnClickListener {
            firebaseAuth.signOut()
            Toast.makeText(requireContext(), "You have been logged out", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireContext(), SignInActivity::class.java)
            startActivity(intent)
        }

        //Delete Account Button
        binding.deleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Delete Account")
            builder.setMessage("Are you sure you want to delete your account?")
            builder.setPositiveButton("Yes") { dialog, which ->
                firebaseAuth.currentUser?.delete()?.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(requireContext(), "Your account has been deleted", Toast.LENGTH_SHORT).show()
                        val intent = Intent(requireContext(), SignInActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                    } else {
                        Toast.makeText(requireContext(), "Failed to delete account", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            builder.setNegativeButton("No") { dialog, which ->
                dialog.dismiss()
            }
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}