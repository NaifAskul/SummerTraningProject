package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var EmailEditText: EditText
    private lateinit var passwordEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        EmailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.PasswordEditText)


        val Login = findViewById<Button>(R.id.button2)

        val ForgotPass = findViewById<TextView>(R.id.textView3)

        ForgotPass.setOnClickListener {
            Toast.makeText(this, " i forgot my password ", Toast.LENGTH_SHORT).show()
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
            val Email = EmailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (Email.isEmpty() or password.isEmpty()) {

                Toasty.error(this, "Email or Password is empty", Toast.LENGTH_SHORT).show()
            } else {


                CoroutineScope(Dispatchers.Main).launch {
                    try {

                        FirebaseHelper.signInWithEmail(Email, password,this@MainActivity)

                    }catch (e: Exception){

                    }
                }

            }


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