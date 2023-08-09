package com.example.summertraningproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

class Survey : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var filename: String
    private lateinit var T: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_survey)

        val SP = findViewById<StepsView>(R.id.stepsView)


        SP.setLabels(arr).setBarColorIndicator(getColor(R.color.TextColor))
            .setProgressColorIndicator(getColor(R.color.ProgressBar))
            .setLabelColorIndicator(getColor(R.color.TextColor))
            .setCompletedPosition(3)
            .drawView()

        val back = findViewById<Button>(R.id.button1)
        val receivedData = intent.getStringExtra("Title")

        if (receivedData != null) {
            T = receivedData
        }

        back.setOnClickListener {

            CoroutineScope(Dispatchers.Main).launch {

                val intent = Intent(this@Survey, Questions::class.java)
                startActivity(intent)

            }

        }

        val buttonNext = findViewById<Button>(R.id.button4)

        buttonNext.setOnClickListener {

            saveDisclosureDataToFirebase()

        }

        val fileB = findViewById<Button>(R.id.button30)

        fileB.setOnClickListener {

            if (T.isNotEmpty()) {
                openFilePicker()
            }

        }

    }

    private fun saveDisclosureDataToFirebase() {
        // Get references to the views containing user input
        val radioGroup1 = findViewById<RadioGroup>(R.id.RG1)
        val radioGroup2 = findViewById<RadioGroup>(R.id.RG2)
        val radioGroup3 = findViewById<RadioGroup>(R.id.RG3)
        val radioGroup4 = findViewById<RadioGroup>(R.id.RG4)

        // Get the selected radio button values
        val isDateOfConceptionDocumented = getSelectedRadioButtonText(radioGroup1)
        val isPriorArtPatentsSearched = getSelectedRadioButtonText(radioGroup2)
        val isPrototypeMade = getSelectedRadioButtonText(radioGroup3)
        val isPriorPublicDisclosure = getSelectedRadioButtonText(radioGroup4)


        if ((isPrototypeMade !=null) and (isDateOfConceptionDocumented != null) and (isPriorArtPatentsSearched != null) and (isPriorPublicDisclosure != null)) {

            val userId = FirebaseAuth.getInstance().currentUser?.uid
            if (userId != null) {
                val InvReference =
                    FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                        .child(T)

                InvReference.child("prototypeMade").setValue(isPrototypeMade)
                InvReference.child("dateOfConceptionDocumented").setValue(isDateOfConceptionDocumented)
                InvReference.child("priorArtPatentsSearched").setValue(isPriorArtPatentsSearched)
                InvReference.child("priorPublicDisclosure").setValue(isPriorPublicDisclosure)

                val intent = Intent(this@Survey, Confirm::class.java)
                intent.putExtra("Title", T)
                startActivity(intent)
            }


        }else{

            Toasty.error(
                this@Survey,
                "check the selections",
                Toasty.LENGTH_SHORT
            ).show()

        }

    }

    // Helper function to get the text of the selected radio button in a RadioGroup
    private fun getSelectedRadioButtonText(radioGroup: RadioGroup): String? {
        val selectedRadioButtonId = radioGroup.checkedRadioButtonId
        if (selectedRadioButtonId != -1) {
            val selectedRadioButton = findViewById<RadioButton>(selectedRadioButtonId)
            return selectedRadioButton.text.toString()
        }
        return null
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

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val userId = auth.currentUser?.uid ?: return@launch
                val fileName = getFileName(fileUri)
                val fileId = UUID.randomUUID().toString()
                val fileRef = storageRef.child("files/$userId/S/$fileId")

                val attachedDoc = findViewById<TextView>(R.id.dummyTextView)

                if (fileName.isNotEmpty()) {

                    filename = fileName

                    attachedDoc.setText("$fileName")

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val InvReference =
                            FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                                .child(T)
                        InvReference.child("fileNameS").setValue(filename)

                    }

                }




                val taskSnapshot = fileRef.putFile(fileUri).await()
                val downloadUri = taskSnapshot.storage.downloadUrl.await().toString()

                saveFileDataToFirebaseDatabase(fileName, fileId, downloadUri, T)
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
                                this@Survey,
                                "File uploaded successfully",
                                Toasty.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Use runOnUiThread to show Toast on the UI thread
                        runOnUiThread {
                            Toasty.error(
                                this@Survey,
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
