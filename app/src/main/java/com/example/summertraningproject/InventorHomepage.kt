package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InventorHomepage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventor_homepage)

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {
            val intent = Intent(this,MainPage::class.java)
            startActivity(intent)
        }
    }
}