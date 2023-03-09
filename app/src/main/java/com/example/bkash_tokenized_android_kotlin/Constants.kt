package com.example.bkash_tokenized_android_kotlin

import androidx.lifecycle.MutableLiveData

object Constants {
    var bkashSandboxUsername = "your_sandbox_user_name"
    var bkashSandboxPassword = "your_sandbox_password_name"
    var bkashSandboxAppKey = "your_sandbox_app_key"
    var bkashSandboxAppSecret = "your_sandbox_app_secret"
    var mode = "0011"
    var payerReference = "01770618575"
    var callbackURL = "your_callback_yourDomain.com"
    var merchantAssociationInfo = ""
    var amount = "30"
    var currency = "BDT"
    var intents = "sale"
    var merchantInvoiceNumber = "Inv0124"

    var sessionIdToken = ""
    var paymentIDBkash = ""
    val liveData = MutableLiveData<Boolean>()
}
