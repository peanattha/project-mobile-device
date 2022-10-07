package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class paymentData (
    @Expose
    @SerializedName("payment_id ") val bank_name: String,

    @Expose
    @SerializedName("bank_admin_id") val firstName: String,

    @Expose
    @SerializedName("payment_status") val lastName: String,

    @Expose
    @SerializedName("slip_img") val qr_code: String,

    @Expose
    @SerializedName("reserve_id") val reserve_id: String,

    @Expose
    @SerializedName("user_id") val user_id: String,

    @Expose
    @SerializedName("stadium_id") val stadium_id: String,

    @Expose
    @SerializedName("total_price") val total_price: String,

    @Expose
    @SerializedName("reserve_date") val reserve_date:String,

    @Expose
    @SerializedName("time_start") val time_start: String,

    @Expose
    @SerializedName("time_end") val time_end: String,
){
    val message: String
        get() {
            TODO()
        }
}