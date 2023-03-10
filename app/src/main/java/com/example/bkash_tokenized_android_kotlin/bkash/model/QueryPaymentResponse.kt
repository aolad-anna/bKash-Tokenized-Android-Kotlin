package com.example.bkash_tokenized_android_kotlin.bkash.model

import com.google.gson.annotations.SerializedName


data class QueryPaymentResponse (
	@SerializedName("paymentID")
	var paymentID : String? = null,
	@SerializedName("mode")
	var mode : String? = null,
	@SerializedName("paymentCreateTime")
	var paymentCreateTime : String? = null,
	@SerializedName("amount")
	var amount : String? = null,
	@SerializedName("currency")
	var currency : String? = null,
	@SerializedName("intent")
	var intent : String? = null,
	@SerializedName("merchantInvoice")
	var merchantInvoice : String? = null,
	@SerializedName("transactionStatus")
	var transactionStatus : String? = null,
	@SerializedName("verificationStatus")
	var verificationStatus : String? = null,
	@SerializedName("statusCode")
	var statusCode : String? = null,
	@SerializedName("statusMessage")
	var statusMessage : String? = null,
	@SerializedName("payerReference")
	var payerReference : String? = null,
	@SerializedName("agreementID" )
	var agreementID : String? = null,
	@SerializedName("agreementStatus")
	var agreementStatus : String? = null,
	@SerializedName("agreementCreateTime")
	var agreementCreateTime : String? = null,
	@SerializedName("agreementExecuteTime")
	var agreementExecuteTime : String? = null
)