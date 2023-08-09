package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FileDataAdapter(private val dataList: List<files>) :
    RecyclerView.Adapter<FileDataAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFileName: TextView = itemView.findViewById(R.id.T1)
        val textViewFileID: TextView = itemView.findViewById(R.id.T2)
        val textViewFileDownloadURL: TextView = itemView.findViewById(R.id.T3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.related_files_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataItem = dataList[position]

        // Bind the data to the views
        holder.textViewFileName.text = dataItem.fileName ?: "None"
        holder.textViewFileID.text = dataItem.fileId ?: "None"
        holder.textViewFileDownloadURL.text = dataItem.downloadUrl ?: "None"
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}
