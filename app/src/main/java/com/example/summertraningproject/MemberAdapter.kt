package com.example.summertraningproject

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MemberAdapter(private val members: MutableList<Member>) :
    RecyclerView.Adapter<MemberAdapter.MemberViewHolder>() {

    private var newContribution: String = ""

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rad: RadioButton = itemView.findViewById(R.id.rad)
        val contributionEditText: EditText = itemView.findViewById(R.id.textView4445)
        val nameTextView: TextView = itemView.findViewById(R.id.textView6665)
        val organizationTextView: TextView = itemView.findViewById(R.id.textView456)
        val emailTextView: TextView = itemView.findViewById(R.id.textView4566)
        var textWatcher: TextWatcher? = null
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
                // Loop through the members list
                for (i in members.indices) {
                    val currentMember = members[i]
                    val isSelectedLeader = i == position
                    currentMember.isLead = isSelectedLeader

                    // Exclude the selectedLeaderId and update the lead field for each member
                    val selectedLeaderId = if (isSelectedLeader) member.memberId else null
                    val leadRef = FirebaseHelper.databaseInst.getReference("Inventions")
                        .child(currentMember.InvOwner)
                        .child(currentMember.InvName)
                        .child("members")
                        .child(currentMember.memberId.toString())
                        .child("lead")

                    leadRef.setValue(isSelectedLeader)
                }

                notifyDataSetChanged()
            }


        holder.contributionEditText.addTextChangedListener(object : TextWatcher {


            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                // Update the newContribution variable whenever the text in the EditText changes
                 newContribution = s?.toString() ?: ""

                // Update Firebase Realtime Database with the new contribution
                val memberRef = FirebaseHelper.databaseInst.getReference("Inventions")
                    .child(member.InvOwner)
                    .child(member.InvName)
                    .child("members")
                    .child(member.memberId.toString())

                memberRef.child("contribution").setValue(newContribution)
            }
        })

    }

    override fun getItemCount() = members.size


}
