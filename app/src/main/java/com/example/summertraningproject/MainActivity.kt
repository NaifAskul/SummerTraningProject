package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbHelper = DatabaseHelper(this)

        val Login = findViewById<Button>(R.id.button2)
        Login.setOnClickListener {
            val username = findViewById<EditText>(R.id.editTextTextPersonName).getText().toString();
            val password = findViewById<EditText>(R.id.editTextTextPassword3).getText().toString();

            val userExists = dbHelper.checkUser(username, password)
            if (userExists) {
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show()
            }
        }

        val ForgotPass = findViewById<TextView>(R.id.textView3)
        // Temporary method that creates a new username with 123 and 123 password
        ForgotPass.setOnClickListener {
            val username = "123"
            val password = "123"

            // Add the new user to the database
            val newRowId = dbHelper.addUser(username, password)

            if (newRowId != -1L) {
                Toast.makeText(this, "User added successfully", Toast.LENGTH_SHORT).show()

                // Now you can proceed to the MainPage or do any other actions as needed.
                val intent = Intent(this, MainPage::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
            }
        }
       // End of temporary method

        val img = findViewById<ImageView>(R.id.imageView)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // It's currently in dark mode
            img.setImageResource(R.drawable.gray)
        } else {
            // It's currently in light mode
            img.setImageResource(R.drawable.black)
        }

    }


    override fun onPause() {
        Log.i(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }

}