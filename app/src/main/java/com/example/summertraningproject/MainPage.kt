package com.example.summertraningproject

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.summertraningproject.databinding.ActivityMainPageBinding

class MainPage : AppCompatActivity() {

    private lateinit var binding: ActivityMainPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(Home())

        binding.bottomNav.setOnItemSelectedListener {

            val intent1 = Intent(this,OrderStatus::class.java)
            val intent2 = Intent(this,Settings::class.java)


            when(it.itemId){

                R.id.home -> replaceFragment(Home())
                R.id.Status -> startActivity(intent1)
                R.id.Settings -> startActivity(intent2)


                else ->{



                }

            }

            true

        }

    }

    private fun replaceFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
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