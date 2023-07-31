package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlin.properties.Delegates

class Researches : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val auth = FirebaseAuth.getInstance()
    private val membersList: MutableList<Member> = mutableListOf()
    private lateinit var memberAdapter: MemberAdapter


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

            // Reference to the location you want to delete
            val referenceToDelete = FirebaseHelper.databaseInst.getReference("Inventions")

            // Get the data passed from the SenderActivity
            val receivedData = intent.getStringExtra("Title")

            // Delete the data
            referenceToDelete.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(receivedData.toString()).child("members").removeValue()

            val intent = Intent(this, NewDisclosure::class.java)
            startActivity(intent)

        }


        val nextStep = findViewById<Button>(R.id.button4)

        nextStep.setOnClickListener {

            val intent = Intent(this, Researches::class.java)
            startActivity(intent)

        }


        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        // Set up RecyclerView and its adapter
        memberAdapter = MemberAdapter(membersList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = memberAdapter

        val add: Button = findViewById(R.id.button5)

        add.setOnClickListener {

            // Variables to hold references to the EditText views inside the CardView
            var firstNameEditText: EditText = findViewById(R.id.textView45616)
            var middleNameEditText: EditText = findViewById(R.id.textView457)
            var lastNameEditText: EditText = findViewById(R.id.textView4000)
            var organizationEditText: EditText = findViewById(R.id.textView45602)
            var emailAddressEditText: EditText = findViewById(R.id.textView4560)

            // Get the user input from the EditText fields
            val firstName = firstNameEditText.text.toString().trim()
            val middleName = middleNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val organization = organizationEditText.text.toString().trim()
            val emailAddress = emailAddressEditText.text.toString().trim()

            // Create a new Member object using the input
            val newMember = Member(
                false, "0.00", "$firstName $middleName $lastName", organization, emailAddress
            )

            val membersRef = FirebaseHelper.databaseInst.getReference("Inventions")

            // Get the data passed from the SenderActivity
            val receivedData = intent.getStringExtra("Title")

            val newMemberRef =
                membersRef.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(
                    receivedData.toString()
                ).child("members").push()


            newMemberRef.setValue(newMember)


            // Add the new member to the list
            membersList.add(newMember)

            // Notify the adapter about the data change
            memberAdapter.notifyDataSetChanged()

        }

    }


    fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return email.matches(emailRegex)
    }

}
