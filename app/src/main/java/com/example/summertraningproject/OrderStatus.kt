package com.example.summertraningproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class OrderStatus : AppCompatActivity() {

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_status)

        val newDis = findViewById<TextView>(R.id.textView8)

        mainScope.launch {
            FirebaseHelper.getInventionsData(this@OrderStatus,findViewById(R.id.recyclerView),findViewById(R.id.textView5),findViewById(R.id.textView8))
        }

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