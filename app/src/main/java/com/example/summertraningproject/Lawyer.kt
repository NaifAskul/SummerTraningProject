package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Lawyer : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lawyer)

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {


            val intent = Intent(this@Lawyer, MainActivity::class.java)
            startActivity(intent)


        }

    }
}