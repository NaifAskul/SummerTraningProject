package com.example.summertraningproject

import java.io.Serializable

data class InventionModel(var createDate : String? = "None",
                          var inventionName: String? = "None",
                          var no: String? = "None",
                          var status: String? = "None") : Serializable
