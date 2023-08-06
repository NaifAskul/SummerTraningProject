package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Questions : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions","Survey", "Confirm")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)


        val back = findViewById<Button>(R.id.button1)
        val receivedData = intent.getStringExtra("Title")

        back.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                    val intent = Intent(this@Questions, Funding::class.java)
                    startActivity(intent)

            }

        }

        val nextStep = findViewById<Button>(R.id.button4)

        // Variables to hold references to the EditText views inside the CardView
        var Sponsor_Name: EditText = findViewById(R.id.textViewName)
        var Contract_Number : EditText = findViewById(R.id.textView4000)
        var Sponsor_Name: EditText = findViewById(R.id.textViewName)
        var Contract_Number : EditText = findViewById(R.id.textView4000)

        nextStep.setOnClickListener {


            GlobalScope.launch(Dispatchers.Main) {

                // Coroutine code will go here
                val sponsorName = Sponsor_Name.text.toString().trim()
                val contractNum = Contract_Number.text.toString().trim()

                if (sponsorName.isEmpty()) {
                    Toasty.error(this@Questions, "Sponsor name is empty", Toasty.LENGTH_SHORT)
                        .show()
                } else if (contractNum.isEmpty()) {
                    Toasty.error(
                        this@Questions,
                        "Contract Number is empty",
                        Toasty.LENGTH_SHORT
                    ).show()
                } else if (citizenShip.isEmpty()) {
                    Toasty.error(this@Funding, "Citizenship is empty", Toasty.LENGTH_SHORT)
                        .show()
                } else {
                    val receivedData = intent.getStringExtra("Title")
                    val intent = Intent(this@Questions, Survey::class.java)
                    intent.putExtra("Title", receivedData)
                    startActivity(intent)

                }
            }
        }

    }

}