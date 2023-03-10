package com.example.bkash_tokenized_android_kotlin.bkash.model

import com.google.gson.annotations.SerializedName

data class SearchTransactionBodyRequest(
  @SerializedName("trxID")
  var trxID: String? = null,
  )