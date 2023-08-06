package com.example.summertraningproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.annotation.RequiresApi

class InventionDetailsActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invention_details)

        val intent = intent
        val selectedInvention: InventionModel? = intent.getParcelableExtra("invention")

        val b = findViewById<TextView>(R.id.textView5555)

        // Now you can use the 'selectedInvention' to query Firebase or display the details in the activity.

        if (selectedInvention != null) {
            // You can use the retrieved data here

            val createDate = selectedInvention.createDate
            val inventionName = selectedInvention.inventionName
            val status = selectedInvention.status

            b.setText(inventionName)

            // Implement your Firebase query or display logic here.
            // For example, you can access selectedInvention.createDate, selectedInvention.inventionName, etc.
        } else {
            finish()
        }

    }
}