package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SponAdapter(private val sponsorList: List<Sponsor>) :
    RecyclerView.Adapter<SponAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val sponsorNameTextView: TextView = view.findViewById(R.id.T5)
        val sponsorCountryTextView: TextView = view.findViewById(R.id.T88)
        val contractNumberTextView: TextView = view.findViewById(R.id.T2)
        val contactInformationTextView: TextView = view.findViewById(R.id.T3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.spon_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSponsor = sponsorList[position]
        holder.sponsorNameTextView.text = currentSponsor.name
        holder.sponsorCountryTextView.text = currentSponsor.country
        holder.contractNumberTextView.text = currentSponsor.contractNumber
        holder.contactInformationTextView.text = currentSponsor.contact_Information
    }

    override fun getItemCount(): Int {
        return sponsorList.size
    }
}
