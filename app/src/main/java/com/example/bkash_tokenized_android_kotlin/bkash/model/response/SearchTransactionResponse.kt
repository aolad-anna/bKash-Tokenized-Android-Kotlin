package com.example.bkash_tokenized_android_kotlin.bkash.model.response

import com.google.gson.annotations.SerializedName


data class SearchTransactionResponse (
	@SerializedName("trxID")
	var trxID : String? = null,
	@SerializedName("initiationTime")
	var initiationTime : String? = null,
	@SerializedName("completedTime")
	var completedTime : String? = null,
	@SerializedName("transactionType")
	var transactionType : String? = null,
	@SerializedName("customerMsisdn")
	var customerMsisdn : String? = null,
	@SerializedName("transactionStatus")
	var transactionStatus : String? = null,
	@SerializedName("amount")
	var amount : String? = null,
	@SerializedName("currency")
	var currency : String? = null,
	@SerializedName("organizationShortCode")
	var organizationShortCode : String? = null,
	@SerializedName("statusCode")
	var statusCode : String? = null,
	@SerializedName("statusMessage")
	var statusMessage : String? = null
)