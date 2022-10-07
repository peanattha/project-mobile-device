package com.example.project

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface stadiumAPI {
    @GET("allstadiun")
    fun retrieveStadium(): Call<List<stadium>>

    @FormUrlEncoded
    @POST("search/")
    fun retrieveStadiumID(
        @Field("id") id: String,
        @Field("reserve_date") reserve_date: String,
        @Field("time_start") time_start: String,
        @Field("time_end") time_end: String
    ): Call<search>

    @FormUrlEncoded
    @POST("stadium")
    fun insertStadium(
        @Field("stadium_name") stadium_name: String,
        @Field("stadium_price") stadium_price: Int,
        @Field("stadium_img") stadium_img: String,
        @Field("stadium_detail") stadium_detail: String,
        @Field("create_at") create_at: String): Call<stadium>

    @FormUrlEncoded
    @PUT("stadium/{stadium_id}")
    fun updateStadium(
        @Path("stadium_id") stadium_id: String,
        @Field("stadium_name") stadium_name: String,
        @Field("stadium_price") stadium_price: Int,
        @Field("stadium_img") stadium_img: String,
        @Field("stadium_detail") stadium_detail: String,
        @Field("update_at") update_at: String): Call<stadium>

    @FormUrlEncoded
    @PUT("stadium/{stadium_id}")
    fun softDeleteStadium(
        @Path("stadium_id") stadium_id: String,
        @Field("delete_at") delete_at: String): Call<stadium>

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