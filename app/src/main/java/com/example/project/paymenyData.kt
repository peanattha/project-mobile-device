package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class paymentData (
    @Expose
    @SerializedName("bank_admin_id") val bank_admin_id: Int,

    @Expose
    @SerializedName("bank_name") val bank_name: String,

    @Expose
    @SerializedName("account_number") val account_number:String,

    @Expose
    @SerializedName("firstName") val firstName: String,

    @Expose
    @SerializedName("lastName") val lastName: String,

    @Expose
    @SerializedName("qr_code") val qr_code: String,
){
}