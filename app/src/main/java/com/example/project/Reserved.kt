package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Reserved(
    @Expose
    @SerializedName("reserve_id") val reserve_id: String,

    @Expose
    @SerializedName("user_id") val user_id: String,

    @Expose
    @SerializedName("total_price") val total_price: String,

    @Expose
    @SerializedName("reserve_date") val reserve_date:String,

    @Expose
    @SerializedName("time_start") val time_start: String,

    @Expose
    @SerializedName("time_end") val time_end: String,

    @Expose
    @SerializedName("slip_img") val slip_img: String,

    @Expose
    @SerializedName("payment_status") val payment_status: String,

    @Expose
    @SerializedName("stadium_name") val stadium_name: String,

    @Expose
    @SerializedName("stadium_img") val stadium_img: String,

    @Expose
    @SerializedName("payment_id") val payment_id: String,
){}