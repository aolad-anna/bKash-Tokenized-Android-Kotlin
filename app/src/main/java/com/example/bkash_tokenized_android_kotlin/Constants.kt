package com.example.bkash_tokenized_android_kotlin

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData

object Constants {
    var bkashSandboxUsername = "your sandbox user name"
    var bkashSandboxPassword = "your sandbox password name"
    var bkashSandboxAppKey = "your sandbox app key"
    var bkashSandboxAppSecret = "your sandbox app secret"
    var mode = "0011"
    var payerReference = "01770618575"
    var callbackURL = "your callback yourDomain.com"
    var merchantAssociationInfo = ""
    var amount = "30"
    var currency = "BDT"
    var intents = "sale"
    var merchantInvoiceNumber = "Inv0124"

    var sessionIdToken = ""
    var paymentIDBkash = ""
    var searchTextInput = ""
    var pd: ProgressDialog?= null
    val liveData = MutableLiveData<Boolean>()
}
