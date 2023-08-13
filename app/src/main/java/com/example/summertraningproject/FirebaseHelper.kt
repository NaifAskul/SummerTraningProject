package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.messaging.FirebaseMessaging
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseHelper {

    public val databaseInst: FirebaseDatabase = FirebaseDatabase.getInstance()
    public val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val Reference: DatabaseReference = databaseInst.reference
    private lateinit var userRecyclerview: RecyclerView
    private  var inventionsArrayList: ArrayList<InventionModel>  = ArrayList()


    // Function to sign in user with email and password
    suspend fun signInWithEmail(Email: String, password: String, context: Context) =
        withContext(Dispatchers.IO) {
            auth.signInWithEmailAndPassword(Email, password).addOnCompleteListener {

                if (it.isSuccessful) {

                    requestNotificationPermission()

                    if (Email == "admin@gmail.com") {

                        startAdminActivity(context)

                    } else {

                        val UserId = FirebaseAuth.getInstance().currentUser?.uid
                        if (UserId != null) {
                            val invReference = FirebaseHelper.databaseInst.getReference("Inventors").child(UserId)


                            // Add a ValueEventListener to retrieve data
                            invReference.addValueEventListener(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {

                                        if (snapshot.child("userType").value.toString() == "Certified arbitrator") {

                                            startCertifiedArbitratorMainPageActivity(context)

                                        }else if (snapshot.child("userType").value.toString() == "Admin"){

                                            startAdminActivity(context)

                                        }else if(snapshot.child("userType").value.toString() == "Inventor"){

                                            startInventorMainPageActivity(context)

                                        }

                                    }

                                }

                                override fun onCancelled(error: DatabaseError) {
                                    // Handle any errors that occur while retrieving data
                                }
                            })
                        }

                    }

                } else {

                    Toasty.error(context, " Email or Password is invalid ", Toasty.LENGTH_SHORT)
                        .show()

                }

            }.await()
        }

    private fun requestNotificationPermission() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val token = task.result

                // Send the token to your server or store it in the database
                sendTokenToServer(token)
            } else {
                // Handle token retrieval failure
            }
        }
    }

    private fun sendTokenToServer(token: String?) {

        // In your Kotlin code, get the FCM token
        FirebaseMessaging.getInstance().token
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(
                        TAG,
                        "Fetching FCM registration token failed",
                        task.exception
                    )
                    return@addOnCompleteListener
                }

                // Get the FCM token
                val token = task.result

                val invetorID = FirebaseAuth.getInstance().currentUser?.uid

                // Store the token in the Realtime Database
                val userId = invetorID.toString().trim()
                val tokenRef =
                    FirebaseDatabase.getInstance().reference.child("Inventors")
                        .child(userId).child("fcmToken")
                tokenRef.setValue(token)

            }

    }

    fun startAdminActivity(context: Context) {
        val intent = Intent(context, Admin::class.java)
        context.startActivity(intent)
    }

    fun startInventorMainPageActivity(context: Context) {
        val intent = Intent(context, MainPage::class.java)
        context.startActivity(intent)
    }

    fun startCertifiedArbitratorMainPageActivity(context: Context) {
        val intent = Intent(context, CertifiedArbitrator::class.java)
        context.startActivity(intent)
    }

    suspend fun createUserWithEmail(Email: String, password: String,Utype: String, context: Context) =
        withContext(Dispatchers.IO) {

            auth.createUserWithEmailAndPassword(Email, password).addOnCompleteListener {

                    if (it.isSuccessful) {

                        val invetorID = FirebaseAuth.getInstance().currentUser?.uid
                        val interest = "None"

                        val inventors = InventorModel(Email, password, invetorID, interest)


                        Reference.child("Inventors").child(invetorID.toString()).setValue(inventors)
                            .addOnCompleteListener {

                                Reference.child("Inventors").child(invetorID.toString())
                                    .child("userType").setValue(Utype)

                                Toasty.success(
                                    context,
                                    " User has been added Successfully. ",
                                    Toasty.LENGTH_SHORT
                                ).show()


                            }.addOnFailureListener {

                                Toasty.error(
                                    context,
                                    "Inventor has not been added",
                                    Toasty.LENGTH_LONG
                                )
                                    .show()
                            }

                    } else {

                        Toasty.error(context, " Email or Password is invalid ", Toasty.LENGTH_SHORT)
                            .show()

                    }



                }.await()


        }


    // Function to sign out user
    fun signOut() {
        auth.signOut()
    }

    // Function to check if a user is signed in
    fun isUserSignedIn(): Boolean {
        return auth.currentUser != null
    }

    suspend fun getInventionsData(
        context: Context,
        findViewById1: Any,
        findViewById2: Any,
        findViewById3: Any
    ) {
        userRecyclerview = findViewById1 as RecyclerView
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)

        try {
            val dbref = FirebaseHelper.databaseInst.getReference("Inventions")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())

            val dataSnapshot = withContext(Dispatchers.IO) {
                dbref.get().await()
            }

            if (dataSnapshot.exists()) {
                val tempList: MutableList<InventionModel> = mutableListOf()

                // Go one level deeper by iterating through the children of each node
                for (inventionSnapshot in dataSnapshot.children) {
                    // Exclude nodes with key "sponsors" and "members" that have children
                    if (inventionSnapshot.key == "sponsors" || inventionSnapshot.key == "members") {
                        if (inventionSnapshot.hasChildren()) {
                            continue
                        }
                    } else {
                        val inv = inventionSnapshot.getValue(InventionModel::class.java)
                        inv?.let {
                            if(it.status != "Received") {
                                tempList.add(it)
                            }

                        }
                    }
                }

                inventionsArrayList.clear()
                inventionsArrayList.addAll(tempList)


                val adapter = MyAdapter(inventionsArrayList) { item ->
                    // Handle item click here
                    val intent1 = Intent(context, InventionDetailsActivity::class.java)
                    intent1.putExtra("invention", item)
                    context.startActivity(intent1)

                }



                userRecyclerview.adapter = adapter


            } else {
                val noDis = findViewById2 as TextView
                val clickhere = findViewById3 as TextView

                noDis.visibility = View.VISIBLE
                clickhere.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            // Handle any exceptions or errors
        }

    }

    suspend fun getInventionsList(
        context: Context,
        findViewById1: Any,
        findViewById2: Any,
        id: String
    ) {
        userRecyclerview = findViewById1 as RecyclerView
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)

        try {
            val dbref = databaseInst.getReference("Inventions")
                .child(id)

            val dataSnapshot = withContext(Dispatchers.IO) {
                dbref.get().await()
            }

            if (dataSnapshot.exists()) {
                val tempList: MutableList<InventionModel> = mutableListOf()

                // Go one level deeper by iterating through the children of each node
                for (inventionSnapshot in dataSnapshot.children) {
                    // Exclude nodes with key "sponsors" and "members" that have children
                    if (inventionSnapshot.key == "sponsors" || inventionSnapshot.key == "members") {
                        if (inventionSnapshot.hasChildren()) {
                            continue
                        }
                    } else {
                        val inv = inventionSnapshot.getValue(InventionModel::class.java)
                        inv?.let {
                            if(it.status != "Received") {


                                tempList.add(it)
                            }

                        }
                    }
                }

                inventionsArrayList.clear()
                val noDis = findViewById2 as TextView

                noDis.visibility = View.INVISIBLE
                inventionsArrayList.addAll(tempList)


                val adapter = MyAdapter(inventionsArrayList) { item ->
                    // Handle item click here
                    val intent1 = Intent(context, InventionsCAdetails::class.java)
                    intent1.putExtra("invention", item)
                    intent1.putExtra("id",id)
                    context.startActivity(intent1)

                }



                userRecyclerview.adapter = adapter


            } else {

                Toasty.error(
                    context,
                    " Invention is not found ",
                    Toasty.LENGTH_SHORT
                ).show()

                val noDis = findViewById2 as TextView

                noDis.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            // Handle any exceptions or errors
        }

    }


    suspend fun getInventionsDataApproved(
        context: Context,
        findViewById1: Any,
        findViewById2: Any,
        findViewById3: Any
    ) {
        userRecyclerview = findViewById1 as RecyclerView
        userRecyclerview.layoutManager = LinearLayoutManager(context)
        userRecyclerview.setHasFixedSize(true)

        try {
            val dbref = FirebaseHelper.databaseInst.getReference("Inventions")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())

            val dataSnapshot = withContext(Dispatchers.IO) {
                dbref.get().await()
            }

            if (dataSnapshot.exists()) {
                val tempList: MutableList<InventionModel> = mutableListOf()

                // Go one level deeper by iterating through the children of each node
                for (inventionSnapshot in dataSnapshot.children) {
                    // Exclude nodes with key "sponsors" and "members" that have children
                    if (inventionSnapshot.key == "sponsors" || inventionSnapshot.key == "members") {
                        if (inventionSnapshot.hasChildren()) {
                            continue
                        }
                    } else {
                        val inv = inventionSnapshot.getValue(InventionModel::class.java)
                        inv?.let {
                            if(it.status == "Approved") {
                                tempList.add(it)
                            }
                        }
                    }
                }

                inventionsArrayList.clear()

                inventionsArrayList.addAll(tempList)


                if (tempList.isEmpty()) {
                    val noDis = findViewById2 as TextView
                    val clickhere = findViewById3 as TextView

                    noDis.visibility = View.VISIBLE
                    clickhere.visibility = View.VISIBLE
                } else {
                    val adapter = MyAdapter(inventionsArrayList) { item ->
                        // Handle item click here
                        val intent1 = Intent(context, InventionDetailsActivity::class.java)
                        intent1.putExtra("invention", item)
                        context.startActivity(intent1)
                    }

                    userRecyclerview.adapter = adapter
                }

            } else {
                val noDis = findViewById2 as TextView
                val clickhere = findViewById3 as TextView

                noDis.visibility = View.VISIBLE
                clickhere.visibility = View.VISIBLE
            }
        } catch (e: Exception) {
            // Handle any exceptions or errors
        }

    }

    // Function to insert Inventions data into Firebase in a coroutine-safe way
    suspend fun insertDataInInventions(
        inventionName: String,
        status: String,
        CreateDate: String,
        context: Context
    ) = withContext(Dispatchers.IO) {

        val reference: DatabaseReference = databaseInst.getReference("Inventions")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(inventionName)


        reference.setValue(
            InventionModel(
                CreateDate,
                inventionName,
                reference.push().key,
                FirebaseAuth.getInstance().currentUser?.uid.toString(),
                status
            )
        )
            .addOnCompleteListener {


            }.addOnFailureListener {

                Toasty.error(context, "an error has occurred ", Toasty.LENGTH_LONG).show()
            }

    }

}
