package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.summertraningproject.databinding.ActivityEditPageBinding

class EditPage : AppCompatActivity() {


    private lateinit var profileImage:ImageView
    companion object {
        val IMAGE_REQUEST_CODE = 1_000;
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_page)

        val back = findViewById<Button>(R.id.button1)
        back.setOnClickListener {
            val intent = Intent(this,Settings::class.java)
            startActivity(intent)
        }

        profileImage = findViewById<ImageView>(R.id.profileImage)
        profileImage.setOnClickListener {

            profileImage.clipToOutline = true
            pickImageFromGallery()

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
            profileImage.setImageURI(data?.data)
        }
    }
}
