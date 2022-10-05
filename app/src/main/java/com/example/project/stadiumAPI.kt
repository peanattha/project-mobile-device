package com.example.project

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface stadiumAPI {
    @GET("allstadiun")//แก้ตรงนี้
    fun retrieveStadium(): Call<List<stadium>>

    @FormUrlEncoded
    @POST("search/")
    fun retrieveStadiumID(
        @Field("id") id: String,
        @Field("reserve_date") reserve_date: String,
        @Field("time_start") time_start: String,
        @Field("time_end") time_end: String
    ): Call<List<search>>


    companion object {
        fun create(): stadiumAPI {
            val serv : stadiumAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(stadiumAPI::class.java )
            return serv
        }
    }
}