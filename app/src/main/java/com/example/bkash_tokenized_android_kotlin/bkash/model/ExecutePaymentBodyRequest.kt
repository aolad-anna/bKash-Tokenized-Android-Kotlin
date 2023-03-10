package com.example.bkash_tokenized_android_kotlin.bkash.model

import com.google.gson.annotations.SerializedName

data class ExecutePaymentBodyRequest(
  @SerializedName("paymentID")
  var paymentID: String? = null,
  )