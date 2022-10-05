package com.example.project

import retrofit2.Call
import retrofit2.http.*

interface RegisterAPI {

    @FormUrlEncoded
    @POST("register")
    fun insertUser(
        @Field("user_email") user_email: String,
        @Field("user_password") user_password: String,
        @Field("user_firstName") user_firstName: String,
        @Field("user_lastName") user_lastName: String,
        @Field("user_tel") user_tel: String,
        @Field("is_admin") is_admin: String,): Call<RegisterData>

}