package com.example.summertraningproject

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.*

class InventionDetailsActivity : AppCompatActivity() {

    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: reseAdapter
    private lateinit var dataList: MutableList<rese>

    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter2: SponAdapter
    private lateinit var dataList2: MutableList<Sponsor>

    private lateinit var recyclerView3: RecyclerView
    private lateinit var adapter3: FileDataAdapter
    private lateinit var dataList3: MutableList<files>

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_invention_details)

        val intent = intent
        val selectedInvention: InventionModel? = intent.getParcelableExtra("invention")

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {
            val intent = Intent(this@InventionDetailsActivity, InventorHomepage::class.java)
            startActivity(intent)
        }

        dataList = mutableListOf()
        dataList2 = mutableListOf()
        dataList3 = mutableListOf()

        if (selectedInvention != null) {
            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val invName = selectedInvention.inventionName.toString()

                CoroutineScope(Dispatchers.Main).launch {
                    updateUI(selectedInvention)
                    fetchMembersData(userId, invName)
                    fetchSponsorData(userId, invName)
                    fetchFilesData(userId, invName)
                }
            }
        }
    }

    private suspend fun fetchMembersData(userId: String, invName: String) {


        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = reseAdapter(dataList)
        recyclerView.adapter = adapter

        val MembersRef = FirebaseHelper.databaseInst.getReference("Inventions")
            .child(userId).child(invName).child("members")


        MembersRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList.clear()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(rese::class.java)
                    data?.let {
                        dataList.add(it)
                    }
                }

                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any error occurred while fetching data
            }
        })
    }

    private suspend fun fetchSponsorData(userId: String, invName: String) {

        recyclerView2 = findViewById(R.id.recyclerView2)
        recyclerView2.layoutManager = LinearLayoutManager(this)
        adapter2 = SponAdapter(dataList2)
        recyclerView2.adapter = adapter2

        val SponRef = FirebaseHelper.databaseInst.getReference("Inventions")
            .child(userId).child(invName).child("sponsors")

        SponRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList2.clear()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(Sponsor::class.java)
                    data?.let {
                        dataList2.add(it)
                    }
                }
                adapter2.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any error occurred while fetching data
            }
        })
    }

    private suspend fun fetchFilesData(userId: String, invName: String) {

        recyclerView3 = findViewById(R.id.recyclerView4)
        recyclerView3.layoutManager = LinearLayoutManager(this)
        adapter3 = FileDataAdapter(dataList3)
        recyclerView3.adapter = adapter3

        val FilesRef = FirebaseHelper.databaseInst.getReference("Inventions")
            .child(userId).child(invName).child("Details").child("Files")

        FilesRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataList3.clear()
                for (snapshot in dataSnapshot.children) {
                    val data = snapshot.getValue(files::class.java)
                    data?.let {
                        dataList3.add(it)
                    }
                }
                adapter3.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle any error occurred while fetching data
            }
        })
    }


    private fun updateUI(selectedInvention: InventionModel) {
        val invName = findViewById<TextView>(R.id.textView5555)
        val CI = findViewById<TextView>(R.id.textView6667)

        val invNum = findViewById<TextView>(R.id.textView11111)
        val DType = findViewById<TextView>(R.id.textView55555)
        val DStatus = findViewById<TextView>(R.id.textView4445)
        val fpd = findViewById<TextView>(R.id.textView6665)
        val cod = findViewById<TextView>(R.id.textView456)
        val sKeywords = findViewById<TextView>(R.id.textView4566)
        val Des = findViewById<TextView>(R.id.textView78)

        // Questions
        val Q1 = findViewById<TextView>(R.id.T1)
        val Q2 = findViewById<TextView>(R.id.T2)
        val Q3 = findViewById<TextView>(R.id.T3)
        val Q4 = findViewById<TextView>(R.id.T4)
        val Q5 = findViewById<TextView>(R.id.T5)
        val Q6 = findViewById<TextView>(R.id.T6)
        val Q7 = findViewById<TextView>(R.id.T7)
        val Q8 = findViewById<TextView>(R.id.T8)
        val Q9 = findViewById<TextView>(R.id.T9)
        val Q10 = findViewById<TextView>(R.id.T10)
        val Q11 = findViewById<TextView>(R.id.T11)

        // Survey
        val s1 = findViewById<TextView>(R.id.T111)
        val s2 = findViewById<TextView>(R.id.T222)
        val s3 = findViewById<TextView>(R.id.T333)
        val s4 = findViewById<TextView>(R.id.T44)

        invName.text = selectedInvention.inventionName.toString()
        CI.text = selectedInvention.createDate.toString()
        DStatus.text = selectedInvention.status.toString()
        invNum.text = selectedInvention.no.toString()
        DType.text = selectedInvention.type.toString()
        Des.text = selectedInvention.description.toString()
        fpd.text = selectedInvention.first_Public_Disclosure.toString()
        cod.text = selectedInvention.circumstances_of_Disclosure.toString()
        sKeywords.text = selectedInvention.suggested_Keywords.toString()

        Q1.text = selectedInvention.intro.toString()
        Q2.text = selectedInvention.invDomain.toString()
        Q3.text = selectedInvention.suggested_Keywords.toString()
        Q4.text = selectedInvention.literature_review.toString()
        Q5.text = selectedInvention.state_the_problem.toString()
        Q6.text = selectedInvention.disadvantages_of_the_invention.toString()
        Q7.text = selectedInvention.proposed_solution.toString()
        Q8.text = selectedInvention.methodology.toString()
        Q9.text = selectedInvention.advantages_of_the_invention.toString()
        Q10.text = selectedInvention.preliminary_results.toString()
        Q11.text = selectedInvention.references.toString()

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val invReference = FirebaseHelper.databaseInst.getReference("Inventions")
                .child(userId).child(selectedInvention.inventionName.toString())

            // Attach a ValueEventListener to read data from the database
            invReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method will be triggered when data is read or updated in the database
                    // dataSnapshot contains the data at the specified database reference
                    if (dataSnapshot.exists()) {
                        s1.text = dataSnapshot.child("dateOfConceptionDocumented").value.toString()
                        s2.text = dataSnapshot.child("priorArtPatentsSearched").value.toString()
                        s3.text = dataSnapshot.child("prototypeMade").value.toString()
                        s4.text = dataSnapshot.child("priorPublicDisclosure").value.toString()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // This method will be triggered if there is an error while reading data
                    // Handle the error here
                }
            })
        }
    }


}
