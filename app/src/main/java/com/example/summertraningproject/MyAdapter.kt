package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val inventionsList : ArrayList<InventionModel> , private val onItemClickListener: (InventionModel) -> Unit) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item,
            parent,false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = inventionsList[position]

        holder.createDate.text = currentitem.createDate
        holder.No.text = currentitem.no
        holder.inventionName.text = currentitem.inventionName
        holder.status.text = currentitem.status

        holder.itemView.setOnClickListener {
            onItemClickListener.invoke(currentitem)
        }

    }

    override fun getItemCount(): Int {

        return inventionsList.size
    }


    class MyViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){

        val createDate : TextView = itemView.findViewById(R.id.cdate)
        val inventionName : TextView = itemView.findViewById(R.id.invname)
        val No: TextView = itemView.findViewById(R.id.no)
        val status : TextView = itemView.findViewById(R.id.statuss)

    }

}