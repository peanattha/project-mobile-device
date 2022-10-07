package com.example.project

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface paymentAPI {
    @GET("bank/")
    fun callpayment(): Call<List<Bank>>

    @Multipart
    @POST("upload")
    fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("desc") desc: RequestBody
    ): Call<UploadResponse>


    @Multipart
    @POST("paymentadd")
    fun insertpay(
        @Part image: MultipartBody.Part,
        @Part("bank_admin_id") bank_admin_id: RequestBody,
        @Part("payment_status") payment_status: RequestBody,
        @Part("reserve_id") reserve_id: RequestBody,
        @Part("user_id") user_id: RequestBody,
        @Part("stadium_id") stadium_id: RequestBody,
        @Part("total_price") total_price: RequestBody,
        @Part("reserve_date") reserve_date: RequestBody,
        @Part("time_start") time_start: RequestBody,
        @Part("time_end") time_end: RequestBody
    ): Call<UploadResponse>

    companion object {
        operator fun invoke(): paymentAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val serv : paymentAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(paymentAPI::class.java )
            return serv
        }
    }
}