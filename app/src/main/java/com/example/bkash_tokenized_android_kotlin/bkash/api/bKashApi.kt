package com.example.bkash_tokenized_android_kotlin.bkash.api

import com.example.bkash_tokenized_android_kotlin.bkash.model.CreatePaymentBodyRequest
import com.example.bkash_tokenized_rnd.bkash.model.*
import retrofit2.Call
import retrofit2.http.*


interface ApiInterface {
    @POST("/v1.2.0-beta/tokenized/checkout/token/grant")
    fun postGrantToken(
        @Header("username") username: String?,
        @Header("password") password: String?,
        @Body grantTokenBodyRequest: GrantTokenBodyRequest
    ): Call<GrantTokenResponse>

    @POST("/v1.2.0-beta/tokenized/checkout/create")
    fun postPaymentCreate(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body createPaymentBody: CreatePaymentBodyRequest
    ): Call<CreatePaymentResponse>

    @POST("/v1.2.0-beta/tokenized/checkout/execute")
    fun postPaymentExecute(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body executePaymentBodyRequest: ExecutePaymentBodyRequest
    ): Call<ExecutePaymentResponse>

    @POST("/v1.2.0-beta/tokenized/checkout/general/searchTransaction")
    fun postSearchTransaction(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body searchTransactionBodyRequest: SearchTransactionBodyRequest
    ): Call<SearchTransactionResponse>

    @POST("/v1.2.0-beta/tokenized/checkout/payment/status")
    fun postQueryPayment(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body queryPaymentBodyRequest: QueryPaymentBodyRequest
    ): Call<QueryPaymentResponse>
}