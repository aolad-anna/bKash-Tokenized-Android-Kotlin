package com.example.bkash_tokenized_android_kotlin.ui.search

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.databinding.FragmentSearchBinding
import com.example.bkash_tokenized_android_kotlin.ui.home.HomeFragment
import com.example.bkash_tokenized_rnd.bkash.model.GrantTokenBodyRequest
import com.example.bkash_tokenized_rnd.bkash.model.GrantTokenResponse
import com.example.bkash_tokenized_rnd.bkash.model.SearchTransactionBodyRequest
import com.example.bkash_tokenized_rnd.bkash.model.SearchTransactionResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private var pd: ProgressDialog?= null

    companion object {
        var searchTextInput = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchBtn.setOnClickListener {
            if (binding.textView2Search.text.isNotEmpty()){
                searchTextInput = binding.textView2Search.text.toString()
                hideKeyboard()
                pd = ProgressDialog(requireContext(), R.style.DialogStyle)
                pd?.setMessage("Searching")
                pd?.setCancelable(false)
                pd!!.show()
                grantBkashToken()
            }
            else
                Toast.makeText(requireContext(), "Please Enter Valid TrxId!!", Toast.LENGTH_SHORT).show()
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
                        searchBkashTransaction()
                    }
                    else{
                        Toast.makeText(requireContext(), response.body()?.statusMessage, Toast.LENGTH_SHORT).show()
                        pd!!.dismiss()
                    }
                }
            }

            override fun onFailure(call: Call<GrantTokenResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Something Went To Wrong", Toast.LENGTH_SHORT).show()
            }
        })
    }


    private fun searchBkashTransaction() {
        val apiService = BkashApiClient.client!!.create(ApiInterface::class.java)
        val call: Call<SearchTransactionResponse> = apiService.postSearchTransaction(
            authorization = "Bearer ${Constants.sessionIdToken}",
            xAppKey = Constants.bkashSandboxAppKey,
            SearchTransactionBodyRequest(
                trxID = searchTextInput
            )
        )
        call.enqueue(object : Callback<SearchTransactionResponse> {
            @SuppressLint("SetTextI18n")
            override fun onResponse(call: Call<SearchTransactionResponse>, response: Response<SearchTransactionResponse>) {
                if (response.isSuccessful) {
                    if (response.body()?.statusCode == "0000"){
                        pd!!.dismiss()
                        binding.statusMessageSearch.text = response.body()?.statusMessage
                        binding.trxIDSearch.text = "trxID: ${response.body()?.trxID}"
                        binding.transactionStatusSearch.text = "Transaction Status: ${response.body()?.transactionStatus}"
                        binding.transactionTypeSearch.text = "Transaction Type:\n ${response.body()?.transactionType}"
                        binding.amountSearch.text = "Amount: ${response.body()?.amount}"
                        binding.customerMsisdnSearch.text = "Customer Msisdn: ${response.body()?.customerMsisdn}"
                        binding.initiationTimeSearch.text = "Initiation Time:\n ${response.body()?.initiationTime}"
                        binding.completedTimeSearch.text = "Completed Time:\n ${response.body()?.completedTime}"
                    }
                    else{
                        pd!!.dismiss()
                        binding.statusMessageSearch.text = response.body()?.statusMessage
                    }
                }
            }

            override fun onFailure(call: Call<SearchTransactionResponse>, t: Throwable) {
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