package com.example.summertraningproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val members: MutableList<Member>) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rad: RadioButton = itemView.findViewById(R.id.rad)
        val contributionEditText: EditText = itemView.findViewById(R.id.textView4445)
        val nameTextView: TextView = itemView.findViewById(R.id.textView6665)
        val organizationTextView: TextView = itemView.findViewById(R.id.textView456)
        val emailTextView: TextView = itemView.findViewById(R.id.textView4566)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.member_item_layout, parent, false)
        return MemberViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MemberViewHolder, position: Int) {
        val member = members[position]

        holder.rad.isChecked = member.isLead
        holder.contributionEditText.setText(member.contribution)
        holder.nameTextView.text = member.name
        holder.organizationTextView.text = member.organization
        holder.emailTextView.text = member.email

        // Handle RadioButton click
        holder.rad.setOnClickListener {
            for (i in members.indices) {
                members[i].isLead = i == position
            }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount() = members.size
}
