package com.example.summertraningproject

import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Admin : AppCompatActivity() {

    private lateinit var EmailEditText: EditText
    private lateinit var passwordEditText: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        EmailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.PasswordEditText)

        val register = findViewById<Button>(R.id.button2)


        register.setOnClickListener {

            val Email = EmailEditText.text.toString().trim()
            val password = passwordEditText.text.toString()

            if (Email.isEmpty() or password.isEmpty()) {

                Toasty.error(this, "Email or Password is empty", Toast.LENGTH_SHORT).show()

            } else {

                if (isEmailValid(Email)) {
                    if (password.length >= 6) {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {

                                FirebaseHelper.createUserWithEmail(Email, password, this@Admin)
                                EmailEditText.text.clear()
                                passwordEditText.text.clear()

                            } catch (e: Exception) {

                            }
                        }
                    } else {
                        Toasty.error(
                            this,
                            "The password must be at least 6 characters",
                            Toasty.LENGTH_SHORT
                        ).show()
                    }

                }else{
                    Toasty.error(
                        this,
                        "The email is invalid",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
            }

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

    }

    fun isEmailValid(email: String): Boolean {
        // Regular expression to validate email addresses
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

        return regex.matches(email)
    }
}