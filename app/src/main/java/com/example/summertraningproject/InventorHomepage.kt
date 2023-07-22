package com.example.summertraningproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

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