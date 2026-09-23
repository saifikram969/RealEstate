package com.btjnonbrokerage.DialogBox

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.btjnonbrokerage.databinding.FragmentAccountCreated2Binding

class AccountCreatedFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAccountCreated2Binding
    private var onFinishClick: (() -> Unit)? = null

    // Setter method to pass the callback function
    fun setOnFinishClickListener(listener: () -> Unit) {
        onFinishClick = listener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountCreated2Binding.inflate(layoutInflater, container, false)

        // Set the click listener for the finish button
        binding.tvFinish.setOnClickListener {
            onFinishClick?.invoke()
            dismiss()
        }

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        onFinishClick?.invoke() // Safely invoke the listener
    }
}