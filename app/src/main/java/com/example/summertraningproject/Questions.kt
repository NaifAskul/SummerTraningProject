package com.example.summertraningproject

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import com.anton46.stepsview.StepsView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.UUID

class Questions : AppCompatActivity() {

    val arr = arrayOf("Details", "Researchers", "Funding", "Questions", "Confirm")
    private val storageRef = FirebaseStorage.getInstance().reference
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val auth = FirebaseAuth.getInstance()
    private lateinit var filename: String
    private lateinit var T: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)

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

                    val intent = Intent(this@Questions, Funding::class.java)
                    startActivity(intent)

            }

        }

        val nextStep = findViewById<Button>(R.id.button4)

        // Variables to hold references to the EditText views inside the CardView
        var Q1: EditText = findViewById(R.id.textView45616)
        var Q2 : EditText = findViewById(R.id.textView457)
        var Q3: EditText = findViewById(R.id.textView4000)
        var Q4 : EditText = findViewById(R.id.textView45604)
        var Q5: EditText = findViewById(R.id.textView4560)
        var Q6 : EditText = findViewById(R.id.textView45602)
        var Q7: EditText = findViewById(R.id.textView45603)
        var Q8 : EditText = findViewById(R.id.textView456022)
        var Q9: EditText = findViewById(R.id.textView4560222)
        var Q10 : EditText = findViewById(R.id.textView45602222)
        var Q11: EditText = findViewById(R.id.textView456032)

        val C1 = findViewById<TextView>(R.id.c1)
        val C2 = findViewById<TextView>(R.id.c2)
        val C3 = findViewById<TextView>(R.id.c3)
        val C4 = findViewById<TextView>(R.id.c4)
        val C5 = findViewById<TextView>(R.id.c5)
        val C6 = findViewById<TextView>(R.id.c6)
        val C7 = findViewById<TextView>(R.id.c7)

        Q1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C1.text = "$wordCount"
            }
        })

        Q4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C2.text = "$wordCount"
            }
        })

        Q5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C3.text = "$wordCount"
            }
        })

        Q6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C4.text = "$wordCount"
            }
        })

        Q7.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C5.text = "$wordCount"
            }
        })

        Q8.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C6.text = "$wordCount"
            }
        })

        Q9.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // Not needed for word counting
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Not needed for word counting
            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString() ?: ""
                val wordCount = countWords(text)
                C7.text = "$wordCount"
            }
        })


        nextStep.setOnClickListener {


            GlobalScope.launch(Dispatchers.Main) {

                // Coroutine code will go here
                val q1 = Q1.text.toString().trim()
                val q2 = Q2.text.toString().trim()
                val q3 = Q3.text.toString().trim()
                val q4 = Q4.text.toString().trim()
                val q5 = Q5.text.toString().trim()
                val q6 = Q6.text.toString().trim()
                val q7 = Q7.text.toString().trim()
                val q8 = Q8.text.toString().trim()
                val q9 = Q9.text.toString().trim()
                val q10 = Q10.text.toString().trim()
                val q11 = Q11.text.toString().trim()

                val q1WordCount = countWords(q1)
                val q4WordCount = countWords(q4)
                val q5WordCount = countWords(q5)
                val q6WordCount = countWords(q6)
                val q7WordCount = countWords(q7)
                val q8WordCount = countWords(q8)
                val q9WordCount = countWords(q9)




                val userId = FirebaseAuth.getInstance().currentUser?.uid
                if (userId != null) {
                    val InvReference =
                        FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                            .child(T)

                    if (q1.isEmpty() or (q1WordCount < 120)) {
                        Toasty.error(this@Questions, "Q1 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q3.isEmpty()) {
                        Toasty.error(this@Questions, "Q3 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q5.isEmpty() or (q5WordCount< 200)) {
                        Toasty.error(this@Questions, "Q5 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q6.isEmpty() or (q6WordCount < 100)) {
                        Toasty.error(this@Questions, "Q6 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q7.isEmpty() or (q7WordCount < 600)) {
                        Toasty.error(this@Questions, "Q7 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q8.isEmpty() or (q8WordCount < 700)) {
                        Toasty.error(this@Questions, "Q8 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q9.isEmpty() or (q9WordCount < 100)) {
                        Toasty.error(this@Questions, "Q9 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else if (q11.isEmpty()) {
                        Toasty.error(this@Questions, "Q11 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                            .show()
                    } else {

                        InvReference.child("intro").setValue(q1)

                        if(q2.isNotEmpty()) {
                            InvReference.child("invDomain").setValue(q2)
                        }

                        InvReference.child("suggested_Keywords").setValue(q3)

                        if (q4.isNotEmpty() and (q4WordCount >= 700)) {
                            InvReference.child("literature_review").setValue(q4)
                        }else{
                            Toasty.error(this@Questions, "Q4 is empty or the answer is too short", Toasty.LENGTH_SHORT)
                                .show()
                        }

                        InvReference.child("state_the_problem").setValue(q5)
                        InvReference.child("disadvantages_of_the_invention").setValue(q6)
                        InvReference.child("proposed_solution").setValue(q7)
                        InvReference.child("methodology").setValue(q8)
                        InvReference.child("advantages_of_the_invention").setValue(q9)

                        if (q10.isNotEmpty()) {
                            InvReference.child("preliminary_results").setValue(q10)
                        }

                        InvReference.child("references").setValue(q11)

                        val receivedData = intent.getStringExtra("Title")
                        val intent = Intent(this@Questions, Survey::class.java)
                        intent.putExtra("Title", receivedData)
                        startActivity(intent)

                    }
                }
            }
        }

        val fileB = findViewById<Button>(R.id.button30)

        fileB.setOnClickListener {

            if (T.isNotEmpty()) {
                openFilePicker()
            }

        }

    }

    private fun countWords(text: String): Int {
        val words = text.trim().split("\\s+".toRegex())
        return words.filter { it.isNotBlank() }.size
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
                val fileRef = storageRef.child("files/$userId/Q/$fileId")

                val attachedDoc = findViewById<TextView>(R.id.dummyTextView)

                if (fileName.isNotEmpty()) {

                    filename = fileName

                    attachedDoc.setText("$fileName")

                    val userId = FirebaseAuth.getInstance().currentUser?.uid
                    if (userId != null) {
                        val InvReference =
                            FirebaseHelper.databaseInst.getReference("Inventions").child(userId)
                                .child(T)
                        InvReference.child("fileNameQ").setValue(filename)

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
                                this@Questions,
                                "File uploaded successfully",
                                Toasty.LENGTH_SHORT
                            ).show()
                        }
                    }
                    .addOnFailureListener { e ->
                        // Use runOnUiThread to show Toast on the UI thread
                        runOnUiThread {
                            Toasty.error(
                                this@Questions,
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