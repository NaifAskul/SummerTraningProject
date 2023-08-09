package com.example.summertraningproject

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.UUID

class NewDisclosure : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var DT: String
    private lateinit var TT: String
    private lateinit var filename: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_disclosure)

        val back = findViewById<Button>(R.id.button1)

        var Title = findViewById<EditText>(R.id.EditText1)
        var Description = findViewById<EditText>(R.id.EditText22)
        var FPD = findViewById<EditText>(R.id.textView6658)
        var COD = findViewById<EditText>(R.id.EditText)
        var SK = findViewById<EditText>(R.id.descriptionEditText)
        filename = "None"




        back.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {
                withContext(Dispatchers.IO) {
                    // Your existing code goes here

                    if (TT.isNotEmpty()){
                    val referenceToDelete = FirebaseHelper.databaseInst.getReference("Inventions")
                    referenceToDelete.child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(TT).removeValue()
                    }

                    val intent = Intent(this@NewDisclosure, MainPage::class.java)
                    startActivity(intent)
                }
            }

        }

        val DisclosureTypes = findViewById<Spinner>(R.id.spinner1)

        DisclosureTypes.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                /*temp till we finish the db*/
                if (adapter != null) {
                    DT = adapter.getItemAtPosition(position).toString()
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        val SP = findViewById<StepsView>(R.id.stepsView)


        SP.setLabels(arr).setBarColorIndicator(getColor(R.color.TextColor))
            .setProgressColorIndicator(getColor(R.color.ProgressBar))
            .setLabelColorIndicator(getColor(R.color.TextColor))
            .setCompletedPosition(0)
            .drawView()


        val nextStep = findViewById<Button>(R.id.button4)

        nextStep.setOnClickListener {

            val T = Title.text.toString().trim()
            val DES = Description.text.toString()
            val FPDATE = FPD.text.toString().trim()
            val Circ = COD.text.toString()
            val keywords = SK.text.toString().trim()

            if (T.isNotEmpty() and  DT.isNotEmpty()) {

                TT=T

                if (FPDATE.isEmpty()) {


                    ToResearches(T,DES,FPDATE,Circ,keywords)


                } else if (FPDATE.isNotEmpty() and isDateValid(FPDATE)) {


                    ToResearches(T,DES,FPDATE,Circ,keywords)

                } else {

                    Toasty.error(this, " The date is invalid ", Toasty.LENGTH_SHORT).show()
                }
            }else {
                Toasty.error(this, " Title or Disclosure Types is empty", Toasty.LENGTH_SHORT).show()
            }

        }

        val fileB = findViewById<Button>(R.id.button30)

        fileB.setOnClickListener {

            val T = Title.text.toString().trim()

            if (T.isNotEmpty()) {
                openFilePicker()
            } else {
                Toasty.error(this, " Title is empty ", Toasty.LENGTH_SHORT).show()
            }

        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    fun ToResearches(invName: String, DES: String, FPDATE: String, Circ: String, keywords: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                FirebaseHelper.insertDataInInventions(
                    invName, "Received",
                    LocalDate.now().toString(), this@NewDisclosure
                )


                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val InvReference =
                        FirebaseHelper.databaseInst.getReference("Inventions").child(userId).child(invName)

                    // Update the fields that are not empty
                    InvReference.child("type").setValue(DT)

                    if (DES.isNotEmpty()) {
                        InvReference.child("description").setValue(DES)
                    }
                    if (FPDATE.isNotEmpty()) {
                        InvReference.child("first_Public_Disclosure").setValue(FPDATE)
                    }
                    if (Circ.isNotEmpty()) {
                        InvReference.child("circumstances_of_Disclosure").setValue(Circ)
                    }
                    if (keywords.isNotEmpty()) {
                        InvReference.child("keywords").setValue(keywords)
                    }

                    if(filename.isNotEmpty()){

                        InvReference.child("fileNameD").setValue(filename)

                    }

                }

                val intent = Intent(this@NewDisclosure, Researches::class.java)
                val tite = findViewById<EditText>(R.id.EditText1).text.toString().trim()
                intent.putExtra("Title", tite)
                startActivity(intent)

            } catch (e: Exception) {

                runOnUiThread {
                    Toasty.error(this@NewDisclosure, e.toString(), Toasty.LENGTH_LONG).show()
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isDateValid(inputDate: String): Boolean {
        // Define the expected date pattern
        val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

        try {
            // Parse the inputDate using the defined pattern
            val date = LocalDate.parse(inputDate, dateFormatter)

            // No exception was thrown, so the date is valid
            return true
        } catch (e: DateTimeParseException) {
            // An exception was thrown, indicating that the date is invalid
            return false
        }
    }


    private val filePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val fileUri = result.data?.data
                if (fileUri != null) {
                    uploadFileToFirebaseStorage(fileUri)
                } else {
                    Toasty.error(this, "File selection failed", Toasty.LENGTH_SHORT).show()
                }
            }
        }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        filePickerLauncher.launch(Intent.createChooser(intent, "Select File"))
    }

    // Replace your existing uploadFileToFirebaseStorage function with the following:
    private fun uploadFileToFirebaseStorage(fileUri: Uri) {

        val invName = findViewById<EditText>(R.id.EditText1).text.toString()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                val fileName = getFileName(fileUri)
                val fileId = UUID.randomUUID().toString()
                val fileRef = storageRef.child("files/$userId/D/$fileId")

                val attachedDoc = findViewById<TextView>(R.id.dummyTextView)

                if (fileName.isNotEmpty()) {

                    filename = fileName

                    attachedDoc.setText("$fileName")

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val InvReference =
                            FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                                .child(invName)
                        InvReference.child("fileNameD").setValue(filename)

                    }

                }




                val taskSnapshot = fileRef.putFile(fileUri).await()
                val downloadUri = taskSnapshot.storage.downloadUrl.await().toString()

                saveFileDataToFirebaseDatabase(fileName, fileId, downloadUri, invName)
            } catch (e: Exception) {
                // Handle exception, if any
            }
        }
    }

    private fun getFileName(fileUri: Uri): String {
        contentResolver.query(fileUri, null, null, null, null)?.use {
            if (it.moveToFirst()) {
                val displayName =
                    it.getString(it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
                if (displayName != null) {
                    return displayName
                }
            }
        }
        return UUID.randomUUID().toString()
    }

    private fun saveFileDataToFirebaseDatabase(
        fileName: String,
        fileId: String,
        downloadUrl: String,
        InventionName: String
    ) {
        val userId = auth.currentUser?.uid ?: return
        val fileData = mapOf(
            "fileName" to fileName,
            "fileId" to fileId,
            "downloadUrl" to downloadUrl
        )

        // Assuming you have a "files" node in the Firebase Realtime Database
        CoroutineScope(Dispatchers.IO).launch {
            try {
                databaseRef.child("Inventions").child(userId).child(InventionName).child("Details")
                    .child("Files").push().setValue(fileData)
                    .addOnSuccessListener {
                        // Use runOnUiThread to show Toast on the UI thread
                        runOnUiThread {
                            Toasty.success(
                                this@NewDisclosure,
                                "File uploaded successfully",
                                Toasty.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Use runOnUiThread to show Toast on the UI thread
                        runOnUiThread {
                            Toasty.error(
                                this@NewDisclosure,
                                "Failed to save file data: ${e.message}",
                                Toasty.LENGTH_SHORT
                            ).show()
                        }
                    }
            } catch (e: Exception) {
                // Handle exception, if any
            }
        }
    }

}