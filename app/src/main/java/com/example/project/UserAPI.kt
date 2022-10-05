package com.example.project

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UserAPI {
    @GET("userDetail/{id}")
    fun userDetail(
        @Path("id") id: String
    ): Call<List<User>>

    @FormUrlEncoded
    @PUT("edit_profile/{id}")
    fun editUser(
        @Path("id") id: String,
        @Field("user_email") user_email: String,
        @Field("user_firstName") user_firstName: String,
        @Field("user_lastName") user_lastName: String,
        @Field("user_tel") user_tel: String
    ): Call<User>

    @GET("login/{user}/{pass}")
    fun retrieveUser(
        @Path("user") user: String,
        @Path("pass") pass: String
    ): Call<User>

    companion object {
        fun create(): UserAPI {
            val serv : UserAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserAPI ::class.java)
            return serv
        }
    }
}