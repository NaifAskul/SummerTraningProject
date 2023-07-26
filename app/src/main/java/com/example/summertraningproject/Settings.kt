package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.google.firebase.auth.FirebaseAuth
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

const val FIRST_START = "FirstStart"
const val NIGHT_MODE = "NightMode"
const val PREF = "AppSettingsPrefs"

class Settings : AppCompatActivity() {

    private val mainScope = MainScope()

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        val appSettingsPrefs: SharedPreferences = getSharedPreferences(PREF, 0)
        val isNightModeOn: Boolean = appSettingsPrefs.getBoolean(NIGHT_MODE, true)
        val isFirstStart: Boolean = appSettingsPrefs.getBoolean(FIRST_START, false)
        val editor: SharedPreferences.Editor = appSettingsPrefs.edit()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && isFirstStart) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        } else {
            when {
                isNightModeOn -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                else -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }

        mainScope.launch {
            getInventorData(this@Settings)
        }

        val switch = findViewById<Switch>(R.id.switch1)

        switch.setOnClickListener {
            if (isNightModeOn) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                editor.putBoolean(FIRST_START, false)
                editor.putBoolean(NIGHT_MODE, false)
                editor.apply()
                //recreate activity to make changes visible
                recreate()
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                editor.putBoolean(FIRST_START, false)
                editor.putBoolean(NIGHT_MODE, true)
                editor.apply()
                recreate()

            }
        }

        val back = findViewById<Button>(R.id.button1)
        back.setOnClickListener {
            val intent = Intent(this, MainPage::class.java)
            startActivity(intent)
        }

        val edit = findViewById<ImageView>(R.id.imageView2)
        edit.setOnClickListener {
            val intent = Intent(this, EditPage::class.java)
            startActivity(intent)
        }

        val SignOut = findViewById<Button>(R.id.button)
        SignOut.setOnClickListener {
            FirebaseHelper.signOut()
            val intent = Intent(this, MainActivity::class.java)
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
                val invnum = dataSnapshot.child("inventorID").value.toString()
                val invPhNum = dataSnapshot.child("PhoneNum").value.toString()
                val CNR = dataSnapshot.child("CNRStartingBalance").value.toString()
                val Citizenship= dataSnapshot.child("Citizenship").value.toString()
                val CourtesyTitle = dataSnapshot.child("CourtesyTitle").value.toString()
                val CreatedBy = dataSnapshot.child("CreatedBy").value.toString()
                val InvStartDate = dataSnapshot.child("EmployeeStartDate").value.toString()
                val Gender = dataSnapshot.child("Gender").value.toString()
                val ModifiedBy = dataSnapshot.child("ModifiedBy").value.toString()
                val Suffix = dataSnapshot.child("Suffix").value.toString()
                val TotalDistributions = dataSnapshot.child("TotalDistributions").value.toString()

                withContext(Main) {
                    val ProfileName = findViewById<TextView>(R.id.textView1118)
                    val job = findViewById<TextView>(R.id.textView5555)
                    val email = findViewById<TextView>(R.id.textView55766)
                    val Interests = findViewById<TextView>(R.id.textView5576)
                    val invenNum = findViewById<TextView>(R.id.textView456)
                    val cnr = findViewById<TextView>(R.id.textView6667)
                    val citizenship = findViewById<TextView>(R.id.textView4445)
                    val courtesyTitle = findViewById<TextView>(R.id.textView55555)
                    val createdBy = findViewById<TextView>(R.id.textView543)
                    val invSD = findViewById<TextView>(R.id.textView6665)
                    val gender = findViewById<TextView>(R.id.textView78)
                    val modifiedBy = findViewById<TextView>(R.id.textView995)
                    val suffix = findViewById<TextView>(R.id.textView11111)
                    val totalDistributions = findViewById<TextView>(R.id.textView123)
                    val phoneNum = findViewById<TextView>(R.id.textView4566)

                    ProfileName.text = "$F $M $L"
                    job.text = J
                    email.text = E
                    Interests.text = I
                    invenNum.text = invnum
                    phoneNum.text = invPhNum
                    cnr.text = "$CNR SAR"
                    citizenship.text = Citizenship
                    courtesyTitle.text = CourtesyTitle
                    createdBy.text = CreatedBy
                    invSD.text = InvStartDate
                    gender.text = Gender
                    modifiedBy.text = ModifiedBy
                    suffix.text = Suffix
                    totalDistributions.text = "$TotalDistributions SAR"
                }
            } else {
                withContext(Main) {
                    Toasty.warning(context, "User doesn't exist", Toasty.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            withContext(Main) {
                Toasty.error(context, "An error has occurred", Toasty.LENGTH_SHORT).show()
            }
        }
    }


    override fun onPause() {
        Log.i(TAG, "onPause")
        super.onPause()
    }

    override fun onStop() {
        Log.i(TAG, "onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.i(TAG, "onDestroy")
        super.onDestroy()
    }
}