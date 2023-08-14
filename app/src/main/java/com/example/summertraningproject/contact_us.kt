package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import es.dmoral.toasty.Toasty

class contact_us : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_us2)

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {



            val intent = Intent(this@contact_us, MainPage::class.java)
            startActivity(intent)


        }

        val send = findViewById<Button>(R.id.button4)

        var Mail: EditText = findViewById(R.id.textView45616)



        send.setOnClickListener {

            val mail = Mail.text.toString().trim()

            if (mail.isNotEmpty()) {

                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {

                    val recipient = "admin@gmail.com"
                    val subject = "Customer Support"
                    var email = ""

                    // Initialize Firebase
                    val database = FirebaseDatabase.getInstance()

                    val usersRef = database.reference.child("Inventors")

                    usersRef.child(userId).addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(dataSnapshot: DataSnapshot) {
                            val user = dataSnapshot.getValue(InventorModel::class.java)
                            email = user?.Email.toString()

                            val message = "recipient: $recipient \nEmail: $email\nMessage: $mail"

                            val intent = Intent(Intent.ACTION_SEND)
                            intent.type = "text/plain"
                            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
                            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                            intent.putExtra(Intent.EXTRA_TEXT, message)
                            startActivity(Intent.createChooser(intent, "Send Email"))
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle errors
                        }
                    })

                }

            } else {
                Toasty.error(this, "The text is empty").show()
            }
        }

    }
}