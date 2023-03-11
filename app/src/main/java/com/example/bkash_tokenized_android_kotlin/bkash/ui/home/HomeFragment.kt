package com.example.bkash_tokenized_android_kotlin.bkash.ui.home

import android.app.ProgressDialog
import android.content.Context
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.amount
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.liveData
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.paymentIDBkash
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.pd
import com.example.bkash_tokenized_android_kotlin.bkash.Constants.sessionIdToken
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.bkash.network.InternetConnection
import com.example.bkash_tokenized_android_kotlin.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<HomeViewModel>()
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

            binding.bKashButton.safeClick({
                if (InternetConnection.isOnline(requireActivity())){
                    if (binding.textView2.text.isNotEmpty()) {
                        amount = binding.textView2.text.toString()
                        binding.textView2.setText("")
                        pd = ProgressDialog(requireContext(), R.style.DialogStyle)
                        pd?.setMessage("Processing")
                        pd?.setCancelable(false)
                        pd!!.show()
                        hideKeyboard()
                        grantBkashToken()
                    } else{
                        Toast.makeText(requireContext(), "Please Enter Valid Amount!!", Toast.LENGTH_SHORT).show()
                    }
                }
                else{
                    Toast.makeText(requireContext(), "No internet or data connection!", Toast.LENGTH_SHORT).show()
                }
            })

        liveData.value = false
        liveData.observe(requireActivity()){
            if (liveData.value == true){
                executeBkashPayment()
                pd!!.show()
                liveData.value = false
            }
        }
    }

    private fun grantBkashToken() {
        viewModel.getGrantTokenObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                runCatching {
                    sessionIdToken = it.idToken.toString()
                    if (it.statusCode != "0000") {
                        pd!!.dismiss()
                        Toast.makeText(activity, it.statusMessage, Toast.LENGTH_SHORT).show()
                        return@Observer
                    }
                    else {
                        createBkashPayment()
                        pd!!.dismiss()
                    }
                }
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                pd!!.dismiss()
            }
        })
        viewModel.grantTokenApiCall()
    }
    private fun createBkashPayment() {
        viewModel.getCreatePaymentObserver().observe(viewLifecycleOwner, Observer {
            if (it != null) {
                if (it.statusCode == "0000") {
                    paymentIDBkash = it.paymentID.toString()
                    pd!!.dismiss()
                    val args = Bundle().apply {
                        putString("bKashUrl", it.bkashURL.toString())
                        putString("paymentID", it.paymentID.toString())
                    }
                    findNavController().navigate(R.id.webViewDialog, args)
                }
                else{
                    Toast.makeText(requireContext(), it.statusMessage, Toast.LENGTH_SHORT).show()
                    pd!!.dismiss()
                }
            }
            else {
                Toast.makeText(activity, "Error in getting data", Toast.LENGTH_SHORT).show()
                pd!!.dismiss()
            }
        })
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

    private fun View.safeClick(action: View.OnClickListener, debounceTime: Long = 1000L) {
        this.setOnClickListener(object : View.OnClickListener {
            private var lastClickTime: Long = 0
            override fun onClick(v: View) {
                if (SystemClock.elapsedRealtime() - lastClickTime < debounceTime) return else action.onClick(v)
                lastClickTime = SystemClock.elapsedRealtime()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}