package com.example.bkash_tokenized_android_kotlin.bkash.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized_android_kotlin.bkash.Constants
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.amount
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.bkashSandboxAppKey
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.bkashSandboxAppSecret
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.bkashSandboxPassword
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.bkashSandboxUsername
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.callbackURL
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.currency
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.intents
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.merchantAssociationInfo
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.merchantInvoiceNumber
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.mode
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.payerReference
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.paymentIDBkash
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.sessionIdToken
import com.example.bkash_tokenized_android_kotlin.bkash.SingleLiveEvent
import com.example.bkash_tokenized_android_kotlin.bkash.network.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.network.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.request.CreatePaymentRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.request.ExecutePaymentRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.request.GrantTokenRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.request.QueryPaymentRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.response.CreatePaymentResponse
import com.example.bkash_tokenized_android_kotlin.bkash.model.response.ExecutePaymentResponse
import com.example.bkash_tokenized_android_kotlin.bkash.model.response.GrantTokenResponse
import com.example.bkash_tokenized_android_kotlin.bkash.model.response.QueryPaymentResponse
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
                GrantTokenRequest(
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
                CreatePaymentRequest(
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
                ExecutePaymentRequest(
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
                QueryPaymentRequest(
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