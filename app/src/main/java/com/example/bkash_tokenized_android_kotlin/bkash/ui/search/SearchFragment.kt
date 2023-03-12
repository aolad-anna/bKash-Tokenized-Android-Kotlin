package com.example.bkash_tokenized_android_kotlin.bkash.ui.search

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bkash_tokenized_android_kotlin.bkash.Constants
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.pd
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.searchTextInput
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.bkash.network.InternetConnection
import com.example.bkash_tokenized_android_kotlin.databinding.FragmentSearchBinding
import com.example.bkash_tokenized_android_kotlin.bkash.ui.home.HomeViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
    private val searchViewModel by viewModels<SearchViewModel>()
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
            if(InternetConnection.isOnline(requireActivity())){
                if (binding.textView2Search.text.isNotEmpty()){
                    searchTextInput = binding.textView2Search.text.toString()
                    hideKeyboard()
                    pd = ProgressDialog(requireContext(), R.style.DialogStyle)
                    pd?.setMessage("Searching")
                    pd?.setCancelable(false)
                    pd!!.show()
                    grantBkashToken()
                }
                else{
                    Toast.makeText(requireContext(), "Please Enter Valid TrxId!!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun grantBkashToken() {
        viewModel.getGrantTokenObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                Constants.sessionIdToken = it.idToken.toString()
                if (it.statusCode != "0000") {
                    pd!!.dismiss()
                    Toast.makeText(activity, it.statusMessage, Toast.LENGTH_SHORT).show()
                }
                searchBkashTransaction()
                pd!!.dismiss()
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                pd!!.dismiss()
            }
        }
        viewModel.grantTokenApiCall()
    }

    private fun searchBkashTransaction() {
        searchViewModel.getSearchPaymentObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.statusCode == "0000"){
                        pd!!.dismiss()
                        binding.statusMessageSearch.text = it.statusMessage
                        binding.trxIDSearch.text = "trxID: ${it.trxID}"
                        binding.transactionStatusSearch.text = "Transaction Status: ${it.transactionStatus}"
                        binding.transactionTypeSearch.text = "Transaction Type:\n ${it.transactionType}"
                        binding.amountSearch.text = "Amount: ${it.amount}"
                        binding.customerMsisdnSearch.text = "Customer Msisdn: ${it.customerMsisdn}"
                        binding.initiationTimeSearch.text = "Initiation Time:\n ${it.initiationTime}"
                        binding.completedTimeSearch.text = "Completed Time:\n ${it.completedTime}"
                    }
                    else{
                        pd!!.dismiss()
                        binding.statusMessageSearch.text = it.statusMessage
                    }
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
            }
        }
        searchViewModel.searchPaymentApiCall()
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