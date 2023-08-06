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
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.properties.Delegates

class Researches : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions","Survey", "Confirm")
    private val membersList: MutableList<Member> = mutableListOf()
    private lateinit var memberAdapter: MemberAdapter
    private lateinit var receivedData: String

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


        // Get the data passed from the SenderActivity
        receivedData = intent.getStringExtra("Title").toString()

        back.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                val isSuccess = deleteMembersFromFirebase(receivedData)
                if (isSuccess) {
                    val intent = Intent(this@Researches, NewDisclosure::class.java)
                    startActivity(intent)
                } else {
                    Toasty.error(this@Researches, "Failed to delete members", Toasty.LENGTH_SHORT).show()
                }
            }

        }


        val nextStep = findViewById<Button>(R.id.button4)

        nextStep.setOnClickListener {

            val intent = Intent(this, Funding::class.java)
            intent.putExtra("Title",receivedData)
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

            val firstName = firstNameEditText.text.toString().trim()
            val middleName = middleNameEditText.text.toString().trim()
            val lastName = lastNameEditText.text.toString().trim()
            val organization = organizationEditText.text.toString().trim()
            val emailAddress = emailAddressEditText.text.toString().trim()

            CoroutineScope(Dispatchers.Main).launch {
                if (validateInput(firstName, lastName, emailAddress)) {
                    addMemberToFirebase(
                        firstName,
                        middleName,
                        lastName,
                        organization,
                        emailAddress,
                        receivedData
                    )
                }
            }

        }

    }

    private suspend fun deleteMembersFromFirebase(receivedData: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val referenceToDelete = FirebaseHelper.databaseInst.getReference("Inventions")
            referenceToDelete.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(receivedData).child("members").removeValue()
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    private suspend fun addMemberToFirebase(
        firstName: String,
        middleName: String,
        lastName: String,
        organization: String,
        emailAddress: String,
        receivedData: String
    ) = withContext(Dispatchers.IO) {
        try {
            if (isEmailValid(emailAddress)) {

                val newMember = Member("","","",false, "0.00", "$firstName $middleName $lastName", organization, emailAddress)
                val membersRef = FirebaseHelper.databaseInst.getReference("Inventions")
                val newMemberRef = membersRef.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child(receivedData).child("members").push()

                newMember.memberId = newMemberRef.key.toString()
                newMember.InvOwner = FirebaseAuth.getInstance().currentUser?.uid.toString()
                newMember.InvName = receivedData

                newMemberRef.setValue(newMember)


                // Add the new member to the list
                membersList.add(newMember)

                // Switch to the main thread and notify the adapter about the data change
                withContext(Dispatchers.Main) {
                    memberAdapter.notifyDataSetChanged()
                }

            } else {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Researches, "Email is Invalid", Toasty.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun validateInput(firstName: String, lastName: String, emailAddress: String): Boolean =
        withContext(Dispatchers.Default) {
            if (firstName.isEmpty() || lastName.isEmpty() || emailAddress.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Researches, "First name, Last name, or Email is empty", Toasty.LENGTH_SHORT).show()
                }
                return@withContext false
            }
            return@withContext true
        }



fun isEmailValid(email: String): Boolean {
        val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")
        return email.matches(emailRegex)
    }

}
