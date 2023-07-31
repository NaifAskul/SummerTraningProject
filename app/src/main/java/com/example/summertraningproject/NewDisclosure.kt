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

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_disclosure)

        val back = findViewById<Button>(R.id.button1)

        back.setOnClickListener {
            val intent = Intent(this,MainPage::class.java)
            startActivity(intent)
        }

        val Title = findViewById<EditText>(R.id.EditText1)
        val Description = findViewById<EditText>(R.id.EditText22)
        val FPD = findViewById<EditText>(R.id.textView6658)
        val COD = findViewById<EditText>(R.id.EditText)
        val SK = findViewById<EditText>(R.id.descriptionEditText)

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


                if (!Title.text.trim().isEmpty()) {

                    if (FPD.text.trim().toString().isEmpty()) {

                        ToResearches()

                    } else if ( !FPD.text.trim().toString().isEmpty() and isDateValid(FPD.text.trim().toString())){

                        ToResearches()

                    }else{

                        Toasty.error(this, " The date is invalid ", Toasty.LENGTH_SHORT).show()
                    }

                }else{
                    Toasty.error(this, " Title is empty ", Toasty.LENGTH_SHORT).show()
                }
            }

                    val fileB = findViewById<Button>(R.id.button30)

                    fileB.setOnClickListener {

                        if (!Title.text.trim().isEmpty()) {
                            openFilePicker()
                        }else{
                            Toasty.error(this, " Title is empty ", Toasty.LENGTH_SHORT).show()
                        }

                    }


            }


fun ToResearches(){

    val intent = Intent(this, Researches::class.java)

    val TitlE = findViewById<EditText>(R.id.EditText1)

    val tite = TitlE.text.toString().trim()

    // Attach the data as an extra to the intent
    intent.putExtra("Title", tite)

    startActivity(intent)

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

    private fun uploadFileToFirebaseStorage(fileUri: Uri) {
        val userId = auth.currentUser?.uid ?: return
        val fileName = getFileName(fileUri)
        val fileId = UUID.randomUUID().toString()
        val fileRef = storageRef.child("files/$userId/$fileId")
        val invName = findViewById<EditText>(R.id.EditText1)

        fileRef.putFile(fileUri)
            .addOnSuccessListener {
                fileRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    saveFileDataToFirebaseDatabase(fileName, fileId, downloadUri.toString(), invName.text.toString())
                }
            }
            .addOnFailureListener { e ->
                Toasty.error(this, "Upload failed: ${e.message}", Toasty.LENGTH_SHORT).show()
            }
    }


    private fun getFileName(fileUri: Uri): String {
        contentResolver.query(fileUri, null, null, null, null)?.use {
            if (it.moveToFirst()) {
                val displayName = it.getString(it.getColumnIndex(MediaStore.Files.FileColumns.DISPLAY_NAME))
                if (displayName != null) {
                    return displayName
                }
            }
        }
        return UUID.randomUUID().toString()
    }

    private fun saveFileDataToFirebaseDatabase(fileName: String, fileId: String, downloadUrl: String,InventionName :String) {
        val userId = auth.currentUser?.uid ?: return
        val fileData = mapOf(
            "fileName" to fileName,
            "fileId" to fileId,
            "downloadUrl" to downloadUrl
        )

        // Assuming you have a "files" node in the Firebase Realtime Database
        databaseRef.child("Inventions").child(userId).child(InventionName).child("Files").push().setValue(fileData)
            .addOnSuccessListener {
                Toasty.success(this, "File uploaded successfully", Toasty.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toasty.error(this, "Failed to save file data: ${e.message}", Toasty.LENGTH_SHORT).show()
            }
    }

}