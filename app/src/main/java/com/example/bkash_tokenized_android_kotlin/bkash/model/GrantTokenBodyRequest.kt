package com.example.bkash_tokenized_android_kotlin.bkash.model

import com.google.gson.annotations.SerializedName

data class GrantTokenBodyRequest (
  @SerializedName("app_key")
  var appKey : String? = null,
  @SerializedName("app_secret")
  var appSecret  : String? = null
)