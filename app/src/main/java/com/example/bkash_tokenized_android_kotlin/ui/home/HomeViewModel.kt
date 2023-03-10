package com.example.bkash_tokenized_android_kotlin.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var grantTokenLiveData : MutableLiveData<GrantTokenResponse?> = MutableLiveData()
    var createPaymentLiveData : MutableLiveData<CreatePaymentResponse?> = MutableLiveData()
    var executePaymentLiveData : MutableLiveData<ExecutePaymentResponse?> = MutableLiveData()
    var queryPaymentLiveData : MutableLiveData<QueryPaymentResponse?> = MutableLiveData()
    fun getGrantTokenObserver(): MutableLiveData<GrantTokenResponse?> {
        return grantTokenLiveData
    }
    fun getCreatePaymentObserver(): MutableLiveData<CreatePaymentResponse?> {
        return createPaymentLiveData
    }
    fun getExecutePaymentObserver(): MutableLiveData<ExecutePaymentResponse?> {
        return executePaymentLiveData
    }
    fun getQueryPaymentObserver(): MutableLiveData<QueryPaymentResponse?> {
        return queryPaymentLiveData
    }
    fun grantTokenApiCall() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postGrantToken(
                Constants.bkashSandboxUsername,
                Constants.bkashSandboxPassword,
                GrantTokenBodyRequest(
                    Constants.bkashSandboxAppKey,
                    Constants.bkashSandboxAppSecret
                )
            )
            grantTokenLiveData.postValue(response)
        }
    }

    fun createPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentCreate(
                authorization = "Bearer ${Constants.sessionIdToken}",
                xAppKey = Constants.bkashSandboxAppKey,
                CreatePaymentBodyRequest(
                    mode = Constants.mode,
                    payerReference = Constants.payerReference,
                    callbackURL = Constants.callbackURL,
                    merchantAssociationInfo = Constants.merchantAssociationInfo,
                    amount = Constants.amount,
                    currency = Constants.currency,
                    intent = Constants.intents,
                    merchantInvoiceNumber = Constants.merchantInvoiceNumber,
                )
            )
            createPaymentLiveData.postValue(response)
        }
    }

    fun executePaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postPaymentExecute(
                authorization = "Bearer ${Constants.sessionIdToken}",
                xAppKey = Constants.bkashSandboxAppKey,
                ExecutePaymentBodyRequest(
                    paymentID = Constants.paymentIDBkash
                )
            )
            executePaymentLiveData.postValue(response)
        }
    }

    fun queryPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postQueryPayment(
                authorization = "Bearer ${Constants.sessionIdToken}",
                xAppKey = Constants.bkashSandboxAppKey,
                QueryPaymentBodyRequest(
                    paymentID = Constants.paymentIDBkash
                )
            )
            queryPaymentLiveData.postValue(response)
        }
    }

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
}