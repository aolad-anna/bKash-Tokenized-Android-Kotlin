package com.example.bkash_tokenized_android_kotlin.ui.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.SearchTransactionBodyRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.SearchTransactionResponse
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {

    var searchPaymentLiveData : MutableLiveData<SearchTransactionResponse?> = MutableLiveData()
    fun getSearchPaymentObserver(): MutableLiveData<SearchTransactionResponse?> {
        return searchPaymentLiveData
    }
    fun searchPaymentApiCall() {
        viewModelScope.launch(Dispatchers.IO + coroutineExceptionHandler) {
            val bkashApiClient = BkashApiClient.client?.create(ApiInterface::class.java)
            val response  = bkashApiClient?.postSearchTransaction(
                authorization = "Bearer ${Constants.sessionIdToken}",
                xAppKey = Constants.bkashSandboxAppKey,
                SearchTransactionBodyRequest(
                    trxID = Constants.searchTextInput
                )
            )
            searchPaymentLiveData.postValue(response)
        }
    }

    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
        throwable.printStackTrace()
    }
}