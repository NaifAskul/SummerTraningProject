package com.example.summertraningproject

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate

class NewDisclosure : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_disclosure)

        /* I will deal with it in another time */

        CoroutineScope(Dispatchers.Main).launch {
            try {

                FirebaseHelper.insertDataInInventions(
                    "electric car", "Received",
                    LocalDate.now().toString(), this@NewDisclosure
                )
            } catch (e: Exception) {

            }
        }

    }
}