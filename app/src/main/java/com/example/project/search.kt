package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class search (
    @Expose
    @SerializedName("stadium_id") val stadium_id: String,

    @Expose
    @SerializedName("reserve_date") val reserve_date: String,

    @Expose
    @SerializedName("time_start")val time_start: String,

    @Expose
    @SerializedName("time_end") val time_end: String
    ){}