package com.example.project

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface stadiumAPI {
    @GET("allstadium")
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
    @PUT("stadium/{stadium_id}")
    fun softDeleteStadium(
        @Path("stadium_id") stadium_id: String,
        @Field("delete_at") delete_at: String): Call<stadium>

    @Multipart
    @POST("/stadium")
    fun add(
        @Part image: MultipartBody.Part,
        @Part("stadium_name") stadium_name: RequestBody,
        @Part("stadium_price") stadium_price: RequestBody,
        @Part("stadium_detail") stadium_detail: RequestBody,
        @Part("create_at") create_at: RequestBody
    ): Call<UploadResponse>

    @Multipart
    @PUT("stadium/{stadium_id}")
    fun updateStadium(
        @Part image: MultipartBody.Part,
        @Path("stadium_id") stadium_id: String,
        @Part("stadium_name") stadium_name: RequestBody,
        @Part("stadium_price") stadium_price: RequestBody,
        @Part("stadium_detail") stadium_detail: RequestBody,
        @Part("update_at") update_at: RequestBody
    ): Call<UploadResponse>

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