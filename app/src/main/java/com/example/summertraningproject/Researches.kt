package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class Researches : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    // Declare the views
    private lateinit var textView4445: EditText
    private lateinit var textView6665: TextView
    private lateinit var textView456: TextView
    private lateinit var textView4566: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_researches)

        val SP = findViewById<StepsView>(R.id.stepsView)


        SP.setLabels(arr).setBarColorIndicator(getColor(R.color.TextColor))
            .setProgressColorIndicator(getColor(R.color.ProgressBar))
            .setLabelColorIndicator(getColor(R.color.TextColor))
            .setCompletedPosition(1)
            .drawView()

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {

            val intent = Intent(this, NewDisclosure::class.java)
            startActivity(intent)

        }


        val nextStep = findViewById<Button>(R.id.button4)

        nextStep.setOnClickListener {

            val intent = Intent(this, Researches::class.java)
            startActivity(intent)

        }

        // Find the views by their IDs
        textView4445 = findViewById(R.id.textView4445)
        textView6665 = findViewById(R.id.textView6665)
        textView456 = findViewById(R.id.textView456)
        textView4566 = findViewById(R.id.textView4566)

        val add: Button = findViewById(R.id.button5)
        add.setOnClickListener {

        }

    }



    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return email.matches(emailRegex)
    }

}
