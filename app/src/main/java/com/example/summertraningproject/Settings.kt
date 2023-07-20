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
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

const val FIRST_START = "FirstStart"
const val NIGHT_MODE = "NightMode"
const val PREF = "AppSettingsPrefs"

class Settings : AppCompatActivity() {


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