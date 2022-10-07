package com.example.project

import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BankAdminAPI {
    @GET("bank/")
    fun receieveBank(): Call<List<Bank>>

    @Multipart
    @PUT("editbankadmin/{bank_admin_id}")
    fun editBankAccount(
        @Part image: MultipartBody.Part,
        @Path("bank_admin_id") bank_admin_id: String,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number: RequestBody,
        @Part("firstName") firstName: RequestBody,
        @Part("lastName") lastName: RequestBody,
    ): Call<UploadResponse>

    @POST("add")
    @Multipart
    fun addBankAccount(
        @Part image: MultipartBody.Part,
        @Part("bank_name") bank_name: RequestBody,
        @Part("account_number") account_number:RequestBody,
        @Part("firstName") firstName:RequestBody,
        @Part("lastName") lastName:RequestBody
    ): Call<UploadResponse>

    companion object {
        fun create(): BankAdminAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            val serv: BankAdminAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(BankAdminAPI::class.java)
            return serv
        }

        operator fun invoke(): BankAdminAPI {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
                .create(BankAdminAPI::class.java)
        }
    }
}