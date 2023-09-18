package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.mock1.View.SplashFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SecondActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth
    lateinit var button : Button
    lateinit var btn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        auth = FirebaseAuth.getInstance()
        button = findViewById(R.id.sign_out)
        btn = findViewById(R.id.start)

        val user = Firebase.auth.currentUser
        if (user == null) {
            val intent = Intent(this@SecondActivity, Login::class.java)
            startActivity(intent)
        } else {
            println("hihi")
        }

        btn.setOnClickListener {
            onClick1()
        }

        button.setOnClickListener {
            onClick()
        }
    }

    private fun onClick1() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        val fragment = SplashFragment()

        fragmentTransaction.replace(R.id.activity_to_fragment, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun onClick() {
        Firebase.auth.signOut()
        val intent = Intent(this@SecondActivity, Login::class.java)
        startActivity(intent)
    }
}