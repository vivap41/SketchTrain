package com.example.sketchtrain.login
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets.Side
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.example.sketchtrain.R
import com.example.sketchtrain.SideMenu
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*
import kotlin.properties.Delegates

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.log_in)

        auth = FirebaseAuth.getInstance()
val btn_login : Button = findViewById(R.id.btnConn)
        val et_email : EditText = findViewById(R.id.editTextTextEmailAddress)
        val et_password : EditText = findViewById(R.id.editTextPassword)

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Toast.makeText(baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(this, "Please enter email and password.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            startActivity(Intent(this, SideMenu::class.java))
            finish()
        } else {
            Toast.makeText(this, "Please log in to continue.", Toast.LENGTH_SHORT).show()
        }
    }
}
