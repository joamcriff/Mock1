package com.example.mock1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    lateinit var editTextEmail : EditText
    lateinit var editTextPassword : EditText
    lateinit var btn : Button
    lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = FirebaseAuth.getInstance()
        editTextEmail = findViewById(R.id.email_sign_up)
        editTextPassword = findViewById(R.id.password_sign_up)
        btn = findViewById(R.id.btn_sign_up)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        btn.setOnClickListener {
            onClick()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onClick() {
        val email : String = editTextEmail.text.toString()
        val password : String = editTextPassword.text.toString()

        if(TextUtils.isEmpty(email)) {
            Toast.makeText(this@Register, "Enter email", Toast.LENGTH_SHORT).show()
            return
        }

        if(TextUtils.isEmpty(password)) {
            Toast.makeText(this@Register, "Enter password", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@Register,
                        "Success",
                        Toast.LENGTH_SHORT,
                    ).show()
                } else {
                    Toast.makeText(
                        this@Register,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}