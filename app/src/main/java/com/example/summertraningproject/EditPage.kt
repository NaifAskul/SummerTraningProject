package com.example.summertraningproject

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.summertraningproject.databinding.ActivityEditPageBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class EditPage : AppCompatActivity() {


    private lateinit var profileImage: ImageView
    private lateinit var ct: String
    private lateinit var citizenShip: String
    private lateinit var gendeR: String

    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        lifecycleScope.launch(Dispatchers.IO) {
            getInventorData(this@EditPage)
        }


        val back = findViewById<Button>(R.id.button1)
        back.setOnClickListener {
            val intent = Intent(this, Settings::class.java)
            startActivity(intent)
        }

        profileImage = findViewById<ImageView>(R.id.profileImage)

        // Load the user's profile image from Firebase Storage and display it
        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("profile_images/${FirebaseAuth.getInstance().currentUser?.uid}")

        imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
            Glide.with(this).load(downloadUri).into(profileImage)
        }.addOnFailureListener { exception ->

        }

        profileImage.setOnClickListener {
            pickImageFromGallery()
        }

        val CourtesyTitle = findViewById<Spinner>(R.id.spinner1)
        val Citizenship = findViewById<Spinner>(R.id.spinner2)
        val Gender = findViewById<Spinner>(R.id.spinner3)

        CourtesyTitle.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                /*temp till we finish the db*/
                if (adapter != null) {
                    ct = adapter.getItemAtPosition(position).toString()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

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

        Gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                /*temp till we finish the db*/
                if (adapter != null) {
                    gendeR = adapter.getItemAtPosition(position).toString()
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }

        val reset = findViewById<Button>(R.id.button3)
        reset.setOnClickListener {
            // Launch the coroutine inside lifecycleScope
            lifecycleScope.launch(Dispatchers.IO) {
                val dataSnapshot = FirebaseHelper.databaseInst.getReference("Inventors")
                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).get().await()

                if (dataSnapshot.exists()) {
                    val E = dataSnapshot.child("email").value.toString()
                    ResetPassword(E)
                }
            }
        }

        val save = findViewById<Button>(R.id.button)
        save.setOnClickListener {

            val Fname = findViewById<EditText>(R.id.textView4566)
            val Mname = findViewById<EditText>(R.id.textView457)
            val Lname = findViewById<EditText>(R.id.textView4560)
            val Interests = findViewById<EditText>(R.id.textView66578)
            val PhNum = findViewById<EditText>(R.id.textView456)
            val cnr = findViewById<EditText>(R.id.textView4569)
            val suffix = findViewById<EditText>(R.id.textView11111)

            val fname = Fname.text.toString()
            val mname = Mname.text.toString()
            val lname = Lname.text.toString()
            val interests = Interests.text.toString()
            val phNum = PhNum.text.toString()
            val cnR = cnr.text.toString()
            val suffiX = suffix.text.toString()


            if (fname.isEmpty() or lname.isEmpty()) {
                Toasty.error(this, "First Name or Last Name are null", Toasty.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (phNum.isNotEmpty() and  !isPhoneNumberValid(phNum) ) {
                Toasty.error(this, "Phone Number is not valid", Toasty.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch(Dispatchers.IO) {
                updateData(fname, mname, lname, interests, phNum, cnR, suffiX,ct,citizenShip,gendeR)
            }
        }
    }



    private suspend fun updateData(
        fname: String,
        mname: String,
        lname: String,
        interests: String,
        phNum: String,
        cnR: String,
        suffiX: String,
        ctt: String,
        cs: String,
        gen: String
    ) {

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val InvReference = FirebaseHelper.databaseInst.getReference("Inventors").child(userId)

            // Update the fields that are not empty
            if (fname.isNotEmpty()) {
                InvReference.child("FirstName").setValue(fname)
            }
            if (mname.isNotEmpty()) {
                InvReference.child("MiddleName").setValue(mname)
            }
            if (lname.isNotEmpty()) {
                InvReference.child("LastName").setValue(lname)
            }
            if (interests.isNotEmpty()) {
                InvReference.child("intersts").setValue(interests)
            }
            if (phNum.isNotEmpty()) {

                if(isPhoneNumberValid(phNum)) {

                    InvReference.child("PhoneNum").setValue(phNum)

                }else{

                    Toasty.error(this@EditPage, "Phone number is invalid", Toasty.LENGTH_SHORT).show()

                }
            }
            if (cnR.isNotEmpty()) {
                if (isNumeric(cnR)) {
                    InvReference.child("CNRStartingBalance").setValue(cnR)
                }else{
                    Toasty.error(this@EditPage, "User not found", Toasty.LENGTH_SHORT).show()
                }
            }
            if (suffiX.isNotEmpty()) {
                InvReference.child("Suffix").setValue(suffiX)

            }
            if (ctt.isNotEmpty()) {
                InvReference.child("CourtesyTitle").setValue(ctt)

            }
            if (cs.isNotEmpty()) {
                InvReference.child("Citizenship").setValue(cs)

            }
            if (gen.isNotEmpty()) {
                InvReference.child("Gender").setValue(gen)
            }

            // Show a success message to the user
            withContext(Dispatchers.Main) {
                Toasty.success(this@EditPage, "Profile updated successfully", Toasty.LENGTH_SHORT).show()
            }
        } else {
            // Show an error message if the user is not logged in
            withContext(Dispatchers.Main) {
                Toasty.error(this@EditPage, "User not found", Toasty.LENGTH_SHORT).show()
            }
        }
    }

    fun isPhoneNumberValid(phoneNumber: String): Boolean {
        val regex = Regex("^05\\d{8}$")
        return regex.matches(phoneNumber)
    }


    fun isNumeric(input: String): Boolean {
        return input.matches("\\d+(\\.\\d+)?".toRegex())
    }


    private fun ResetPassword(email: String) {

        FirebaseHelper.auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {

                    Toasty.success(this, " Reset Password mail has been sent successfully ", Toasty.LENGTH_SHORT).show()

                } else {
                    Toasty.error(this, " an error occurred with sending  reset the email ", Toasty.LENGTH_SHORT).show()
                }
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



    private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_REQUEST_CODE && resultCode == RESULT_OK) {
            val imageUri = data?.data

            if (imageUri != null) {
                // Upload the image to Firebase Storage
                val storageRef = FirebaseStorage.getInstance().reference
                val imageRef =
                    storageRef.child("profile_images/${FirebaseAuth.getInstance().currentUser?.uid}")

                profileImage.setImageURI(imageUri)


                imageRef.putFile(imageUri).addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                    } else {
                        // Image upload failed
                        Toasty.error(this, "Image upload failed", Toasty.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}
