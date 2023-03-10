package com.example.bkash_tokenized_android_kotlin.bkash.api

import com.example.bkash_tokenized_android_kotlin.bkash.model.*
import retrofit2.http.*


interface ApiInterface {
    @POST("/v1.2.0-beta/tokenized/checkout/token/grant")
    suspend fun postGrantToken(
        @Header("username") username: String?,
        @Header("password") password: String?,
        @Body grantTokenBodyRequest: GrantTokenBodyRequest
    ): GrantTokenResponse

    @POST("/v1.2.0-beta/tokenized/checkout/create")
    suspend fun postPaymentCreate(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body createPaymentBody: CreatePaymentBodyRequest
    ): CreatePaymentResponse

    @POST("/v1.2.0-beta/tokenized/checkout/execute")
    suspend fun postPaymentExecute(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body executePaymentBodyRequest: ExecutePaymentBodyRequest
    ): ExecutePaymentResponse

    @POST("/v1.2.0-beta/tokenized/checkout/general/searchTransaction")
    suspend fun postSearchTransaction(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body searchTransactionBodyRequest: SearchTransactionBodyRequest
    ): SearchTransactionResponse

    @POST("/v1.2.0-beta/tokenized/checkout/payment/status")
    suspend fun postQueryPayment(
        @Header("authorization") authorization: String?,
        @Header("x-app-key") xAppKey: String?,
        @Body queryPaymentBodyRequest: QueryPaymentBodyRequest
    ): QueryPaymentResponse
}