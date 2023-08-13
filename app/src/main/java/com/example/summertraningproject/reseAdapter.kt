package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class reseAdapter(private val dataList: List<rese>) :
    RecyclerView.Adapter<reseAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewT1: TextView = itemView.findViewById(R.id.T88)
        val textViewT2: TextView = itemView.findViewById(R.id.T2)
        val textViewT6: TextView = itemView.findViewById(R.id.T6)
        val textViewT4: TextView = itemView.findViewById(R.id.T4)
        val textViewT5: TextView = itemView.findViewById(R.id.T5)

        // Add references to other views if necessary
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.disclosure_researchers_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]

        // Bind the data to the views
        holder.textViewT1.text = dataItem.contribution
        holder.textViewT2.text = dataItem.name
        holder.textViewT6.text = dataItem.organization
        holder.textViewT4.text = dataItem.email
        holder.textViewT5.text = dataItem.lead.toString()

        // Bind other data if necessary
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
