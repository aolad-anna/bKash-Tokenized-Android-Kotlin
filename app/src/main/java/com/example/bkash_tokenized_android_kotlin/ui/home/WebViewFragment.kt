package com.example.bkash_tokenized_android_kotlin.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.bkash_tokenized_android_kotlin.Constants
import com.example.bkash_tokenized_android_kotlin.R

class WebViewFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(
            STYLE_NORMAL,
            R.style.FullScreenDialogStyle
        )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?, ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_web_view, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val HtmlUrl = arguments?.getString("bKashUrl")
        val paymentID = arguments?.getString("paymentID")

        val myWebView = getView()?.findViewById<WebView>(R.id.WebView)
        val progressBar = getView()?.findViewById<ProgressBar>(R.id.progressbar)
        myWebView?.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }

        myWebView?.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar?.visibility = View.VISIBLE
            }
        }

        myWebView?.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                progressBar?.progress = newProgress
                if (newProgress == 100) {
                    progressBar?.visibility = View.GONE
                }
            }
        }

        myWebView?.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if(url.toString().contains("status=success")){
                    Constants.liveData.value = true
                    dialog?.dismiss()
                }
                else if(url.toString().contains("status=failure")){
                    Toast.makeText(requireContext(), "Payment Failure!! \n Try Again!!", Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
                else if(url.toString().contains("status=cancel")){
                    Toast.makeText(requireContext(), "Payment Canceled!!", Toast.LENGTH_SHORT).show()
                    dialog?.dismiss()
                }
            }
        }

        if (HtmlUrl != null) {
            myWebView?.loadUrl(HtmlUrl)
        }
        myWebView?.settings?.javaScriptEnabled = true
        myWebView?.settings?.allowContentAccess = true
        myWebView?.settings?.domStorageEnabled = true
        myWebView?.settings?.useWideViewPort = true
    }
}