package com.example.mock1

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.mock1.databinding.ActivityMainBinding
import com.example.mock1.View.LoginFragment

class MainActivity : AppCompatActivity() {

    lateinit var handler : Handler

    private lateinit var binding: ActivityMainBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.img.animate().alpha(0f).duration = 5000
        binding.textM.animate().alpha(0f).duration = 5000

        handler = Handler()

        handler.postDelayed({
            val fragmentManager = supportFragmentManager
            val fragmentTransaction = fragmentManager.beginTransaction()

            val fragment = LoginFragment()

            fragmentTransaction.replace(R.id.main_to_log, fragment)
            fragmentTransaction.commit()
        }, 5000)
    }
}