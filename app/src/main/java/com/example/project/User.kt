package com.example.project

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class User(
    @Expose
    @SerializedName("user_id") val user_id: String,

    @Expose
    @SerializedName("user_email") val user_email: String,

    @Expose
    @SerializedName("user_password") val user_password:String,

    @Expose
    @SerializedName("user_firstName") val user_firstName: String,

    @Expose
    @SerializedName("user_lastName") val user_lastName: String,

    @Expose
    @SerializedName("user_tel") val user_tel: String,

    @Expose
    @SerializedName("is_admin") val is_admin: String

){}