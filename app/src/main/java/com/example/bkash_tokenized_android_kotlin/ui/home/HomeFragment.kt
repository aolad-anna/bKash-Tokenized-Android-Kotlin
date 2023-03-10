package com.example.bkash_tokenized_android_kotlin.ui.home

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.bkash.api.ApiInterface
import com.example.bkash_tokenized_android_kotlin.bkash.api.BkashApiClient
import com.example.bkash_tokenized_android_kotlin.bkash.model.CreatePaymentBodyRequest
import com.example.bkash_tokenized_android_kotlin.bkash.model.GrantTokenBodyRequest
import com.example.bkash_tokenized_android_kotlin.databinding.FragmentHomeBinding
import com.example.bkash_tokenized_rnd.bkash.model.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response



class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
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
        viewModel.getGrantTokenObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                Constants.sessionIdToken = it.idToken.toString()
                if (it.statusCode != "0000") {
                    pd!!.dismiss()
                    Toast.makeText(activity, it.statusMessage, Toast.LENGTH_SHORT).show()
                }
                createBkashPayment()
                pd!!.dismiss()
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                pd!!.dismiss()
            }
        }
        viewModel.grantTokenApiCall()
    }

    private fun createBkashPayment() {
        viewModel.getCreatePaymentObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                if (it.statusCode != "0000") {
                    pd!!.dismiss()
                    Toast.makeText(activity, it.statusMessage, Toast.LENGTH_SHORT).show()
                }
                else{
                    Constants.paymentIDBkash = it.paymentID.toString()
                    val args = Bundle().apply {
                        putString("bKashUrl", it.bkashURL.toString())
                        putString("paymentID", it.paymentID.toString())
                    }
                    pd!!.dismiss()
                    findNavController().navigate(R.id.webViewDialog, args)
                }
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                pd!!.dismiss()
            }
        }
        viewModel.createPaymentApiCall()
    }

    private fun executeBkashPayment() {
        viewModel.getExecutePaymentObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                pd?.dismiss()
                    val args = Bundle().apply {
                        putString("statusMessage", it.statusMessage)
                        putString("trxID", it.trxID)
                        putString("statusCode", it.statusCode)
                    }
                    findNavController().navigate(R.id.bottomSheetDialog, args)
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                queryBkashPayment()
            }
        }
        viewModel.executePaymentApiCall()
    }

    private fun queryBkashPayment() {
        viewModel.getQueryPaymentObserver().observe(viewLifecycleOwner) {
            if (it != null) {
                pd?.dismiss()
                if (it.transactionStatus == "Initiated"){
                        grantBkashToken()
                    }
                else if (it.transactionStatus == "Completed"){
                    val args = Bundle().apply {
                        putString("statusMessage", it.statusMessage)
                        putString("statusCode", it.statusCode)
                    }
                    findNavController().navigate(R.id.bottomSheetDialog, args)
                }
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.queryPaymentApiCall()
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