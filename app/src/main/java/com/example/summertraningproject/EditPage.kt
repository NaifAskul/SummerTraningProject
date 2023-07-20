package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
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
            pickImageFromGallery()

        }

        val CourtesyTitle = findViewById<Spinner>(R.id.spinner1)
        val Citizenship = findViewById<Spinner>(R.id.spinner2)
        val Gender = findViewById<Spinner>(R.id.spinner3)

        CourtesyTitle.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                /*temp till we finish the db*/
                if (adapter != null) {
                    /*Toast.makeText(this@EditPage,"You have selected ${adapter.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()*/
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        Citizenship.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                /*temp till we finish the db*/
                if (adapter != null) {
                    /*Toast.makeText(this@EditPage,"You have selected ${adapter.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()*/
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


        }

        Gender.onItemSelectedListener = object :AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                adapter: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                /*temp till we finish the db*/
                if (adapter != null) {
                    /*Toast.makeText(this@EditPage,"You have selected ${adapter.getItemAtPosition(position).toString()}",Toast.LENGTH_SHORT).show()*/
                }


            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }


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
