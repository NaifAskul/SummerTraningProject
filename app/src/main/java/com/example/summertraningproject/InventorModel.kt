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
    var LastName: String? = "None",
    var CNRStartingBalance: String? = "None",
    var Citizenship: String? = "None",
    var CourtesyTitle: String? = "None",
    var CreatedBy: String? = "None",
    var Gender: String? = "None",
    var ModifiedBy: String? = "None",
    var Suffix: String? = "None",
    var PhoneNum: String? = "None",
    var TotalDistributions: String? = "None",
    var Image: String? = "None",
    var userType: String? = "None" ) : Serializable