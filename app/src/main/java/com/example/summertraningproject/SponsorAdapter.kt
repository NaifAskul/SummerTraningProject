package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SponsorAdapter (private val sponsors: MutableList<Sponsor>) :
    RecyclerView.Adapter<SponsorAdapter.SponsorViewHolder>() {
    class SponsorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textView6665)
        val contractTextView: TextView = itemView.findViewById(R.id.textView456)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SponsorViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.sponsor_item_layout, parent, false)
        return SponsorViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: SponsorViewHolder, position: Int) {
        val sponsor = sponsors[position]


        holder.nameTextView.text = sponsor.name
        holder.contractTextView.text = sponsor.contractNumber

    }

    override fun getItemCount() = sponsors.size
}