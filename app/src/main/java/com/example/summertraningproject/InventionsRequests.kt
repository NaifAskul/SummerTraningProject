package com.example.summertraningproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import es.dmoral.toasty.Toasty
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class InventionsRequests : AppCompatActivity() {

    private val mainScope = MainScope()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventions_requests)

        val search = findViewById<Button>(R.id.button4)
        val noDis = findViewById<TextView>(R.id.textView5)
        noDis.visibility = View.VISIBLE

        search.setOnClickListener {


            mainScope.launch {

                val id = findViewById<EditText>(R.id.EditText99)

                if (id.text.toString().trim().isNotEmpty()
                ) {
                    FirebaseHelper.getInventionsList(
                        this@InventionsRequests,
                        findViewById(R.id.recyclerView),
                        findViewById(R.id.textView5),
                        id.text.toString().trim()
                    )
                } else {
                    Toasty.error(
                        this@InventionsRequests,
                        " There is an empty field ",
                        Toasty.LENGTH_SHORT
                    ).show()
                }
            }

        }
        val back = findViewById<Button>(R.id.button1)
        back.setOnClickListener {
            val intent = Intent(this, CertifiedArbitrator::class.java)
            startActivity(intent)
        }

    }
}