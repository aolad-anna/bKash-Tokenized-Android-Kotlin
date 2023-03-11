package com.example.bkash_tokenized_android_kotlin.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.Constants.amount
import com.example.bkash_tokenized_android_kotlin.Constants.bkashSandboxAppKey
import com.example.bkash_tokenized_android_kotlin.Constants.bkashSandboxAppSecret
import com.example.bkash_tokenized_android_kotlin.Constants.bkashSandboxPassword
import com.example.bkash_tokenized_android_kotlin.Constants.bkashSandboxUsername
import com.example.bkash_tokenized_android_kotlin.Constants.callbackURL
import com.example.bkash_tokenized_android_kotlin.Constants.currency
import com.example.bkash_tokenized_android_kotlin.Constants.intents
import com.example.bkash_tokenized_android_kotlin.Constants.merchantAssociationInfo
import com.example.bkash_tokenized_android_kotlin.Constants.merchantInvoiceNumber
import com.example.bkash_tokenized_android_kotlin.Constants.mode
import com.example.bkash_tokenized_android_kotlin.Constants.payerReference
import com.example.bkash_tokenized_android_kotlin.Constants.paymentIDBkash
import com.example.bkash_tokenized_android_kotlin.Constants.sessionIdToken
import com.example.bkash_tokenized_android_kotlin.SingleLiveEvent
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val job = Job()
    private val grantTokenLiveData = SingleLiveEvent<GrantTokenResponse?>()
    private val createPaymentLiveData = SingleLiveEvent<CreatePaymentResponse?>()
    private val executePaymentLiveData = SingleLiveEvent<ExecutePaymentResponse?>()
    private val queryPaymentLiveData = SingleLiveEvent<QueryPaymentResponse?>()
    fun getGrantTokenObserver(): SingleLiveEvent<GrantTokenResponse?> {
        return grantTokenLiveData
    }
    fun getCreatePaymentObserver(): SingleLiveEvent<CreatePaymentResponse?> {
        return createPaymentLiveData
    }
    fun getExecutePaymentObserver(): SingleLiveEvent<ExecutePaymentResponse?> {
        return executePaymentLiveData
    }
    fun getQueryPaymentObserver(): SingleLiveEvent<QueryPaymentResponse?> {
        return queryPaymentLiveData
    }
    fun grantTokenApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postGrantToken(
                username = bkashSandboxUsername,
                password = bkashSandboxPassword,
                GrantTokenBodyRequest(
                    appKey = bkashSandboxAppKey,
                    appSecret = bkashSandboxAppSecret
                )
            )
            grantTokenLiveData.postValue(response)
        }
    }

    fun createPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentCreate(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                CreatePaymentBodyRequest(
                    mode = mode,
                    payerReference = payerReference,
                    callbackURL = callbackURL,
                    merchantAssociationInfo = merchantAssociationInfo,
                    amount = amount,
                    currency = currency,
                    intent = intents,
                    merchantInvoiceNumber = merchantInvoiceNumber,
                )
            )
            createPaymentLiveData.postValue(response)
        }
    }

    fun executePaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentExecute(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                ExecutePaymentBodyRequest(
                    paymentID = paymentIDBkash
                )
            )
            executePaymentLiveData.postValue(response)
        }
    }

    fun queryPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + job) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postQueryPayment(
                authorization = "Bearer $sessionIdToken",
                xAppKey = bkashSandboxAppKey,
                QueryPaymentBodyRequest(
                    paymentID = paymentIDBkash
                )
            )
            queryPaymentLiveData.postValue(response)
        }
    }

    private val coroutineExceptionHandler = CoroutineExceptionHandler{ _, throwable ->
        Constants.pd?.dismiss()
        throwable.printStackTrace()
    }
}