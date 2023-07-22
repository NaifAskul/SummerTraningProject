package com.example.summertraningproject

import java.io.Serializable

data class InventorModel  (
    var Email : String? = "None",
    var password: String? = "None",
    var inventorID: String? = "None",
    var interests: String? = "None") : Serializable