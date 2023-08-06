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

            val email = EmailEditText.text.toString().trim()

            if (email.isEmpty()) {
                EmailEditText.error = "Email is required"
                return@setOnClickListener
            }

            ResetPassword(email)
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
            val password = passwordEditText.text.toString().trim()

            if (Email.isEmpty() or password.isEmpty()) {

                Toasty.error(this, "Email or Password is empty", Toasty.LENGTH_SHORT).show()
            } else {

                if (isEmailValid(Email)) {
                    if (password.length >= 6) {
                        CoroutineScope(Dispatchers.Main).launch {
                            try {

                                FirebaseHelper.signInWithEmail(Email, password, this@MainActivity)

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


    }

    private fun ResetPassword(email: String) {

        FirebaseHelper.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toasty.success(
                        this,
                        " Reset Password mail has been sent successfully ",
                        Toasty.LENGTH_SHORT
                    ).show()

                } else {
                    Toasty.error(
                        this,
                        " an error occurred with sending  reset the email ",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
            }
    }

    fun isEmailValid(email: String): Boolean {
        // Regular expression to validate email addresses
        val regex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$")

        return regex.matches(email)
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