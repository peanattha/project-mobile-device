package com.example.project

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface paymentAPI {
    @GET("bank/")
    fun callpayment(): Call<List<Bank>>

//    @Multipart
//    @POST("upload")
//    fun uploadImage(
//        @Part image: MultipartBody.Part,
//        @Part("desc") desc: RequestBody
//    ): Call<UploadResponse>

    companion object {
        fun invoke(): paymentAPI {
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