package com.example.project

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface BankAdminAPI {
    @GET("bank/")
    fun receieveBank(): Call<List<Bank>>

    @FormUrlEncoded
    @PUT("editbankadmin/{bank_admin_id}")
    fun editBankAccount(
        @Path("bank_admin_id") bank_admin_id:String,
        @Field("bank_name") bank_name:String,
        @Field("account_number") account_number:String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("qr_code") qr_code:String
    ): Call<Bank>

    @FormUrlEncoded
    @POST("add")
    fun addBankAccount(
        @Field("bank_admin_id") bank_admin_id:String,
        @Field("bank_name") bank_name:String,
        @Field("account_number") account_number:String,
        @Field("firstName") firstName:String,
        @Field("lastName") lastName:String,
        @Field("qr_code") qr_code:String
    ): Call<Bank>

    companion object {
        fun create(): BankAdminAPI {
            val serv: BankAdminAPI = Retrofit.Builder()
                .baseUrl("http://10.0.2.2:3000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(BankAdminAPI::class.java)
            return serv
        }
    }
}