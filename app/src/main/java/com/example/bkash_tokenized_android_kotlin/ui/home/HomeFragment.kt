package com.example.bkash_tokenized_android_kotlin.ui.home

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.CreatePaymentBodyRequest
import com.example.bkash_tokenized_android_kotlin.databinding.FragmentHomeBinding
import com.example.bkash_tokenized_rnd.bkash.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private var pd: ProgressDialog?= null

    companion object {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Constants.liveData.value = false

        binding.bKashButton.setOnClickListener {
            if (binding.textView2.text.isNotEmpty()) {
                Constants.amount = binding.textView2.text.toString()
                pd = ProgressDialog(requireContext(), R.style.DialogStyle)
                pd?.setMessage("Processing")
                pd?.setCancelable(false)
                pd!!.show()
                hideKeyboard()
                grantBkashToken()
            } else
                Toast.makeText(requireContext(), "Please Enter Valid Amount!!", Toast.LENGTH_SHORT).show()
        }

        Constants.liveData.observe(requireActivity()){
            if (Constants.liveData.value == true){
                executeBkashPayment()
                pd!!.show()
                Constants.liveData.value = false
            }
        }
    }

    private fun grantBkashToken() {
        val apiService = BkashApiClient.client!!.create(ApiInterface::class.java)
        val call: Call<GrantTokenResponse> = apiService.postGrantToken(
            username = Constants.bkashSandboxUsername,
            password = Constants.bkashSandboxPassword,
            GrantTokenBodyRequest(
                appKey = Constants.bkashSandboxAppKey,
                appSecret = Constants.bkashSandboxAppSecret
            )
        )
        call.enqueue(object : Callback<GrantTokenResponse> {
            override fun onResponse(call: Call<GrantTokenResponse>, response: Response<GrantTokenResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.statusCode == "0000"){
                        Constants.sessionIdToken = response.body()?.idToken.toString()
                        createBkashPayment()
                        pd!!.dismiss()
                        binding.bKashButton.visibility = View.VISIBLE
                    }
                    else{
                        Toast.makeText(requireContext(), response.body()?.statusMessage, Toast.LENGTH_SHORT).show()
                        pd!!.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<GrantTokenResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Something Went To Wrong", Toast.LENGTH_SHORT).show()
                binding.bKashButton.visibility = View.VISIBLE
                pd!!.dismiss()
            }
        })
    }

    private fun createBkashPayment() {
        val apiService = BkashApiClient.client!!.create(ApiInterface::class.java)
        val call: Call<CreatePaymentResponse> = apiService.postPaymentCreate(
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
        call.enqueue(object : Callback<CreatePaymentResponse> {
            @SuppressLint("RestrictedApi")
            override fun onResponse(call: Call<CreatePaymentResponse>, response: Response<CreatePaymentResponse>) {
                if (response.isSuccessful) {
                    Constants.paymentIDBkash = response.body()?.paymentID.toString()
                    pd!!.dismiss()
                    val args = Bundle().apply {
                        putString("bKashUrl", response.body()?.bkashURL.toString())
                        putString("paymentID", response.body()?.paymentID.toString())
                    }
                    findNavController().navigate(R.id.webViewDialog, args)
                }
            }

            override fun onFailure(call: Call<CreatePaymentResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Something Went To Wrong", Toast.LENGTH_SHORT).show()
                binding.bKashButton.visibility = View.VISIBLE
                pd!!.dismiss()
            }
        })
    }

    private fun executeBkashPayment() {
        val apiService = BkashApiClient.client!!.create(ApiInterface::class.java)
        val call: Call<ExecutePaymentResponse> = apiService.postPaymentExecute(
            authorization = "Bearer ${Constants.sessionIdToken}",
            xAppKey = Constants.bkashSandboxAppKey,
            ExecutePaymentBodyRequest(
                paymentID = Constants.paymentIDBkash
            )
        )
        call.enqueue(object : Callback<ExecutePaymentResponse> {
            override fun onResponse(call: Call<ExecutePaymentResponse>, response: Response<ExecutePaymentResponse>) {
                if (response.isSuccessful) {
                    pd?.dismiss()
                    val args = Bundle().apply {
                        putString("statusMessage", response.body()?.statusMessage)
                        putString("trxID", response.body()?.trxID)
                        putString("statusCode", response.body()?.statusCode)
                    }
                    findNavController().navigate(R.id.bottomSheetDialog, args)
                }
                else if (response.body() == null){
                    pd?.dismiss()
                    queryBkashPayment()
                }
            }

            override fun onFailure(call: Call<ExecutePaymentResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Something Went To Wrong", Toast.LENGTH_SHORT).show()
                pd?.dismiss()
                queryBkashPayment()
            }
        })
    }

    private fun queryBkashPayment() {
        val apiService = BkashApiClient.client!!.create(ApiInterface::class.java)
        val call: Call<QueryPaymentResponse> = apiService.postQueryPayment(
            authorization = "Bearer ${Constants.sessionIdToken}",
            xAppKey = Constants.bkashSandboxAppKey,
            QueryPaymentBodyRequest(
                paymentID = Constants.paymentIDBkash
            )
        )
        call.enqueue(object : Callback<QueryPaymentResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<QueryPaymentResponse>, response: Response<QueryPaymentResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.transactionStatus == "Initiated"){
                        grantBkashToken()
                    }
                    else if (response.body()?.transactionStatus == "Completed"){
                        val args = Bundle().apply {
                            putString("statusMessage", response.body()?.statusMessage)
                            putString("statusCode", response.body()?.statusCode)
                        }
                        findNavController().navigate(R.id.bottomSheetDialog, args)
                    }
                }
            }

            override fun onFailure(call: Call<QueryPaymentResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Something Went To Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun hideKeyboard(){
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}