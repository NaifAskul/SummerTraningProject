package com.example.summertraningproject

import android.os.Parcel
import android.os.Parcelable
import java.io.Serializable

data class InventionModel(var createDate : String? = "None",
                          var inventionName: String? = "None",
                          var no: String? = "None",
                          var status: String? = "None",
                          var type : String? = "None",
                          var description: String? = "None",
                          var first_Public_Disclosure: String? = "None",
                          var circumstances_of_Disclosure: String? = "None",
                          var suggested_Keywords : String? = "None",
                          var fileNameD: String? = "None",
                          var fileNameS: String? = "None",
                          var intro: String? = "None",
                          var invDomain: String? = "None",
                          var keywords : String? = "None",
                          var literature_review: String? = "None",
                          var state_the_problem: String? = "None",
                          var proposed_solution: String? = "None",
                          var preliminary_results  : String? = "None",
                          var advantages_of_the_invention: String? = "None",
                          var disadvantages_of_the_invention: String? = "None",
                          var references: String? = "None",
                          var fileNameQ: String? = "None") : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(createDate)
        parcel.writeString(inventionName)
        parcel.writeString(no)
        parcel.writeString(status)
        parcel.writeString(type)
        parcel.writeString(description)
        parcel.writeString(first_Public_Disclosure)
        parcel.writeString(circumstances_of_Disclosure)
        parcel.writeString(suggested_Keywords)
        parcel.writeString(fileNameD)
        parcel.writeString(fileNameS)
        parcel.writeString(intro)
        parcel.writeString(invDomain)
        parcel.writeString(keywords)
        parcel.writeString(literature_review)
        parcel.writeString(state_the_problem)
        parcel.writeString(proposed_solution)
        parcel.writeString(preliminary_results)
        parcel.writeString(advantages_of_the_invention)
        parcel.writeString(disadvantages_of_the_invention)
        parcel.writeString(references)
        parcel.writeString(fileNameQ)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InventionModel> {
        override fun createFromParcel(parcel: Parcel): InventionModel {
            return InventionModel(parcel)
        }

        override fun newArray(size: Int): Array<InventionModel?> {
            return arrayOfNulls(size)
        }
    }
}
