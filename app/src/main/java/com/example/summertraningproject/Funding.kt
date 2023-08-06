package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Funding : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions","Survey", "Confirm")
    private val sponsorsList: MutableList<Sponsor> = mutableListOf()
    private lateinit var sponsorAdapter: SponsorAdapter
    private lateinit var citizenShip: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_funding)

        val SP = findViewById<StepsView>(R.id.stepsView)


        SP.setLabels(arr).setBarColorIndicator(getColor(R.color.TextColor))
            .setProgressColorIndicator(getColor(R.color.ProgressBar))
            .setLabelColorIndicator(getColor(R.color.TextColor))
            .setCompletedPosition(2)
            .drawView()

        val Citizenship = findViewById<Spinner>(R.id.spinner2)

        Citizenship.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                /*temp till we finish the db*/
                if (adapter != null) {
                    citizenShip = adapter.getItemAtPosition(position).toString()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                val isSuccess = deleteSponsorsFromFirebase()
                if (isSuccess) {
                    val intent = Intent(this@Funding, Researches::class.java)
                    startActivity(intent)
                } else {
                    Toasty.error(this@Funding, "Failed to delete sponsors", Toasty.LENGTH_SHORT).show()
                }
            }

        }

        // Variables to hold references to the EditText views inside the CardView
        var Sponsor_Name: EditText = findViewById(R.id.textViewName)
        var Contract_Number : EditText = findViewById(R.id.textView4000)




            val nextStep = findViewById<Button>(R.id.button4)

            nextStep.setOnClickListener {


                    GlobalScope.launch(Dispatchers.Main) {
                        // Coroutine code will go here
                        val sponsorName = Sponsor_Name.text.toString().trim()
                        val contractNum = Contract_Number.text.toString().trim()

                        if (sponsorName.isEmpty()) {
                            Toasty.error(this@Funding, "Sponsor name is empty", Toasty.LENGTH_SHORT)
                                .show()
                        } else if (contractNum.isEmpty()) {
                            Toasty.error(
                                this@Funding,
                                "Contract Number is empty",
                                Toasty.LENGTH_SHORT
                            ).show()
                        } else if (citizenShip.isEmpty()) {
                            Toasty.error(this@Funding, "Citizenship is empty", Toasty.LENGTH_SHORT)
                                .show()
                        } else {
                            val receivedData = intent.getStringExtra("Title")
                            val intent = Intent(this@Funding, Questions::class.java)
                            intent.putExtra("Title", receivedData)
                            startActivity(intent)
                        }
                    }
                }


                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

                // Set up RecyclerView and its adapter
                sponsorAdapter = SponsorAdapter(sponsorsList)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.adapter = sponsorAdapter

                val add: Button = findViewById(R.id.button5)

                add.setOnClickListener {

                    // Variables to hold references to the EditText views inside the CardView
                    var Sponsor_Name: EditText = findViewById(R.id.textViewName)
                    var Contract_Number: EditText = findViewById(R.id.textView4000)
                    var Contact_Information: EditText = findViewById(R.id.textView45602)

                    val sponsorName = Sponsor_Name.text.toString().trim()
                    val contractNum = Contract_Number.text.toString().trim()
                    val contract_Info = Contact_Information.text.toString()

                    CoroutineScope(Dispatchers.Main).launch {
                        if (validateInput(sponsorName, contractNum)) {
                            addSponsorToFirebase(sponsorName, contractNum, contract_Info)
                        }
                    }
                }

            }

    private suspend fun deleteSponsorsFromFirebase(): Boolean = withContext(Dispatchers.IO) {
        try {
            val referenceToDelete = FirebaseHelper.databaseInst.getReference("Inventions")
            val receivedData = intent.getStringExtra("Title")
            referenceToDelete.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                .child(receivedData.toString()).child("sponsors").removeValue()
            return@withContext true
        } catch (e: Exception) {
            e.printStackTrace()
            return@withContext false
        }
    }

    private suspend fun addSponsorToFirebase(
        sponsorName: String,
        contractNum: String,
        contractInfo: String
    ) = withContext(Dispatchers.IO) {
        try {
            val newSponsor = Sponsor(sponsorName, citizenShip, contractNum, contractInfo)
            val membersRef = FirebaseHelper.databaseInst.getReference("Inventions")
            val receivedData = intent.getStringExtra("Title")
            val newMemberRef =
                membersRef.child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                    .child(receivedData.toString()).child("sponsors").push()
            newMemberRef.setValue(newSponsor)

            // Add the new member to the list
            sponsorsList.add(newSponsor)

            // Notify the adapter about the data change (assuming sponsorAdapter is global or accessible)
            sponsorAdapter.notifyDataSetChanged()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private suspend fun validateInput(sponsorName: String, contractNum: String): Boolean =
        withContext(Dispatchers.Default) {
            if (sponsorName.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Funding, "Sponsor name is empty", Toasty.LENGTH_SHORT).show()
                }
                return@withContext false
            } else if (citizenShip.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Funding, "Country name is empty", Toasty.LENGTH_SHORT).show()
                }
                return@withContext false
            } else if (contractNum.isEmpty()) {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Funding, "Contract Number is empty", Toasty.LENGTH_SHORT).show()
                }
                return@withContext false
            } else if (!isNumeric(contractNum)) {
                withContext(Dispatchers.Main) {
                    Toasty.error(this@Funding, "Contract Number is invalid", Toasty.LENGTH_SHORT).show()
                }
                return@withContext false
            }
            return@withContext true
        }


    fun isNumeric(input: String): Boolean {
        return input.matches("\\d+(\\.\\d+)?".toRegex())
    }

}