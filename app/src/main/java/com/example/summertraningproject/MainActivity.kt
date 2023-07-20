package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.summertraningproject.databinding.ActivityMainPageBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var database: DatabaseReference
    private lateinit var invList :ArrayList<InventorModel>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Realtime Database
        database = FirebaseDatabase.getInstance().getReference("Inventions")

        usernameEditText = findViewById(R.id.editTextTextPersonName)
        passwordEditText = findViewById(R.id.editTextTextPassword3)

            val Login = findViewById<Button>(R.id.button2)


        val ForgotPass = findViewById<TextView>(R.id.textView3)

        ForgotPass.setOnClickListener{
            Toast.makeText(this," i forgot my password ",Toast.LENGTH_SHORT).show()
        }

        val img = findViewById<ImageView>(R.id.imageView)
        val currentNightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            // It's currently in dark mode
            img.setImageResource(R.drawable.gray)
        } else {
            // It's currently in light mode
            img.setImageResource(R.drawable.black)
        }

            Login.setOnClickListener {
                val username = usernameEditText.text.toString().trim()
                val password = passwordEditText.text.toString()

                if(username.isEmpty() or password.isEmpty()){

                        Toast.makeText(this,"Username or Password is empty",Toast.LENGTH_LONG).show()
                }else{

                     invList = arrayListOf<InventorModel>()

                    val intent = Intent(this,MainPage::class.java)
                    startActivity(intent)


                }


                }


    }

    private fun getInventorsData() {
        database = FirebaseDatabase.getInstance().getReference("Inventors")

        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                invList.clear()

                if (snapshot.exists()) {
                    for (invSnap in snapshot.children) {
                        val invdata = invSnap.getValue(InventorModel::class.java)
                        invList.add(invdata!!)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
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