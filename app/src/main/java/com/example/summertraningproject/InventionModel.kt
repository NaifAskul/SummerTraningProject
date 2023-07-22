package com.example.summertraningproject

import java.io.Serializable

data class InventionModel(var No : String? = "None",
                          var inventionName: String? = "None",
                          var status: String? = "None",
                          var CreateDate: String? = "None") : Serializable
