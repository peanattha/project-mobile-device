package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class stadium (
    @Expose
    @SerializedName("stadium_id") val stadium_id: Int,

    @Expose
    @SerializedName("stadium_name") val stadium_name: String,

    @Expose
    @SerializedName("stadium_price")val stadium_price: Int,

    @Expose
    @SerializedName("stadium_img") val stadium_img: String,

    @Expose
    @SerializedName("stadium_detail")val stadium_detail: String,

    ){}