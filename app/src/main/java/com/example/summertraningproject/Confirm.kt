package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty

class Confirm : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var T: String


    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: reseAdapter
    private var dataList: MutableList<rese> = mutableListOf()

    private lateinit var recyclerView2: RecyclerView
    private lateinit var adapter2: SponAdapter
    private var dataList2: MutableList<Sponsor> = mutableListOf()

    private lateinit var recyclerView3: RecyclerView
    private lateinit var adapter3: FileDataAdapter
    private var dataList3: MutableList<files> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm)


        val SP = findViewById<StepsView>(R.id.stepsView)


        SP.setLabels(arr).setBarColorIndicator(getColor(R.color.TextColor))
            .setProgressColorIndicator(getColor(R.color.ProgressBar))
            .setLabelColorIndicator(getColor(R.color.TextColor))
            .setCompletedPosition(4)
            .drawView()

        val receivedData = intent.getStringExtra("Title")

        if (receivedData != null) {
            T = receivedData
        }


        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {


            val intent = Intent(this@Confirm, Survey::class.java)
            startActivity(intent)

        }

        val Submit = findViewById<Button>(R.id.button4)

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val InvReference =
                FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                    .child(T.toString())



            Submit.setOnClickListener {

                Toasty.success(
                    this@Confirm,
                    "Disclosure has been submitted successfully! ",
                    Toasty.LENGTH_SHORT
                ).show()



                InvReference.child("status").setValue("Submitted")

                val intent = Intent(this@Confirm, MainPage::class.java)
                startActivity(intent)


            }

            // Details
            val invName = findViewById<TextView>(R.id.textView5555)
            val invNum = findViewById<TextView>(R.id.textView11111)
            val DType = findViewById<TextView>(R.id.textView55555)
            val DStatus = findViewById<TextView>(R.id.textView4445)
            val fpd = findViewById<TextView>(R.id.textView6665)
            val cod = findViewById<TextView>(R.id.textView456)
            val sKeywords = findViewById<TextView>(R.id.textView4566)
            val Des = findViewById<TextView>(R.id.textView78)
            val CI = findViewById<TextView>(R.id.textView6667)

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


            val selectedInvention = InvReference.get().addOnSuccessListener {


                if (it.exists()) {
                    invName.text = it.child("inventionName").value.toString()
                    CI.text = it.child("createDate").value.toString()
                    DStatus.text = it.child("status").value.toString()
                    invNum.text = it.child("no").value.toString()
                    DType.text = it.child("type").value.toString()
                    Des.text = it.child("description").value.toString()
                    fpd.text = it.child("first_Public_Disclosure").value.toString()
                    cod.text = it.child("circumstances_of_Disclosure").value.toString()
                    sKeywords.text = it.child("suggested_Keywords").value.toString()


                    Q1.text = it.child("intro").value.toString()
                    Q2.text = it.child("invDomain").value.toString()
                    Q3.text = it.child("suggested_Keywords").value.toString()
                    Q4.text = it.child("literature_review").value.toString()
                    Q5.text = it.child("state_the_problem").value.toString()
                    Q6.text = it.child("disadvantages_of_the_invention").value.toString()
                    Q7.text = it.child("proposed_solution").value.toString()
                    Q8.text = it.child("methodology").value.toString()
                    Q9.text = it.child("advantages_of_the_invention").value.toString()
                    Q10.text = it.child("preliminary_results").value.toString()
                    Q11.text = it.child("references").value.toString()

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val InvReference =
                            FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                                .child(it.child("inventionName").value.toString())

                        // Attach a ValueEventListener to read data from the database
                        InvReference.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                // This method will be triggered when data is read or updated in the database
                                // dataSnapshot contains the data at the specified database reference
                                if (dataSnapshot.exists()) {

                                    s1.text =
                                        dataSnapshot.child("dateOfConceptionDocumented").value.toString()
                                    s2.text =
                                        dataSnapshot.child("priorArtPatentsSearched").value.toString()
                                    s3.text = dataSnapshot.child("prototypeMade").value.toString()
                                    s4.text =
                                        dataSnapshot.child("priorPublicDisclosure").value.toString()


                                }
                            }

                            override fun onCancelled(databaseError: DatabaseError) {
                                // This method will be triggered if there is an error while reading data
                                // Handle the error here
                            }
                        })

                        recyclerView = findViewById(R.id.recyclerView)
                        recyclerView.layoutManager = LinearLayoutManager(this)
                        adapter = reseAdapter(dataList)
                        recyclerView.adapter = adapter

                        val MembersRef = FirebaseHelper.databaseInst.getReference("Inventions")
                            .child(userId).child(it.child("inventionName").value.toString())
                            .child("members")

                        MembersRef.addValueEventListener(object : ValueEventListener {
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

                        recyclerView2 = findViewById(R.id.recyclerView2)
                        recyclerView2.layoutManager = LinearLayoutManager(this)
                        adapter2 = SponAdapter(dataList2)
                        recyclerView2.adapter = adapter2

                        val SponRef = FirebaseHelper.databaseInst.getReference("Inventions")
                            .child(userId).child(it.child("inventionName").value.toString())
                            .child("sponsors")

                        SponRef.addValueEventListener(object : ValueEventListener {
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

                        recyclerView3 = findViewById(R.id.recyclerView4)
                        recyclerView3.layoutManager = LinearLayoutManager(this)
                        adapter3 = FileDataAdapter(dataList3)
                        recyclerView3.adapter = adapter3

                        val FilesRef = FirebaseHelper.databaseInst.getReference("Inventions")
                            .child(userId).child(it.child("inventionName").value.toString())
                            .child("Details").child("Files")

                        FilesRef.addValueEventListener(object : ValueEventListener {
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

                }
            }

        }
    }

}
