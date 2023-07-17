package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.summertraningproject.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

            val Login = findViewById<Button>(R.id.button2)
            Login.setOnClickListener {
                val intent = Intent(this,MainPage::class.java)
                startActivity(intent)
            }

        val ForgotPass = findViewById<TextView>(R.id.textView3)

        ForgotPass.setOnClickListener{
            Toast.makeText(this," i forgot my password ",Toast.LENGTH_SHORT).show()
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