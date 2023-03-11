package com.example.bkash_tokenized_android_kotlin.bkash.ui.home

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.bkash_tokenized_android_kotlin.R
import com.example.bkash_tokenized_android_kotlin.databinding.BottomSheetDialogBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetFragment : BottomSheetDialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val dialogBinding = BottomSheetDialogBinding.inflate(layoutInflater)

        val statusMessageBkash = arguments?.getString("statusMessage")!!
        val trxIDBkash = arguments?.getString("trxID")
        val statusCodeBkash = arguments?.getString("statusCode")

        dialog.setContentView(dialogBinding.root)
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        val parent = dialogBinding.root.parent as View
        val bottomSheetBehavior = BottomSheetBehavior.from(parent)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED

        if (statusCodeBkash == "0000"){
            dialogBinding.iconSuccess.setImageResource(R.drawable.success)
            dialogBinding.titleSuccess.text = statusMessageBkash
            if (trxIDBkash != null){
                dialogBinding.subTitleSuccess.text = "trxID: $trxIDBkash"
            }
        }

        else if (statusCodeBkash == "2023"){
            dialogBinding.iconSuccess.setImageResource(R.drawable.no_balance)
            dialogBinding.titleSuccess.text = statusMessageBkash
            if (trxIDBkash != null){
                dialogBinding.subTitleSuccess.text = "trxID: $trxIDBkash"
            }
        }
        else{
            dialogBinding.iconSuccess.setImageResource(R.drawable.error)
            dialogBinding.titleSuccess.text = statusMessageBkash
            if (trxIDBkash != null){
                dialogBinding.subTitleSuccess.text = "trxID: $trxIDBkash"
            }
        }

        val btnClose = dialog.findViewById<Button>(R.id.okayButton)
        btnClose?.setOnClickListener {
            dialog.dismiss()
        }

        return dialog
    }

    override fun getTheme(): Int = R.style.SheetDialog
}