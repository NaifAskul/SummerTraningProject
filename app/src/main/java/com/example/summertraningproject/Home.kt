package com.example.summertraningproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class Home : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val inventorHomepage = view.findViewById<ImageButton>(R.id.imageButton2)

        inventorHomepage.setOnClickListener {
            val intent = Intent(getActivity(),InventorHomepage::class.java)
            startActivity(intent)
        }


    }
}