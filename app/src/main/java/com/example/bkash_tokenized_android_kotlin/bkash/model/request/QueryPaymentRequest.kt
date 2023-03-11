package com.example.bkash_tokenized_android_kotlin.bkash.model.request

import com.google.gson.annotations.SerializedName

data class QueryPaymentRequest(
  @SerializedName("paymentID")
  var paymentID: String? = null,
  )