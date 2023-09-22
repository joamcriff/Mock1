package com.example.mock1.view.authentication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.example.mock1.databinding.ActivityForgotPasswordBinding
import com.example.mock1.SecondActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPassword : AppCompatActivity() {

    lateinit var auth : FirebaseAuth
    private lateinit var binding: ActivityForgotPasswordBinding
    lateinit var strEmail : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        binding.btnRS.setOnClickListener {
            onclick()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun onclick() {
        strEmail = binding.emailRS.text.toString().trim()

       if(!TextUtils.isEmpty(strEmail)) {
            resetPassword()
       } else {
           binding.emailRS.error = "Email field can't be empty"
       }
    }

    private fun resetPassword() {
        auth.sendPasswordResetEmail(strEmail)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@ForgotPassword, "Reset Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@ForgotPassword, SecondActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@ForgotPassword,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}