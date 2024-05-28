package com.example.sketchtrain.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.example.sketchtrain.databinding.ActivitySignInBinding
import com.example.sketchtrain.ui.Aaaaa
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private var lastPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        binding.tvNewAcc.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
        binding.btnConn.setOnClickListener {
            val email = binding.emailEt.text.toString()
            val pass = binding.passET.text.toString()

            if (email.isNotEmpty() && pass.isNotEmpty()) {

                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, Aaaaa::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()

            }
        }

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (System.currentTimeMillis() - lastPressedTime < 2000) {
                    finishAffinity()
                } else {
//                    Toast.makeText(applicationContext, "Press back again to exit", Toast.LENGTH_SHORT).show()
                    lastPressedTime = System.currentTimeMillis()
                }
            }
        })
    }

    override fun onStart() {
        super.onStart()

        if (firebaseAuth.currentUser != null) {
            val intent = Intent(this, Aaaaa::class.java)
            startActivity(intent)
        }
    }
}