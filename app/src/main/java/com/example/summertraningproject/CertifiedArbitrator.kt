package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class CertifiedArbitrator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certified_arbitrator)

        val inventionsList = findViewById<ImageButton>(R.id.imageButton2)

        inventionsList.setOnClickListener {
            val intent = Intent(this,InventionsRequests::class.java)
            startActivity(intent)
        }
    }
}