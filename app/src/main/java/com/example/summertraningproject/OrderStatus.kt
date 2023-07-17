package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class OrderStatus : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        val newDis = findViewById<TextView>(R.id.textView8)

        newDis.setOnClickListener{
            Toast.makeText(this," i forgot my password ", Toast.LENGTH_SHORT).show()
        }

        val back = findViewById<Button>(R.id.button1)
        back.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }
    }
}