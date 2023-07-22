package com.example.summertraningproject

import android.app.Application
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()

        // Initialize Firebase Realtime Database
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        // Initialize Firebase Authentication
        FirebaseAuth.getInstance()

    }

}