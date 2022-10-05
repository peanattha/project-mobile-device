package com.example.project

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReservedAPI {
    @GET("reserved/{id}")
    fun reserved_his(
        @Path("id") id: String
    ): Call<List<Reserved>>

    @GET("reserved-admin/")
    fun reserved_admin_his(): Call<List<Reserved>>

    @GET("reserved-cancel/")
    fun reserved_admin_cancel(): Call<List<Reserved>>

    @GET("reserved-confirm/")
    fun reserved_admin_confirm(): Call<List<Reserved>>

    @FormUrlEncoded
    @PUT("cancelReserved/{payment_id}")
    fun cancelReserved(
        @Path("payment_id") payment_id: String,
        @Field("payment_status") payment_status: String
    ): Call<Reserved>

    companion object {
        fun create(): ReservedAPI {
            val serv : ReservedAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ReservedAPI ::class.java)
            return serv
        }
    }
}