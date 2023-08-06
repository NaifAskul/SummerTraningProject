package com.example.summertraningproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class Survey : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions","Survey", "Confirm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)
    }
}