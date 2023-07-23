package com.example.summertraningproject

import java.io.Serializable

data class InventorModel  (
    var Email : String? = "None",
    var password: String? = "None",
    var inventorID: String? = "None",
    var interests: String? = "None",
    var Job: String? = "None",
    var FirstName : String? = "None",
    var MiddleName: String? = "None",
    var LastName: String? = "None") : Serializable