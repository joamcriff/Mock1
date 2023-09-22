package com.example.mock1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mock1.View.SplashFragment
import com.example.mock1.databinding.ActivitySecondBinding
import com.example.mock1.view.Login
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SecondActivity : AppCompatActivity() {
    lateinit var auth : FirebaseAuth

    private lateinit var binding: ActivitySecondBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = FirebaseAuth.getInstance()

        val user = Firebase.auth.currentUser
        if (user == null) {
            val intent = Intent(this@SecondActivity, Login::class.java)
            startActivity(intent)
        } else {
            println("hihi")
        }

        binding.start.setOnClickListener {
            onClick1()
        }

        binding.signOut.setOnClickListener {
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