package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Forgot_Password : AppCompatActivity() {

    lateinit var btn : Button
    lateinit var emailText : EditText
    lateinit var auth : FirebaseAuth
    lateinit var strEmail : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btn = findViewById(R.id.btn_reset)
        emailText = findViewById(R.id.email_reset)
        auth = FirebaseAuth.getInstance()

        btn.setOnClickListener {
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
        strEmail = emailText.text.toString().trim()

       if(!TextUtils.isEmpty(strEmail)) {
            resetPassword()
       } else {
           emailText.error = "Email field can't be empty"
       }
    }

    private fun resetPassword() {
        auth.sendPasswordResetEmail(strEmail)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this@Forgot_Password, "Reset Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this@Forgot_Password, SecondActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@Forgot_Password,
                        "Authentication failed.",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
            }
    }
}