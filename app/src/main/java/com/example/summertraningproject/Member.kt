package com.example.summertraningproject

data class Member(
    var InvName: String = "",
    var InvOwner: String = "",
    var memberId: String = "", // Unique ID of the member
    var isLead: Boolean = false,
    var contribution: String = "0.00",
    var name: String = "",
    var organization: String = "",
    var email: String = "",
    var signOffBy: String = ""
)
