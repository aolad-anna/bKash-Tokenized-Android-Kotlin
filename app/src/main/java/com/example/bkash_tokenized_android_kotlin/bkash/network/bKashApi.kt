package com.example.bkash_tokenized_android_kotlin.bkash.network

import com.example.bkash_tokenized_android_kotlin.bkash.model.*
import com.example.bkash_tokenized_android_kotlin.bkash.model.request.*
import com.example.bkash_tokenized_android_kotlin.bkash.model.response.*
import retrofit2.http.*


interface ApiInterface {
    @POST("/v1.2.0-beta/tokenized/checkout/token/grant")
    suspend fun postGrantToken(
        @Header("username") username: String?,
        @Header("password") password: String?,
        @Body grantTokenRequest: GrantTokenRequest
    ): GrantTokenResponse

    @POST("/v1.2.0-beta/tokenized/checkout/create")
    suspend fun postPaymentCreate(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body createPaymentRequest: CreatePaymentRequest
    ): CreatePaymentResponse

    @POST("/v1.2.0-beta/tokenized/checkout/execute")
    suspend fun postPaymentExecute(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body executePaymentRequest: ExecutePaymentRequest
    ): ExecutePaymentResponse

    @POST("/v1.2.0-beta/tokenized/checkout/general/searchTransaction")
    suspend fun postSearchTransaction(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body searchTransactionRequest: SearchTransactionRequest
    ): SearchTransactionResponse

    @POST("/v1.2.0-beta/tokenized/checkout/payment/status")
    suspend fun postQueryPayment(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body queryPaymentRequest: QueryPaymentRequest
    ): QueryPaymentResponse
}