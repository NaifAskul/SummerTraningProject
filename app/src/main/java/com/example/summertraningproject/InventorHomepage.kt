package com.example.summertraningproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class InventorHomepage : AppCompatActivity() {

    private val mainScope = MainScope()
    private lateinit var profileImage: ImageView

    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventor_homepage)


        // Call the function within a coroutine scope on the main thread
        mainScope.launch {

            getInventorData(this@InventorHomepage)
            FirebaseHelper.getInventionsDataApproved(this@InventorHomepage,findViewById(R.id.recyclerView),findViewById(R.id.textView5),findViewById(R.id.textView8))
        }


        profileImage = findViewById<ImageView>(R.id.profileImage)

       // Load the user's profile image from Firebase Storage and display it
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef =
            storageRef.child("profile_images/${FirebaseAuth.getInstance().currentUser?.uid}")

        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            Glide.with(this).load(downloadUri).into(profileImage)
        }.addOnFailureListener { exception ->

        }


        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {
            val intent = Intent(this,MainPage::class.java)
            startActivity(intent)
        }

        val ND = findViewById<TextView>(R.id.textView8)

        ND.setOnClickListener {
            val intent = Intent(this,NewDisclosure::class.java)
            startActivity(intent)
        }

    }

    suspend fun getInventorData(context: Context) {
        try {
            val dataSnapshot = withContext(Dispatchers.IO) {
                FirebaseHelper.databaseInst.getReference("Inventors")
                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).get().await()
            }

            if (dataSnapshot.exists()) {
                val F = dataSnapshot.child("FirstName").value.toString()
                val M = dataSnapshot.child("MiddleName").value.toString()
                val L = dataSnapshot.child("LastName").value.toString()
                val J = dataSnapshot.child("Job").value.toString()
                val E = dataSnapshot.child("email").value.toString()
                val I = dataSnapshot.child("intersts").value.toString()

                withContext(Dispatchers.Main) {
                    val ProfileName = findViewById<TextView>(R.id.profileName)
                    val job = findViewById<TextView>(R.id.profileDescription)
                    val email = findViewById<TextView>(R.id.profileEmail)
                    val Interests = findViewById<TextView>(R.id.Interests)

                    ProfileName.text = "$F $M $L"
                    job.text = J
                    email.text = E
                    Interests.text = I
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toasty.warning(context, "User doesn't exist", Toasty.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toasty.error(context, "An error has occurred", Toasty.LENGTH_SHORT).show()
            }
        }
    }

}