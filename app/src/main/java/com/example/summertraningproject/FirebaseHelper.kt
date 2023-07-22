package com.example.summertraningproject

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object FirebaseHelper {

    private val databaseInst: FirebaseDatabase = FirebaseDatabase.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val Reference: DatabaseReference = databaseInst.reference


    // Function to sign in user with email and password
    suspend fun signInWithEmail(Email: String, password: String,context: Context) = withContext(Dispatchers.IO) {
       auth.signInWithEmailAndPassword(Email,password).addOnCompleteListener {

            if(it.isSuccessful){

                if (Email == "admin@gmail.com"){

                    startAdminActivity(context)

                }else {

                    startMainPageActivity(context)
                }

            }else{

                Toasty.error(context," Email or Password is invalid ", Toasty.LENGTH_SHORT).show()

            }

        }.await()
    }

    fun startAdminActivity(context: Context) {
        val intent = Intent(context, Admin::class.java)
        context.startActivity(intent)
    }

    fun startMainPageActivity(context: Context) {
        val intent = Intent(context, MainPage::class.java)
        context.startActivity(intent)
    }

    suspend fun createUserWithEmail(Email: String, password: String,context: Context) = withContext(Dispatchers.IO) {

        auth.createUserWithEmailAndPassword(Email,password).addOnCompleteListener {

            if(it.isSuccessful){

                val invetorID = FirebaseAuth.getInstance().currentUser?.uid
                val interest = "None"

                val inventors = InventorModel(Email,password,invetorID,interest)

                Reference.child("Inventors").child(invetorID.toString()).setValue(inventors)
                    .addOnCompleteListener {

                        Toasty.success(context," User has been added Successfully. ", Toast.LENGTH_SHORT).show()


                    }.addOnFailureListener {

                        Toasty.error(context,"Inventor has not been added", Toast.LENGTH_LONG).show()
                    }

            }else{

                Toasty.error(context," Email or Password is invalid ", Toast.LENGTH_SHORT).show()

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

    // Function to get data from Firebase in a coroutine-safe way
    suspend fun getInventorsData(Email: String): String = withContext(Dispatchers.IO) {
        val reference: DatabaseReference = databaseInst.getReference("Inventors").child(Email.replace(".",","))
        val dataSnapshot: DataSnapshot = reference.get().await()
        dataSnapshot.getValue(String::class.java) ?: ""
    }

    suspend fun getInventionsData(Email: String): String = withContext(Dispatchers.IO) {
        val reference: DatabaseReference = databaseInst.getReference("Inventions").child(Email.replace(".",","))
        val dataSnapshot: DataSnapshot = reference.get().await()
        dataSnapshot.getValue(String::class.java) ?: ""
    }

    // Function to insert Inventions data into Firebase in a coroutine-safe way
    suspend fun insertDataInInventions(inventionName: String,status: String,CreateDate: String,context: Context) = withContext(Dispatchers.IO) {

        val reference: DatabaseReference = databaseInst.getReference("Inventions").child(FirebaseAuth.getInstance().currentUser?.uid.toString())

        // Generate a new unique key for each invention using push()
        val newInventionRef = reference.push()

        newInventionRef.setValue(InventionModel(newInventionRef.key,inventionName,status,CreateDate))
            .addOnCompleteListener {

            Toasty.success(context," an Invention has been added Successfully. ", Toast.LENGTH_SHORT).show()


        }.addOnFailureListener {

            Toasty.error(context,"an error has occurred ", Toast.LENGTH_LONG).show()
        }

    }
}
