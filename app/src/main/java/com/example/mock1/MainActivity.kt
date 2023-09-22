package com.example.mock1

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import android.widget.TextView
import com.example.mock1.view.Login

class MainActivity : AppCompatActivity() {

    lateinit var handler : Handler
    lateinit var runnable: Runnable
    lateinit var img : ImageView
    lateinit var text : TextView



    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        img = findViewById(R.id.img)
        img.animate().alpha(0f).duration = 5000
        text = findViewById(R.id.text)
        text.animate().alpha(0f).duration = 5000

        handler = Handler()

        handler.postDelayed({
            val dsp = Intent(this@MainActivity, Login::class.java)
            startActivity(dsp)
            finish()
        }, 5000)



    }



}